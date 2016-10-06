package com.blazebit.storage.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public abstract class LazyInputStream extends InputStream implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private transient InputStream delegate;
	
	protected abstract InputStream createInputStream() throws IOException;
	
	private InputStream getDelegate() throws IOException {
		if (delegate == null) {
			delegate = createInputStream();
		}
		
		return delegate;
	}

	public int read() throws IOException {
		return getDelegate().read();
	}

	public int read(byte[] b) throws IOException {
		return getDelegate().read(b);
	}

	public int read(byte[] b, int off, int len) throws IOException {
		return getDelegate().read(b, off, len);
	}

	public long skip(long n) throws IOException {
		return getDelegate().skip(n);
	}

	public int available() throws IOException {
		return getDelegate().available();
	}

	public void close() throws IOException {
		getDelegate().close();
		delegate = null;
	}

	public abstract int hashCode();

	public abstract boolean equals(Object obj);

	public abstract String toString();
}
