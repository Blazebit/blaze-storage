package com.blazebit.storage.rest.model.convert;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import com.blazebit.storage.rest.model.BlazeStorageHeaders;
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;
import com.blazebit.storage.rest.model.MultipartUploadRepresentation;
import com.blazebit.storage.rest.model.multipart.HeaderFlushedOutputStream;

@Provider
@Consumes("multipart/*")
public class MultipartUploadRepresentationMessageBodyWriter implements MessageBodyWriter<MultipartUploadRepresentation> {

	@Context
	private HttpServletRequest request;
	@Context
	private Providers providers;
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return MultipartUploadRepresentation.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(MultipartUploadRepresentation t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(MultipartUploadRepresentation o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		Class<BucketObjectUpdateRepresentation> writerType = BucketObjectUpdateRepresentation.class;
		Type writerGenericType = writerType;
		Annotation[] writerAnnotations = new Annotation[0];
		
		MessageBodyWriter<BucketObjectUpdateRepresentation> writer = providers.getMessageBodyWriter(writerType, writerGenericType, writerAnnotations, MediaType.WILDCARD_TYPE);
		
		String boundary = UUID.randomUUID().toString();
		httpHeaders.putSingle(BlazeStorageHeaders.QUIET, Boolean.toString(o.isQuiet()));
		httpHeaders.putSingle(HttpHeaders.CONTENT_TYPE, mediaType.toString() + "; boundary=" + boundary);
		byte[] boundaryBytes = ("--" + boundary).getBytes();

		for (Map.Entry<String, BucketObjectUpdateRepresentation> entry : o.getUploads().entrySet()) {
			BucketObjectUpdateRepresentation value = entry.getValue();
			MediaType writerMediaType = MediaType.valueOf(value.getContentType());
			MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
			headers.add(BlazeStorageHeaders.MULTIPART_KEY, entry.getKey());
			
			entityStream.write(boundaryBytes);
			entityStream.write("\r\n".getBytes());
			writer.writeTo(entry.getValue(), writerType, writerGenericType, writerAnnotations, writerMediaType, headers, new HeaderFlushedOutputStream(headers, entityStream));
			entityStream.write("\r\n".getBytes());
		}

		entityStream.write(boundaryBytes);
		entityStream.write("--".getBytes());
	}
	
}
