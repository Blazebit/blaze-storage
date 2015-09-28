package com.blazebit.storage.rest.model.convert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.blazebit.storage.rest.model.BlazeStorageHeaders;
import com.blazebit.storage.rest.model.BucketObjectHeadRepresentation;
import com.blazebit.storage.rest.model.BucketObjectRepresentation;
import com.blazebit.storage.rest.model.rs.ContentDisposition;

@Provider
@Consumes(MediaType.WILDCARD)
public class BucketObjectRepresentationMessageBodyReader implements MessageBodyReader<BucketObjectHeadRepresentation> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return BucketObjectHeadRepresentation.class.equals(type);
	}

	@Override
	public BucketObjectHeadRepresentation readFrom(Class<BucketObjectHeadRepresentation> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		BucketObjectHeadRepresentation result;
		
		if (BucketObjectRepresentation.class.isAssignableFrom(type)) {
			BucketObjectRepresentation bucketObject = new BucketObjectRepresentation();
			bucketObject.setContent(entityStream);
			result = bucketObject;
		} else {
			result = new BucketObjectHeadRepresentation();
		}
		
		result.setContentType(mediaType.getType());
		result.setContentDisposition(ContentDisposition.fromString(httpHeaders.getFirst(HttpHeaders.CONTENT_DISPOSITION)));
		Date lastModified = Response.ok().header(HttpHeaders.LAST_MODIFIED, httpHeaders.getFirst(HttpHeaders.LAST_MODIFIED)).build().getLastModified();
		Calendar lastModifiedCalendar = Calendar.getInstance();
		lastModifiedCalendar.setTime(lastModified);
		result.setLastModified(lastModifiedCalendar);
		result.setSize(Long.valueOf(httpHeaders.getFirst(HttpHeaders.CONTENT_LENGTH)));
		result.setStorageName(httpHeaders.getFirst(BlazeStorageHeaders.STORAGE_NAME));
		result.setStorageOwner(httpHeaders.getFirst(BlazeStorageHeaders.STORAGE_OWNER));
		
		// TODO: headers?
//		result.setTags(httpHeaders);
		
		
		return result;
	}

}
