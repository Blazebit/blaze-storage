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

import com.blazebit.storage.rest.model.BucketObjectRepresentation;
import com.blazebit.storage.rest.model.ContentDisposition;

@Provider
@Consumes(MediaType.WILDCARD)
public class BucketObjectRepresentationMessageBodyReader implements MessageBodyReader<BucketObjectRepresentation> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return BucketObjectRepresentation.class.equals(type);
	}

	@Override
	public BucketObjectRepresentation readFrom(Class<BucketObjectRepresentation> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		BucketObjectRepresentation result = new BucketObjectRepresentation();
		result.setContentType(mediaType.getType());
		result.setContentDisposition(ContentDisposition.fromString(httpHeaders.getFirst(HttpHeaders.CONTENT_DISPOSITION)));
		Date lastModified = Response.ok().header(HttpHeaders.LAST_MODIFIED, httpHeaders.getFirst(HttpHeaders.LAST_MODIFIED)).build().getLastModified();
		Calendar lastModifiedCalendar = Calendar.getInstance();
		lastModifiedCalendar.setTime(lastModified);
		result.setLastModified(lastModifiedCalendar);
		result.setSize(Long.valueOf(httpHeaders.getFirst(HttpHeaders.CONTENT_LENGTH)));
		result.setContent(entityStream);
		return result;
	}

}
