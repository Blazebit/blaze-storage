package com.blazebit.storage.rest.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.ext.MessageBodyReader;

import com.blazebit.storage.rest.model.convert.*;
import org.glassfish.jersey.client.proxy.WebResourceFactory;

import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.api.AdminResource;
import com.blazebit.storage.rest.api.BucketsResource;
import com.blazebit.storage.rest.api.BucketsSubResource;
import com.blazebit.storage.rest.api.StorageQuotaModelsResource;
import com.blazebit.storage.rest.api.StorageTypesResource;

public class BlazeStorageClient implements BlazeStorage, Serializable {

	private static final long serialVersionUID = 1L;
	private static final List<MessageBodyReader<?>> responseObjectMessageReader = Arrays.asList(
			(MessageBodyReader<?>) new BucketRepresentationMessageBodyReader(), (MessageBodyReader<?>) new BucketObjectRepresentationMessageBodyReader()
	);
	
	private final String serverUrl;
	private final ClientRequestFilter[] requestFilters;
    private final transient Client client;
    
	private BlazeStorageClient(String serverUrl, Client client, ClientRequestFilter[] requestFilters) {
		this.serverUrl = serverUrl;
		this.requestFilters = requestFilters;
		this.client = client;
	}
	
	private WebTarget initTarget() {
		WebTarget clientTarget = client.target(serverUrl);
		
		// Register the non-response object providers
		clientTarget.register(BucketObjectUpdateRepresentationMessageBodyReader.class);
		clientTarget.register(BucketObjectUpdateRepresentationClientMessageBodyWriter.class);
		clientTarget.register(MultipartUploadRepresentationMessageBodyReader.class);
		clientTarget.register(MultipartUploadRepresentationMessageBodyWriter.class);
        
        for (ClientRequestFilter filter : requestFilters) {
        	clientTarget = clientTarget.register(filter);
        }
        
        return new ResponseObjectWebTarget(clientTarget, responseObjectMessageReader);
	}

	public static BlazeStorage getInstance(String serverUrl) {
		return new BlazeStorageClient(serverUrl, newClient(), new ClientRequestFilter[0]);
	}

	public static BlazeStorage getInstance(String serverUrl, ClientRequestFilter... requestFilters) {
		return new BlazeStorageClient(serverUrl, newClient(), requestFilters);
	}

	public static BlazeStorage getInstance(String serverUrl, Client client, ClientRequestFilter... requestFilters) {
		return new BlazeStorageClient(serverUrl, client, requestFilters);
	}

	private static Client newClient() {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();

		// Older RestEasy implementations didn't use connection pooling which is important for storage connections
		try {
			Method connectionPoolSize = clientBuilder.getClass().getMethod("connectionPoolSize", int.class);
			connectionPoolSize.invoke(clientBuilder, 50);
		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (Exception e) {
			throw new RuntimeException("Could not create RestEasy client with connection pool size!", e);
		}
		return clientBuilder.build();
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
        set("client", newClient());
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
