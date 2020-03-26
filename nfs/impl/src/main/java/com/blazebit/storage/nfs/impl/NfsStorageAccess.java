package com.blazebit.storage.nfs.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.WhereBuilder;
import com.blazebit.persistence.view.CollectionMapping;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.ViewFilter;
import com.blazebit.persistence.view.ViewFilterProvider;
import com.blazebit.storage.core.api.BucketDataAccess;
import com.blazebit.storage.core.api.BucketObjectDataAccess;
import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.HexUtils;
import com.blazebit.storage.core.api.StorageDataAccess;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageResult;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectVersion;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.nfs.FileStats;
import com.blazebit.storage.nfs.StorageAccess;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

@ApplicationScoped
public class NfsStorageAccess implements StorageAccess {

    @Inject
    private BucketObjectDataAccess bucketObjectDataAccess;
    @Inject
    private BucketDataAccess bucketDataAccess;
    @Inject
    private BucketObjectService bucketObjectService;
    @Inject
    private StorageDataAccess storageDataAccess;

    @Override
    public int read(String key, byte[] data, long offset, int count) {
        EntityViewSetting<BucketObjectRepresentationView, CriteriaBuilder<BucketObjectRepresentationView>> setting;
        setting = EntityViewSetting.create(BucketObjectRepresentationView.class);
        BucketObjectRepresentationView bucketObject = bucketObjectDataAccess.findById(resolveId(key), setting);
        if (bucketObject == null) {
            throw new StorageException("Not found");
        }
        try (InputStream is = bucketObjectDataAccess.getContent(bucketObject.getStorageUri(), bucketObject.getContentKey())) {
            if (is.skip(offset) != offset) {
                throw new StorageException("Invalid offset");
            }
            return is.read(data, 0, count);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    private BucketObjectId resolveId(String key) {
        if (key == null || (key = key.trim()).isEmpty()) {
            throw new IllegalArgumentException("Illegal storage path");
        }
        int slashIdx = key.indexOf('/', 1);
        if (slashIdx == -1) {
            String bucketName;
            if (key.charAt(0) == '/') {
                bucketName = key.substring(1);
            } else {
                bucketName = key;
            }
            return new BucketObjectId(bucketName, "");
        }
        String bucketName;
        if (key.charAt(0) == '/') {
            bucketName = key.substring(1, slashIdx);
        } else {
            bucketName = key.substring(0, slashIdx);
        }
        String name;
        if (key.charAt(key.length() - 1) == '/' && slashIdx + 1 != key.length()) {
            name = key.substring(slashIdx + 1, key.length() - 1);
        } else {
            name = key.substring(slashIdx + 1);
        }
        return new BucketObjectId(bucketName, name);
    }

    @Override
    public Collection<String> list(String key) {
        if ("/".equals(key)) {
            List<BucketNameView> buckets = bucketDataAccess.findAll(EntityViewSetting.create(BucketNameView.class));
            List<String> names = new ArrayList<>(buckets.size());
            for (int i = 0; i < buckets.size(); i++) {
                names.add(buckets.get(i).getId());
            }
            return names;
        }
        BucketObjectId bucketObjectId = resolveId(key);
        int startIndex;
        String prefix = bucketObjectId.getName();
        if (prefix.isEmpty()) {
            startIndex = 1;
        } else {
            startIndex = prefix.length() + 2;
        }
        EntityViewSetting<BucketRepresentationView, CriteriaBuilder<BucketRepresentationView>> setting;
        setting = EntityViewSetting.create(BucketRepresentationView.class);
        setting.withOptionalParameter("startIndex", startIndex);
        setting.withViewFilter("deepFilter");
        BucketRepresentationView bucket = bucketDataAccess.findByName(bucketObjectId.getBucketId(), prefix, 1000, null, setting);
        if (bucket == null) {
            return null;
        }
        return bucket.getObjects();
    }

    @Override
    public void remove(String key) {
        BucketObjectId bucketObjectId = resolveId(key);
        bucketObjectService.delete(bucketObjectId);
    }

    @Override
    public int write(String key, byte[] data, long offset, int count) {
        if (offset != 0L) {
            throw new UnsupportedOperationException();
        }
        BucketObjectId bucketObjectId = resolveId(key);
        Storage storage = storageDataAccess.findByBucketId(bucketObjectId.getBucketId());
        StorageResult storageResult = bucketObjectService.createContent(storage.getUri(), new ByteArrayInputStream(data, 0, count));

        BucketObject bucketObject = new BucketObject(bucketObjectId);
        BucketObjectVersion version = new BucketObjectVersion();
        bucketObject.setContentVersion(version);

        version.setContentLength(storageResult.getSize());
        version.setContentMD5(HexUtils.bytesToHex(storageResult.getMd5Checksum()));
        // TODO: file name sniffing?
        version.setContentType("application/octet-stream");
        version.setContentKey(storageResult.getExternalKey());
        version.setEntityTag("");
        version.setStorage(storage);

        boolean success = false;
        try {
            bucketObjectService.put(bucketObject);
            success = true;
        } finally {
            if (!success) {// && !contentKey.equals(externalContentKey)) {
                // Delete the file
                bucketObjectService.deleteContent(storage.getUri(), storageResult.getExternalKey());
            }
        }
        return (int) storageResult.getSize();
    }

    @Override
    public void move(String oldKey, String newKey) {
        BucketObjectId bucketObjectId = resolveId(oldKey);
        BucketObject bucketObject = bucketObjectDataAccess.findById(resolveId(oldKey));
        bucketObject.setId(resolveId(newKey));
        bucketObject.setBucket(new Bucket(bucketObject.getId().getBucketId()));
        if (!bucketObjectId.getBucketId().equals(bucketObject.getId().getBucketId())) {
            throw new UnsupportedOperationException();
        }
        bucketObjectService.put(bucketObject);
        bucketObjectService.delete(bucketObjectId);
    }

    @Override
    public FileStats stat(String key) {
        BucketObjectId bucketObjectId = resolveId(key);
        boolean isFile;
        long size;
        if (bucketObjectId.getName().isEmpty()) {
            isFile = false;
            size = 0L;
        } else {
            EntityViewSetting<BucketStatsView, CriteriaBuilder<BucketStatsView>> setting;
            setting = EntityViewSetting.create(BucketStatsView.class);
            setting.withOptionalParameter("startIndex", bucketObjectId.getName().length() + 1);
            setting.withViewFilter("statsFilter");
            BucketStatsView bucketObjectStatsView = bucketDataAccess.findByName(bucketObjectId.getBucketId(), bucketObjectId.getName(), 2, null, setting);
            if (bucketObjectStatsView == null) {
                return null;
            }
            BucketObjectStatsView first = bucketObjectStatsView.getObjects().get(0);
            isFile = first.getName().isEmpty();
            size = first.getContentLength();
        }
        return new FileStats() {
            @Override
            public boolean isFile() {
                return isFile;
            }

            @Override
            public boolean isDirectory() {
                return !isFile;
            }

            @Override
            public long getSize() {
                return size;
            }
        };
    }

    @EntityView(Bucket.class)
    @ViewFilter(name = "deepFilter", value = BucketRepresentationView.DeepElementFilter.class)
    public interface BucketRepresentationView {

        @IdMapping
        String getId();

        @Mapping("SUBSTRING(objects.id.name, :startIndex, COALESCE(NULLIF(LOCATE('/', VIEW_ROOT(objects.id.name), :startIndex), 0), LENGTH(objects.id.name) + 1) - :startIndex)")
        public SortedSet<String> getObjects();

        public static class DeepElementFilter extends ViewFilterProvider {
            @Override
            public <T extends WhereBuilder<T>> T apply(T whereBuilder) {
                return whereBuilder.where("LENGTH(VIEW_ROOT(objects.id.name))").gtExpression(":startIndex");
            }
        }
    }

    @EntityView(BucketObject.class)
    public interface BucketObjectRepresentationView {

        @Mapping("contentVersion.storage.uri")
        public URI getStorageUri();

        @Mapping("contentVersion.contentKey")
        public String getContentKey();
    }

    @EntityView(Bucket.class)
    @ViewFilter(name = "statsFilter", value = BucketStatsView.StatsElementFilter.class)
    public interface BucketStatsView {

        @IdMapping
        String getId();

        @CollectionMapping(ignoreIndex = true)
        public List<BucketObjectStatsView> getObjects();

        public static class StatsElementFilter extends ViewFilterProvider {
            @Override
            public <T extends WhereBuilder<T>> T apply(T whereBuilder) {
                return whereBuilder.whereOr()
                    .where("SUBSTRING(VIEW_ROOT(objects.id.name), :startIndex, 1)").eqLiteral("/")
                    .where("LENGTH(VIEW_ROOT(objects.id.name)) + 1").eqExpression(":startIndex")
                    .endOr();
            }
        }

    }

    @EntityView(BucketObject.class)
    public interface BucketObjectStatsView {

        @Mapping("SUBSTRING(id.name, :startIndex)")
        public String getName();

        @Mapping("contentVersion.contentLength")
        public long getContentLength();
    }

    @EntityView(Bucket.class)
    public interface BucketNameView {

        public String getId();
    }
}

