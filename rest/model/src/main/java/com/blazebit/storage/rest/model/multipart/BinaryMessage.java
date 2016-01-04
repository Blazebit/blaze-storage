package com.blazebit.storage.rest.model.multipart;

import java.io.IOException;
import java.io.InputStream;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.storage.DefaultStorageProvider;

/**
 * Originally from org.jboss.resteasy.plugins.providers.multipart.MultipartInputImpl.BinaryOnlyMessageBuilder
 * 
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 */
public class BinaryMessage extends Message {
	BinaryMessage(InputStream is) throws IOException, MimeIOException {
		try {
			MimeStreamParser parser = new MimeStreamParser(null);
			parser.setContentHandler(new BinaryOnlyMessageBuilder(this, DefaultStorageProvider.getInstance()));
			parser.parse(is);
		} catch (MimeException e) {
			throw new MimeIOException(e);
		}

	}
}