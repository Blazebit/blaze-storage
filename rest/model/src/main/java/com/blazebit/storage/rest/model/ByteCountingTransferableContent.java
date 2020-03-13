package com.blazebit.storage.rest.model;

import java.io.IOException;
import java.io.OutputStream;

public class ByteCountingTransferableContent implements TransferableContent {

    private final TransferableContent transferableContent;
    private long byteCount;

    public ByteCountingTransferableContent(TransferableContent transferableContent) {
        this.transferableContent = transferableContent;
    }

    @Override
    public void transferTo(OutputStream os) throws IOException {
        transferableContent.transferTo(new ByteCountingOutputStream(os));
    }

    public long getByteCount() {
        return byteCount;
    }

    private class ByteCountingOutputStream extends OutputStream {

        private final OutputStream os;

        public ByteCountingOutputStream(OutputStream os) {
            this.os = os;
        }

        @Override
        public void write(int b) throws IOException {
            os.write(b);
            byteCount++;
        }

        @Override
        public void write(byte[] b) throws IOException {
            os.write(b);
            byteCount += b.length;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            os.write(b, off, len);
            byteCount += len;
        }

        @Override
        public void flush() throws IOException {
            os.flush();
        }

        @Override
        public void close() throws IOException {
            os.close();
        }
    }
}