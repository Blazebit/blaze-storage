package com.blazebit.storage.rest.model.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Stack;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.Base64InputStream;
import org.apache.james.mime4j.codec.QuotedPrintableInputStream;
import org.apache.james.mime4j.descriptor.BodyDescriptor;
import org.apache.james.mime4j.message.Body;
import org.apache.james.mime4j.message.BodyFactory;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.MessageBuilder;
import org.apache.james.mime4j.storage.StorageProvider;
import org.apache.james.mime4j.util.MimeUtil;

/**
 * Originally from org.jboss.resteasy.plugins.providers.multipart.MultipartInputImpl.BinaryOnlyMessageBuilder
 * 
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 */
// We hack MIME4j so that it always returns a BinaryBody so we don't have to deal with Readers and their charset conversions
public class BinaryOnlyMessageBuilder extends MessageBuilder {
	private Method expectMethod;
	private java.lang.reflect.Field bodyFactoryField;
	private java.lang.reflect.Field stackField;

	private void init() {
		try {
			expectMethod = MessageBuilder.class.getDeclaredMethod("expect", Class.class);
			expectMethod.setAccessible(true);
			bodyFactoryField = MessageBuilder.class.getDeclaredField("bodyFactory");
			bodyFactoryField.setAccessible(true);
			stackField = MessageBuilder.class.getDeclaredField("stack");
			stackField.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	BinaryOnlyMessageBuilder(Entity entity) {
		super(entity);
		init();
	}

	BinaryOnlyMessageBuilder(Entity entity, StorageProvider storageProvider) {
		super(entity, storageProvider);
		init();
	}

	@Override
	public void body(BodyDescriptor bd, InputStream is) throws MimeException, IOException {
		// the only thing different from the superclass is that we just return a
		// BinaryBody no matter what
		try {
			expectMethod.invoke(this, Entity.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		final String enc = bd.getTransferEncoding();

		final Body body;

		final InputStream decodedStream;
		if (MimeUtil.ENC_BASE64.equals(enc)) {
			decodedStream = new Base64InputStream(is);
		} else if (MimeUtil.ENC_QUOTED_PRINTABLE.equals(enc)) {
			decodedStream = new QuotedPrintableInputStream(is);
		} else {
			decodedStream = is;
		}

		try {
			BodyFactory factory = (BodyFactory) bodyFactoryField.get(this);
			body = factory.binaryBody(decodedStream);

			Stack<Object> st = (Stack<Object>) stackField.get(this);
			Entity entity = ((Entity) st.peek());
			entity.setBody(body);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}