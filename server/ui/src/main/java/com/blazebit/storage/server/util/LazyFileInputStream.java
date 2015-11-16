package com.blazebit.storage.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class LazyFileInputStream extends LazyInputStream {

	private static final long serialVersionUID = 1L;
	
	private final Path file;

	public LazyFileInputStream(Path file) {
		this.file = file;
	}

	@Override
	protected InputStream createInputStream() throws IOException {
		return Files.newInputStream(file, StandardOpenOption.READ);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LazyFileInputStream other = (LazyFileInputStream) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LazyFileInputStream [file=" + file + "]";
	}
}
