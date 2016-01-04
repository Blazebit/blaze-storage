package com.blazebit.storage.rest.model.convert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import com.blazebit.storage.rest.model.BlazeStorageHeaders;
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;
import com.blazebit.storage.rest.model.MultipartUploadRepresentation;
import com.blazebit.storage.rest.model.multipart.InputPart;
import com.blazebit.storage.rest.model.multipart.MultipartInputImpl;

@Provider
@Consumes("multipart/*")
public class MultipartUploadRepresentationMessageBodyReader implements MessageBodyReader<MultipartUploadRepresentation> {

	@Context
	private HttpServletRequest request;
	@Context
	private Providers providers;
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return MultipartUploadRepresentation.class.isAssignableFrom(type);
	}

	@Override
	public MultipartUploadRepresentation readFrom(Class<MultipartUploadRepresentation> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		String boundary = mediaType.getParameters().get("boundary");
		if (boundary == null) {
			throw new IOException("Unable to get boundary");
		}
		
		MultipartInputImpl input = new MultipartInputImpl(mediaType, providers);
		input.parse(entityStream);
		Collection<InputPart> parts = input.getParts();
		
		Map<String, BucketObjectUpdateRepresentation> uploads = new HashMap<String, BucketObjectUpdateRepresentation>(parts.size());
		@SuppressWarnings("resource")
		MultipartUploadRepresentation result = new MultipartUploadRepresentation("true".equalsIgnoreCase(httpHeaders.getFirst(BlazeStorageHeaders.QUIET)), uploads, input);
		
		Class<BucketObjectUpdateRepresentation> readerType = BucketObjectUpdateRepresentation.class;
		Type readerGenericType = readerType;
		Annotation[] readerAnnotations = new Annotation[0];
		
		MessageBodyReader<BucketObjectUpdateRepresentation> reader = providers.getMessageBodyReader(readerType, readerGenericType, readerAnnotations, MediaType.WILDCARD_TYPE);
		
		for (InputPart p : parts) {
			MultivaluedMap<String, String> partHttpHeaders = p.getHeaders();
			String partKey = partHttpHeaders.getFirst(BlazeStorageHeaders.MULTIPART_KEY);
			
			if (partKey == null) {
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).build());
			}
			
			BucketObjectUpdateRepresentation bucketObjectUpdateRepresentation = reader.readFrom(readerType, readerGenericType, readerAnnotations, p.getMediaType(), partHttpHeaders, p.getInputStream());
			uploads.put(partKey, bucketObjectUpdateRepresentation);
		}

		return result;
	}

}
