package com.blazebit.storage.rest.model.convert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.blazebit.storage.rest.model.BlazeStorageHeaders;
import com.blazebit.storage.rest.model.BucketHeadRepresentation;
import com.blazebit.storage.rest.model.BucketObjectListElementRepresentation;
import com.blazebit.storage.rest.model.BucketRepresentation;
import com.blazebit.storage.rest.model.StatisticsRepresentation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Consumes(MediaType.WILDCARD)
public class BucketRepresentationMessageBodyReader implements MessageBodyReader<BucketHeadRepresentation> {

	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return BucketHeadRepresentation.class.isAssignableFrom(type);
	}

	@Override
	@SuppressWarnings("unchecked")
	public BucketHeadRepresentation readFrom(Class<BucketHeadRepresentation> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		BucketHeadRepresentation result;
		
		if (BucketRepresentation.class.isAssignableFrom(type)) {
			BucketRepresentation bucket = new BucketRepresentation();
			bucket.setContents((List<BucketObjectListElementRepresentation>) mapper.readValue(entityStream, new TypeReference<List<BucketObjectListElementRepresentation>>() {}));
			result = bucket;
		} else {
			result = new BucketHeadRepresentation();
		}
		
		result.setName(httpHeaders.getFirst(BlazeStorageHeaders.BUCKET_NAME));
		result.setDefaultStorageName(httpHeaders.getFirst(BlazeStorageHeaders.DEFAULT_STORAGE_NAME));
		result.setDefaultStorageOwner(httpHeaders.getFirst(BlazeStorageHeaders.DEFAULT_STORAGE_OWNER));
		Calendar creationDate = Calendar.getInstance();
		creationDate.setTimeInMillis(Long.valueOf(httpHeaders.getFirst(BlazeStorageHeaders.TIMESTAMP)));
		result.setCreationDate(creationDate);
		result.setNextMarker(httpHeaders.getFirst(BlazeStorageHeaders.NEXT_MARKER));
		
		StatisticsRepresentation statisticts = new StatisticsRepresentation();
		statisticts.setObjectBytes(Long.valueOf(httpHeaders.getFirst(BlazeStorageHeaders.OBJECT_BYTES)));
		statisticts.setObjectCount(Long.valueOf(httpHeaders.getFirst(BlazeStorageHeaders.OBJECT_COUNT)));
		result.setStatistics(statisticts);
		return result;
	}

}
