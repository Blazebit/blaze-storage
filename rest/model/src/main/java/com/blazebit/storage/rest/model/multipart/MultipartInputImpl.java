package com.blazebit.storage.rest.model.multipart;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Providers;

import org.apache.james.mime4j.field.ContentTypeField;
import org.apache.james.mime4j.message.BinaryBody;
import org.apache.james.mime4j.message.Body;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.message.TextBody;
import org.apache.james.mime4j.parser.Field;

public class MultipartInputImpl implements Closeable {

	protected MediaType contentType;
	protected Providers workers;
	protected Message mimeMessage;
	protected List<InputPart> parts = new ArrayList<InputPart>();
	protected static final Annotation[] empty = {};
	protected MediaType defaultPartContentType = MediaType.valueOf("text/plain; charset=us-ascii");
	protected String defaultPartCharset = null;
	protected Providers savedProviders;

	public MultipartInputImpl(MediaType contentType, Providers workers) {
		this.contentType = contentType;
		this.workers = workers;
	}

	public void parse(InputStream is) throws IOException {
		mimeMessage = new BinaryMessage(addHeaderToHeadlessStream(is));
		extractParts();
	}

	protected InputStream addHeaderToHeadlessStream(InputStream is) throws UnsupportedEncodingException {
		return new SequenceInputStream(createHeaderInputStream(), is);
	}

	protected InputStream createHeaderInputStream() throws UnsupportedEncodingException {
		String header = HttpHeaders.CONTENT_TYPE + ": " + contentType + "\r\n\r\n";
		return new ByteArrayInputStream(header.getBytes("utf-8"));
	}

	public String getPreamble() {
		return ((Multipart) mimeMessage.getBody()).getPreamble();
	}

	public List<InputPart> getParts() {
		return parts;
	}

	protected void extractParts() throws IOException {
		Multipart multipart = (Multipart) mimeMessage.getBody();
		for (BodyPart bodyPart : multipart.getBodyParts()) {
			parts.add(extractPart(bodyPart));
		}
	}

	protected InputPart extractPart(BodyPart bodyPart) throws IOException {
		return new PartImpl(bodyPart);
	}

	public class PartImpl implements InputPart {

		private BodyPart bodyPart;
		private MediaType contentType;
		private MultivaluedMap<String, String> headers = new CaseInsensitiveMultivaluedMap<String>();

		public PartImpl(BodyPart bodyPart) {
			this.bodyPart = bodyPart;
			for (Field field : bodyPart.getHeader()) {
				headers.add(field.getName(), field.getBody());
				if (field instanceof ContentTypeField) {
					contentType = MediaType.valueOf(field.getBody());
				}
			}
			if (contentType == null)
				contentType = defaultPartContentType;
			if (getCharset(contentType) == null) {
				if (defaultPartCharset != null) {
					contentType = getMediaTypeWithDefaultCharset(contentType);
				} else if (contentType.getType().equalsIgnoreCase("text")) {
					contentType = getMediaTypeWithCharset(contentType, "us-ascii");
				}
			}
		}

		@Override
		public InputStream getInputStream() throws IOException {
			Body body = bodyPart.getBody();
			InputStream result = null;
			if (body instanceof TextBody) {
				throw new UnsupportedOperationException();
				/*
				 * InputStreamReader reader = (InputStreamReader)((TextBody)
				 * body).getReader(); StringBuilder inputBuilder = new
				 * StringBuilder(); char[] buffer = new char[1024]; while (true)
				 * { int readCount = reader.read(buffer); if (readCount < 0) {
				 * break; } inputBuilder.append(buffer, 0, readCount); } String
				 * str = inputBuilder.toString(); return new
				 * ByteArrayInputStream(str.getBytes(reader.getEncoding()));
				 */
			} else if (body instanceof BinaryBody) {
				return ((BinaryBody) body).getInputStream();
			}
			return result;
		}

		@Override
		public MediaType getMediaType() {
			return contentType;
		}
		
		@Override
		public void delete() throws IOException {
			bodyPart.dispose();
		}

		@Override
		public MultivaluedMap<String, String> getHeaders() {
			return headers;
		}
	}

	@Override
	public void close() {
		if (mimeMessage != null) {
			try {
				mimeMessage.dispose();
			} catch (Exception e) {

			}
		}
	}

	protected void finalize() throws Throwable {
		close();
	}

	protected String getCharset(MediaType mediaType) {
		for (Iterator<String> it = mediaType.getParameters().keySet().iterator(); it.hasNext();) {
			String key = it.next();
			if ("charset".equalsIgnoreCase(key)) {
				return mediaType.getParameters().get(key);
			}
		}
		return null;
	}

	private MediaType getMediaTypeWithDefaultCharset(MediaType mediaType) {
		String charset = defaultPartCharset;
		return getMediaTypeWithCharset(mediaType, charset);
	}

	private MediaType getMediaTypeWithCharset(MediaType mediaType, String charset) {
		Map<String, String> params = mediaType.getParameters();
		Map<String, String> newParams = new HashMap<String, String>();
		newParams.put("charset", charset);
		for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			if (!"charset".equalsIgnoreCase(key)) {
				newParams.put(key, params.get(key));
			}
		}
		return new MediaType(mediaType.getType(), mediaType.getSubtype(), newParams);
	}

	public void setProviders(Providers providers) {
		savedProviders = providers;
	}
}
