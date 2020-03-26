package com.blazebit.storage.nfs.executor;

import com.blazebit.storage.nfs.FileStats;
import com.blazebit.storage.nfs.StorageAccess;
import com.google.common.primitives.Longs;
import org.dcache.nfs.status.NoEntException;
import org.dcache.nfs.status.NotSuppException;
import org.dcache.nfs.v4.NfsIdMapping;
import org.dcache.nfs.v4.SimpleIdMap;
import org.dcache.nfs.v4.xdr.nfsace4;
import org.dcache.nfs.vfs.AclCheckable;
import org.dcache.nfs.vfs.DirectoryEntry;
import org.dcache.nfs.vfs.DirectoryStream;
import org.dcache.nfs.vfs.FsStat;
import org.dcache.nfs.vfs.Inode;
import org.dcache.nfs.vfs.Stat;
import org.dcache.nfs.vfs.VirtualFileSystem;

import javax.security.auth.Subject;
import java.io.IOException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class StorageAccessVirtualFileSystem implements VirtualFileSystem {

    // UNIX permissions bits
    public static final int S_IRUSR = 00400; // owner has read permission
    public static final int S_IWUSR = 00200; // owner has write permission
    public static final int S_IXUSR = 00100; // owner has execute permission
    public static final int S_IRGRP = 00040; // group has read permission
    public static final int S_IWGRP = 00020; // group has write permission
    public static final int S_IXGRP = 00010; // group has execute permission
    public static final int S_IROTH = 00004; // others have read permission
    public static final int S_IWOTH = 00002; // others have write permission
    public static final int S_IXOTH = 00001; // others have execute

    /**
     * Unix domain socket
     */
    public static final int S_IFSOCK = 0140000;
    /**
     * Symbolic link
     */
    public static final int S_IFLNK = 0120000;
    /**
     * Regular file
     */
    public static final int S_IFREG = 0100000;
    /**
     * BLock device
     */
    public static final int S_IFBLK = 0060000;
    /**
     * Directory
     */
    public static final int S_IFDIR = 0040000;

    private final StorageAccess storageAccess;
    private final ConcurrentMap<Long, String> inodeToPath = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Long> pathToInode = new ConcurrentHashMap<>();
    private final AtomicLong fileId = new AtomicLong(1); //numbering starts at 1
    private final NfsIdMapping _idMapper = new SimpleIdMap();

    public StorageAccessVirtualFileSystem(StorageAccess storageAccess) {
        this.storageAccess = storageAccess;
        map(fileId.getAndIncrement(), "/");
    }

    private Inode toFileHandle(long inodeNumber) {
        return Inode.forFile(Longs.toByteArray(inodeNumber));
    }

    private long toInodeNumber(Inode inode) {
        return Longs.fromByteArray(inode.getFileId());
    }

    private String resolveInode(long inodeNumber) throws NoEntException {
        String path = inodeToPath.get(inodeNumber);
        if (path == null) {
            throw new NoEntException("inode #" + inodeNumber);
        }
        return path;
    }

    private long resolvePath(String path) throws NoEntException {
        Long inodeNumber = pathToInode.get(path);
        if (inodeNumber == null) {
            throw new NoEntException("path " + path);
        }
        return inodeNumber;
    }

    private long resolveOrMapPath(String path) throws NoEntException {
        return pathToInode.computeIfAbsent(path, p -> {
            long inodeNumber = fileId.getAndIncrement();
            inodeToPath.putIfAbsent(inodeNumber, p);
            return inodeNumber;
        });
    }

    private void map(long inodeNumber, String path) {
        if (inodeToPath.putIfAbsent(inodeNumber, path) != null) {
            throw new IllegalStateException();
        }
        Long otherInodeNumber = pathToInode.putIfAbsent(path, inodeNumber);
        if (otherInodeNumber != null) {
            //try rollback
            if (inodeToPath.remove(inodeNumber) != path) {
                throw new IllegalStateException("cant map, rollback failed");
            }
            throw new IllegalStateException("path ");
        }
    }

    private void unmap(long inodeNumber, String path) {
        String removedPath = inodeToPath.remove(inodeNumber);
        if (!path.equals(removedPath)) {
            throw new IllegalStateException();
        }
        if (pathToInode.remove(path) != inodeNumber) {
            throw new IllegalStateException();
        }
    }

    private void remap(long inodeNumber, String oldPath, String newPath) {
        //TODO - attempt rollback?
        unmap(inodeNumber, oldPath);
        map(inodeNumber, newPath);
    }

    private String resolve(String parentPath, String path) {
        if (parentPath.charAt(parentPath.length() - 1) == '/') {
            return parentPath + path;
        } else {
            return parentPath + "/" + path;
        }
    }

    private String getParent(String path) {
        int idx = path.lastIndexOf('/', path.length() - 1);
        if (idx == -1) {
            return null;
        }
        return path.substring(0, idx + 1);
    }

    @Override
    public Inode create(Inode parent, Stat.Type type, String path, Subject subject, int mode) throws IOException {
        long parentInodeNumber = toInodeNumber(parent);
        String parentPath = resolveInode(parentInodeNumber);
        String newPath = resolve(parentPath, path);
        long newInodeNumber = fileId.getAndIncrement();
        map(newInodeNumber, newPath);
        return toFileHandle(newInodeNumber);
    }

    @Override
    public FsStat getFsStat() throws IOException {
//        FileStore store = Files.getFileStore(_root);
//        long total = store.getTotalSpace();
//        long free = store.getUsableSpace();
        long total = Long.MAX_VALUE;
        long free = Long.MAX_VALUE;
        return new FsStat(total, Long.MAX_VALUE, total - free, pathToInode.size());
    }

    @Override
    public Inode getRootInode() throws IOException {
        return toFileHandle(1); //always #1 (see constructor)
    }

    @Override
    public Inode lookup(Inode parent, String path) throws IOException {
        //TODO - several issues
        //2. we might accidentally allow composite paths here ("/dome/dir/down")
        //3. we dont actually check that the parent exists
        long parentInodeNumber = toInodeNumber(parent);
        String parentPath = resolveInode(parentInodeNumber);
        String child;
        if (path.equals(".")) {
            child = parentPath;
        } else if (path.equals("..")) {
            child = getParent(parentPath);
        } else {
            child = resolve(parentPath, path);
        }
        long childInodeNumber = resolveOrMapPath(child);
        return toFileHandle(childInodeNumber);
    }

    @Override
    public Inode link(Inode parent, Inode existing, String target, Subject subject) throws IOException {
        throw new NotSuppException("Not supported");
    }

    @Override
    public DirectoryStream list(Inode inode, byte[] bytes, long l) throws IOException {
        long inodeNumber = toInodeNumber(inode);
        String path = resolveInode(inodeNumber);
        if (path.charAt(path.length() - 1) != '/') {
            path = path + "/";
        }
        Collection<String> paths = storageAccess.list(path);
        if (paths == null) {
            throw new NoEntException("Directory does not exists: " + path);
        }
        final List<DirectoryEntry> list = new ArrayList<>(paths.size());
        int cookie = 2; // first allowed cookie
        for (String p : paths) {
            cookie++;
            if (cookie > l) {
                String absolutePath = path + p;
                long ino = resolveOrMapPath(absolutePath);
                list.add(new DirectoryEntry(p, toFileHandle(ino), statPath(absolutePath, ino), cookie));
            }
        }
        return new DirectoryStream(list);
    }

    @Override
    public byte[] directoryVerifier(Inode inode) throws IOException {
        return DirectoryStream.ZERO_VERIFIER;
    }

    @Override
    public Inode mkdir(Inode parent, String path, Subject subject, int mode) throws IOException {
        long parentInodeNumber = toInodeNumber(parent);
        String parentPath = resolveInode(parentInodeNumber);
        String newPath = resolve(parentPath, path);
        long newInodeNumber = fileId.getAndIncrement();
        map(newInodeNumber, newPath);
        return toFileHandle(newInodeNumber);
    }

    @Override
    public boolean move(Inode src, String oldName, Inode dest, String newName) throws IOException {
        //TODO - several issues
        //1. we might not deal with "." and ".." properly
        //2. we might accidentally allow composite paths here ("/dome/dir/down")
        //3. we return true (changed) even though in theory a file might be renamed to itself?
        long currentParentInodeNumber = toInodeNumber(src);
        String currentParentPath = resolveInode(currentParentInodeNumber);
        long destParentInodeNumber = toInodeNumber(dest);
        String destPath = resolveInode(destParentInodeNumber);
        String currentPath = resolve(currentParentPath, oldName);
        long targetInodeNumber = resolvePath(currentPath);
        String newPath = resolve(destPath, newName);
//        try {
            storageAccess.move(currentPath, newPath);
//        } catch (FileAlreadyExistsException e) {
//            throw new ExistException("path " + newPath);
//        }
        remap(targetInodeNumber, currentPath, newPath);
        return true;
    }

    @Override
    public Inode parentOf(Inode inode) throws IOException {
        long inodeNumber = toInodeNumber(inode);
        if (inodeNumber == 1) {
            throw new NoEntException("no parent"); //its the root
        }
        String path = resolveInode(inodeNumber);
        String parentPath = getParent(path);
        long parentInodeNumber = resolvePath(parentPath);
        return toFileHandle(parentInodeNumber);
    }

    @Override
    public int read(Inode inode, byte[] data, long offset, int count) throws IOException {
        long inodeNumber = toInodeNumber(inode);
        String path = resolveInode(inodeNumber);
        return storageAccess.read(path, data, offset, count);
    }

    @Override
    public String readlink(Inode inode) throws IOException {
        throw new NotSuppException("Not supported");
    }

    @Override
    public void remove(Inode parent, String path) throws IOException {
        long parentInodeNumber = toInodeNumber(parent);
        String parentPath = resolveInode(parentInodeNumber);
        String targetPath = resolve(parentPath, path);
        long targetInodeNumber = resolvePath(targetPath);
        storageAccess.remove(targetPath);
//        try {
//            Files.delete(targetPath);
//        } catch (DirectoryNotEmptyException e) {
//            throw new NotEmptyException("dir " + targetPath + " is note empty", e);
//        }
        unmap(targetInodeNumber, targetPath);
    }

    @Override
    public Inode symlink(Inode parent, String linkName, String targetName, Subject subject, int mode) throws IOException {
        throw new NotSuppException("Not supported");
    }

    @Override
    public WriteResult write(Inode inode, byte[] data, long offset, int count, StabilityLevel stabilityLevel) throws IOException {
        long inodeNumber = toInodeNumber(inode);
        String path = resolveInode(inodeNumber);
        int bytesWritten = storageAccess.write(path, data, offset, count);
        return new WriteResult(StabilityLevel.FILE_SYNC, bytesWritten);
    }

    @Override
    public void commit(Inode inode, long l, int i) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Stat statPath(String p, long inodeNumber) throws IOException {
        Stat stat = new Stat();

        stat.setMode(permissionsToMode(EnumSet.allOf(PosixFilePermission.class)));
        FileStats stats = null;
        if (p.charAt(p.length() - 1) == '/' || (stats = storageAccess.stat(p)) != null && stats.isDirectory()) {
            stat.setMode(stat.getMode() | Stat.Type.DIRECTORY.toMode());
        } else if (stats != null && stats.isFile()) {
            stat.setMode(stat.getMode() | Stat.Type.REGULAR.toMode());
        } else {
            throw new NoEntException("File does not exist: " + p);
        }

        stat.setATime(0L);
        stat.setCTime(0L);
        stat.setMTime(0L);

        stat.setGid(0);
        stat.setUid(0);

        stat.setNlink(1);
        stat.setDev(17);
        stat.setIno((int) inodeNumber);
        stat.setRdev(17);
        if (stats == null || stats.isDirectory()) {
            stat.setSize(10L);
        } else if (stats.getSize() > 0L) {
            stat.setSize(stats.getSize());
        } else {
            stat.setSize(10L);
        }
        stat.setFileid((int) inodeNumber);
        stat.setGeneration(0L);

        return stat;
    }

    @Override
    public int access(Inode inode, int mode) throws IOException {
        return mode;
    }

    @Override
    public Stat getattr(Inode inode) throws IOException {
        long inodeNumber = toInodeNumber(inode);
        String path = resolveInode(inodeNumber);
        return statPath(path, inodeNumber);
    }

    @Override
    public void setattr(Inode inode, Stat stat) throws IOException {
        long inodeNumber = toInodeNumber(inode);
        String path = resolveInode(inodeNumber);
//        PosixFileAttributeView attributeView = Files.getFileAttributeView(path, PosixFileAttributeView.class, NOFOLLOW_LINKS);
//        if (stat.isDefined(Stat.StatAttribute.OWNER)) {
//            try {
//                String uid = String.valueOf(stat.getUid());
//                UserPrincipal user = _lookupService.lookupPrincipalByName(uid);
//                attributeView.setOwner(user);
//            } catch (IOException e) {
//                throw new UnsupportedOperationException("set uid failed: " + e.getMessage(), e);
//            }
//        }
//        if (stat.isDefined(Stat.StatAttribute.GROUP)) {
//            try {
//                String gid = String.valueOf(stat.getGid());
//                GroupPrincipal group = _lookupService.lookupPrincipalByGroupName(gid);
//                attributeView.setGroup(group);
//            } catch (IOException e) {
//                throw new UnsupportedOperationException("set gid failed: " + e.getMessage(), e);
//            }
//        }
//        if (stat.isDefined(Stat.StatAttribute.MODE)) {
//            try {
//                Files.setAttribute(path, "posix:permissions", modeToPermissions(stat.getMode()), NOFOLLOW_LINKS);
//            } catch (IOException e) {
//                throw new UnsupportedOperationException("set mode unsupported: " + e.getMessage(), e);
//            }
//        }
//        if (stat.isDefined(Stat.StatAttribute.SIZE)) {
//            try ( RandomAccessFile raf = new RandomAccessFile(path.toFile(), "w")) {
//                raf.setLength(stat.getSize());
//            }
//        }
//        if (stat.isDefined(Stat.StatAttribute.ATIME)) {
//            try {
//                FileTime time = FileTime.fromMillis(stat.getCTime());
//                Files.setAttribute(path, "unix:lastAccessTime", time, NOFOLLOW_LINKS);
//            } catch (IOException e) {
//                throw new UnsupportedOperationException("set atime failed: " + e.getMessage(), e);
//            }
//        }
//        if (stat.isDefined(Stat.StatAttribute.MTIME)) {
//            try {
//                FileTime time = FileTime.fromMillis(stat.getMTime());
//                Files.setAttribute(path, "unix:lastModifiedTime", time, NOFOLLOW_LINKS);
//            } catch (IOException e) {
//                throw new UnsupportedOperationException("set mtime failed: " + e.getMessage(), e);
//            }
//        }
//        if (stat.isDefined(Stat.StatAttribute.CTIME)) {
//            try {
//                FileTime time = FileTime.fromMillis(stat.getCTime());
//                Files.setAttribute(path, "unix:ctime", time, NOFOLLOW_LINKS);
//            } catch (IOException e) {
//                throw new UnsupportedOperationException("set ctime failed: " + e.getMessage(), e);
//            }
//        }
    }

    @Override
    public nfsace4[] getAcl(Inode inode) throws IOException {
        return new nfsace4[0];
    }

    @Override
    public void setAcl(Inode inode, nfsace4[] acl) throws IOException {
        throw new UnsupportedOperationException("No ACL support");
    }

    @Override
    public boolean hasIOLayout(Inode inode) throws IOException {
        return false;
    }

    @Override
    public AclCheckable getAclCheckable() {
        return AclCheckable.UNDEFINED_ALL;
    }

    @Override
    public NfsIdMapping getIdMapper() {
        return _idMapper;
    }

    @Override
    public byte[] getXattr(Inode inode, String attr) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setXattr(Inode inode, String attr, byte[] value, SetXattrMode mode) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] listXattrs(Inode inode) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeXattr(Inode inode, String attr) throws IOException {
        throw new UnsupportedOperationException();
    }

    private static int permissionsToMode(Set<PosixFilePermission> permissions) {

        int mode = 0;
        for (PosixFilePermission p : permissions) {
            switch (p) {
                case OWNER_READ:
                    mode |= S_IRUSR;
                    break;
                case OWNER_WRITE:
                    mode |= S_IWUSR;
                    break;
                case OWNER_EXECUTE:
                    mode |= S_IXUSR;
                    break;
                case GROUP_READ:
                    mode |= S_IRGRP;
                    break;
                case GROUP_WRITE:
                    mode |= S_IWGRP;
                    break;
                case GROUP_EXECUTE:
                    mode |= S_IXGRP;
                    break;
                case OTHERS_READ:
                    mode |= S_IROTH;
                    break;
                case OTHERS_WRITE:
                    mode |= S_IWOTH;
                    break;
                case OTHERS_EXECUTE:
                    mode |= S_IXOTH;
                    break;
            }
        }

        return mode;
    }
}
