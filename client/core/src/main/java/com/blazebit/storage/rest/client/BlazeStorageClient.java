package com.blazebit.storage.rest.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

import javax.ws.rs.client.ClientRequestFilter;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.api.BucketsResource;
import com.blazebit.storage.rest.api.BucketsSubResource;
import com.blazebit.storage.rest.api.StorageQuotaModelsResource;
import com.blazebit.storage.rest.api.StorageTypesResource;

public class BlazeStorageClient implements BlazeStorage, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String serverUrl;
	private final ClientRequestFilter[] requestFilters;
    private final transient ResteasyWebTarget target;
    private final transient ResteasyClient client;
    
	private BlazeStorageClient(String serverUrl, ClientRequestFilter[] requestFilters) {
		this.serverUrl = serverUrl;
		this.requestFilters = requestFilters;
		this.client = new ResteasyClientBuilder().build();
        this.target = initTarget();
	}
	
	private ResteasyWebTarget initTarget() {
		ResteasyWebTarget clientTarget = client.target(serverUrl);
        
        for (ClientRequestFilter filter : requestFilters) {
        	clientTarget = clientTarget.register(filter);
        }
        
        return clientTarget;
	}

	public static BlazeStorage getInstance(String serverUrl) {
		return new BlazeStorageClient(serverUrl, new ClientRequestFilter[0]);
	}

	public static BlazeStorage getInstance(String serverUrl, ClientRequestFilter... requestFilters) {
		return new BlazeStorageClient(serverUrl, requestFilters);
	}

	@Override
	public AccountsResource accounts() {
		return target.proxy(AccountsResource.class);
	}
	
	@Override
	public BucketsSubResource buckets() {
		return target.proxy(BucketsResource.class).get();
	}
	
	@Override
	public StorageQuotaModelsResource storageQuotaModels() {
		return target.proxy(StorageQuotaModelsResource.class);
	}
	
	@Override
	public StorageTypesResource storageTypes() {
		return target.proxy(StorageTypesResource.class);
	}

    @Override
	public void close() {
        client.close();
    }
    
    // SERIALIZATION
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        set("client", new ResteasyClientBuilder().build());
        set("target", initTarget());
    }
    
    private void set(String fieldName, Object value) {
    	try {
	    	Field field = BlazeStorageClient.class.getDeclaredField(fieldName);
	    	field.setAccessible(true);
	    	field.set(this, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}
