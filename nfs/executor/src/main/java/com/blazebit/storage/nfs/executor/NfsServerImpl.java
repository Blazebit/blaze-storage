package com.blazebit.storage.nfs.executor;

import com.blazebit.storage.nfs.StorageAccess;
import com.blazebit.storage.nfs.spi.NfsServer;
import org.dcache.nfs.ExportFile;
import org.dcache.nfs.v3.MountServer;
import org.dcache.nfs.v3.NfsServerV3;
import org.dcache.nfs.v4.MDSOperationExecutor;
import org.dcache.nfs.v4.NFS4Client;
import org.dcache.nfs.v4.NFSServerV41;
import org.dcache.nfs.v4.client.Main;
import org.dcache.nfs.vfs.VirtualFileSystem;
import org.dcache.oncrpc4j.portmap.OncRpcEmbeddedPortmap;
import org.dcache.oncrpc4j.rpc.OncRpcProgram;
import org.dcache.oncrpc4j.rpc.OncRpcSvc;
import org.dcache.oncrpc4j.rpc.OncRpcSvcBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NfsServerImpl implements NfsServer {

    private final OncRpcSvc nfsSvc;
    private final OncRpcEmbeddedPortmap portmap;

    public NfsServerImpl(StorageAccess storageAccess, ExecutorService executorService) throws IOException {
        this(new StorageAccessVirtualFileSystem(storageAccess), executorService);
    }

    public NfsServerImpl(VirtualFileSystem vfs, ExecutorService executorService) throws IOException {
        nfsSvc = new OncRpcSvcBuilder()
            .withPort(2049)
            .withTCP()
            .withAutoPublish()
            .withWorkerThreadIoStrategy()
            .withWorkerThreadExecutionService(executorService)
            .build();

        ExportFile exportFile = new ExportFile(new StringReader(
            "/ *(rw,all_squash,anonuid=0,anongid=0)"
//            + "\n/test *(rw,all_squash,anonuid=0,anongid=0)"
        ));

        NFSServerV41 nfs4 = new NFSServerV41.Builder()
            .withExportTable(exportFile)
            .withVfs(vfs)
            .withOperationExecutor(new MDSOperationExecutor())
            .build();

        NfsServerV3 nfs3 = new NfsServerV3(exportFile, vfs);
        MountServer mountd = new MountServer(exportFile, vfs);

        // register NFS servers at portmap service
        nfsSvc.register(new OncRpcProgram(100003, 4), nfs4);
        nfsSvc.register(new OncRpcProgram(100003, 3), nfs3);
        nfsSvc.register(new OncRpcProgram(100005, 3), mountd);
        portmap = new OncRpcEmbeddedPortmap();
    }

    public static void main(String[] args) throws Exception {
//        new NfsServerImpl(new NioVirtualFileSystem(Paths.get("C:\\test")), Executors.newCachedThreadPool()).start();
        Main.main(args);
    }

    @Override
    public void start() throws Exception {
        nfsSvc.start();
    }

    @Override
    public void stop() throws Exception {
        portmap.shutdown();
        nfsSvc.stop();
    }
}
