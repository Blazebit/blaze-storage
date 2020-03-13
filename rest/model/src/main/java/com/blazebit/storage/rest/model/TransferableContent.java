package com.blazebit.storage.rest.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface TransferableContent {

	void transferTo(OutputStream os) throws IOException;

	default InputStream getInputStream() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		transferTo(baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}

	static TransferableContent forInputStream(InputStream is) {
		return new TransferableContent() {

			@Override
			public InputStream getInputStream() {
				return is;
			}

			@Override
			public void transferTo(OutputStream os) throws IOException {
				byte[] buffer = new byte[4096];

				int length;
				try (InputStream inputStream = getInputStream()) {
					while ((length = (inputStream.read(buffer))) >= 0) {
						os.write(buffer, 0, length);
					}
				}
			}
		};
	}
	
}
