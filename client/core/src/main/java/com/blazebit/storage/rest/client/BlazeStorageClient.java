package com.blazebit.storage.rest.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.ext.MessageBodyReader;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.api.AdminResource;
import com.blazebit.storage.rest.api.BucketsResource;
import com.blazebit.storage.rest.api.BucketsSubResource;
import com.blazebit.storage.rest.api.StorageQuotaModelsResource;
import com.blazebit.storage.rest.api.StorageTypesResource;
import com.blazebit.storage.rest.model.convert.BucketObjectRepresentationMessageBodyReader;
import com.blazebit.storage.rest.model.convert.BucketObjectUpdateRepresentationMessageBodyReader;
import com.blazebit.storage.rest.model.convert.BucketObjectUpdateRepresentationMessageBodyWriter;
import com.blazebit.storage.rest.model.convert.BucketRepresentationMessageBodyReader;
import com.blazebit.storage.rest.model.convert.MultipartUploadRepresentationMessageBodyReader;
import com.blazebit.storage.rest.model.convert.MultipartUploadRepresentationMessageBodyWriter;

public class BlazeStorageClient implements BlazeStorage, Serializable {

	private static final long serialVersionUID = 1L;
	private static final List<MessageBodyReader<?>> responseObjectMessageReader = Arrays.asList(
			(MessageBodyReader<?>) new BucketRepresentationMessageBodyReader(), (MessageBodyReader<?>) new BucketObjectRepresentationMessageBodyReader()
	);
	
	private final String serverUrl;
	private final ClientRequestFilter[] requestFilters;
    private final transient Client client;
    
	private BlazeStorageClient(String serverUrl, ClientRequestFilter[] requestFilters) {
		this.serverUrl = serverUrl;
		this.requestFilters = requestFilters;
		
		this.client = ClientBuilder.newClient();
	}
	
	private WebTarget initTarget() {
		WebTarget clientTarget = client.target(serverUrl);
		
		// Register the non-response object providers
		clientTarget.register(BucketObjectUpdateRepresentationMessageBodyReader.class);
		clientTarget.register(BucketObjectUpdateRepresentationMessageBodyWriter.class);
		clientTarget.register(MultipartUploadRepresentationMessageBodyReader.class);
		clientTarget.register(MultipartUploadRepresentationMessageBodyWriter.class);
        
        for (ClientRequestFilter filter : requestFilters) {
        	clientTarget = clientTarget.register(filter);
        }
        
        return new ResponseObjectWebTarget(clientTarget, responseObjectMessageReader);
	}

	public static BlazeStorage getInstance(String serverUrl) {
		return new BlazeStorageClient(serverUrl, new ClientRequestFilter[0]);
	}

	public static BlazeStorage getInstance(String serverUrl, ClientRequestFilter... requestFilters) {
		return new BlazeStorageClient(serverUrl, requestFilters);
	}

	@Override
	public AdminResource admin() {
		return WebResourceFactory.newResource(AdminResource.class, initTarget());
	}

	@Override
	public AccountsResource accounts() {
		return WebResourceFactory.newResource(AccountsResource.class, initTarget());
	}
	
	@Override
	public BucketsSubResource buckets() {
		return WebResourceFactory.newResource(BucketsResource.class, initTarget()).get();
	}
	
	@Override
	public StorageQuotaModelsResource storageQuotaModels() {
		return WebResourceFactory.newResource(StorageQuotaModelsResource.class, initTarget());
	}
	
	@Override
	public StorageTypesResource storageTypes() {
		return WebResourceFactory.newResource(StorageTypesResource.class, initTarget());
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
        set("client", ClientBuilder.newClient());
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
