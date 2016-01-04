package com.blazebit.storage.rest.model.convert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;

import com.blazebit.storage.rest.model.BlazeStorageHeaders;
import com.blazebit.storage.rest.model.BucketObjectBaseRepresentation;
import com.blazebit.storage.rest.model.BucketObjectHeadRepresentation;
import com.blazebit.storage.rest.model.BucketObjectRepresentation;
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;
import com.blazebit.storage.rest.model.rs.ContentDisposition;

public class BucketObjectRepresentationMessageBodyReader implements MessageBodyReader<BucketObjectBaseRepresentation> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return BucketObjectBaseRepresentation.class.isAssignableFrom(type);
	}

	@Override
	public BucketObjectBaseRepresentation readFrom(Class<BucketObjectBaseRepresentation> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		BucketObjectBaseRepresentation result;
		
		if (BucketObjectUpdateRepresentation.class.isAssignableFrom(type)) {
			BucketObjectUpdateRepresentation updateResult = new BucketObjectUpdateRepresentation();
			updateResult.setContentMD5(httpHeaders.getFirst(BlazeStorageHeaders.CONTENT_MD5));
			updateResult.setExternalContentKey(httpHeaders.getFirst(BlazeStorageHeaders.CONTENT_KEY));
			updateResult.setContent(entityStream);
			result = updateResult;
		} else {
			BucketObjectHeadRepresentation headResult;
			if (BucketObjectRepresentation.class.isAssignableFrom(type)) {
				BucketObjectRepresentation bucketObject = new BucketObjectRepresentation();
				bucketObject.setContent(entityStream);
				result = headResult = bucketObject;
			} else {
				result = headResult = new BucketObjectHeadRepresentation();
			}
			
			Date lastModified = Response.ok().header(HttpHeaders.LAST_MODIFIED, httpHeaders.getFirst(HttpHeaders.LAST_MODIFIED)).build().getLastModified();
			if (lastModified != null) {
				Calendar lastModifiedCalendar = Calendar.getInstance();
				lastModifiedCalendar.setTime(lastModified);
				headResult.setLastModified(lastModifiedCalendar);
			}
		}
		
		if (mediaType != null) {
			result.setContentType(mediaType.toString());
		} else {
			result.setContentType(httpHeaders.getFirst(HttpHeaders.CONTENT_TYPE));
		}
		
		result.setContentDisposition(ContentDisposition.fromString(httpHeaders.getFirst(HttpHeaders.CONTENT_DISPOSITION)));
		if (httpHeaders.getFirst(HttpHeaders.CONTENT_LENGTH) == null) {
			result.setSize(0L);
		} else {
			result.setSize(Long.valueOf(httpHeaders.getFirst(HttpHeaders.CONTENT_LENGTH)));
		}
		
		result.setStorageName(httpHeaders.getFirst(BlazeStorageHeaders.STORAGE_NAME));
		result.setStorageOwner(httpHeaders.getFirst(BlazeStorageHeaders.STORAGE_OWNER));
		
		Map<String, String> tags = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
			if (entry.getKey().startsWith(BlazeStorageHeaders.TAG_PREFIX)) {
				String tag = entry.getKey().substring(BlazeStorageHeaders.TAG_PREFIX.length());
				if (entry.getValue().isEmpty()) {
					tags.put(tag, "");
				} else {
					tags.put(tag, entry.getValue().get(0));
				}
			}
		}
		
		result.setTags(tags);
		
		return result;
	}

}
