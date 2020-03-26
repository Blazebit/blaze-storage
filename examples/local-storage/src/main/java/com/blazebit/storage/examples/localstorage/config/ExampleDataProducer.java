package com.blazebit.storage.examples.localstorage.config;

import com.blazebit.storage.core.api.AccountService;
import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.api.StorageQuotaModelService;
import com.blazebit.storage.core.api.StorageService;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectVersion;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlanId;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.net.URI;

@Singleton
@Startup
public class ExampleDataProducer extends Application {

    @Inject
    private AccountService accountService;
    @Inject
    private StorageService storageService;
    @Inject
    private StorageQuotaModelService quotaModelService;
    @Inject
    private BucketService bucketService;
    @Inject
    private BucketObjectService bucketObjectService;

    @PostConstruct
    void init() {
        StorageQuotaModel storageQuotaModel = new StorageQuotaModel("test");
        storageQuotaModel.setName("test");
        storageQuotaModel.setDescription("");
        StorageQuotaPlan quotaPlan = new StorageQuotaPlan(new StorageQuotaPlanId("test", 1));
        quotaPlan.setQuotaModel(storageQuotaModel);
        quotaPlan.setAlertPercent((short) 0);
        storageQuotaModel.getPlans().add(quotaPlan);
        quotaModelService.create(storageQuotaModel);

        Account account = new Account();
        account.setKey("test");
        account.setName("test");
        accountService.create(account);

        Storage storage = new Storage(new StorageId(account.getId(), "test"));
        storage.setOwner(account);
        storage.setQuotaPlan(storageQuotaModel.getPlans().iterator().next());
        storage.setUri(URI.create("file:///C:/test"));
        storageService.put(storage);

        Bucket bucket = new Bucket("test");
        bucket.setStorage(storage);
        bucket.setOwner(account);
        bucketService.put(bucket);

        BucketObject bucketObject = new BucketObject(new BucketObjectId("test", "test/test.txt"));
        bucketObject.setBucket(bucket);
        bucketObject.setContentVersion(new BucketObjectVersion());
        bucketObject.getContentVersion().setStorage(storage);
        bucketObject.getContentVersion().setContentKey("test/test.txt");
        bucketObject.getContentVersion().setContentLength(9);
        bucketObject.getContentVersion().setEntityTag("");
        bucketObject.getContentVersion().setBucketObject(bucketObject);
        bucketObjectService.put(bucketObject);
    }
}
