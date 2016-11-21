package com.blazebit.storage.rest.client;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Moritz Becker <m.becker@curecomp.com>
 * @company curecomp
 * @date 21.11.2016
 */
public class ResponseInputStreamWrapper extends InputStream {

    private final InputStream inputStream;
    private final Response response;

    public ResponseInputStreamWrapper(InputStream inputStream, Response response) {
        this.inputStream = inputStream;
        this.response = response;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return inputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return inputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        response.close();
    }

    @Override
    public void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        inputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }
}
