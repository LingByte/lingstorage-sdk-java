package com.lingbyte.lingstorage;

import com.lingbyte.lingstorage.model.ProgressCallback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

import java.io.IOException;

/**
 * 带进度回调的请求体
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class ProgressRequestBody extends RequestBody {
    private final RequestBody requestBody;
    private final ProgressCallback callback;
    
    public ProgressRequestBody(RequestBody requestBody, ProgressCallback callback) {
        this.requestBody = requestBody;
        this.callback = callback;
    }
    
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }
    
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }
    
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        BufferedSink progressSink = Okio.buffer(new ProgressSink(sink));
        requestBody.writeTo(progressSink);
        progressSink.flush();
    }
    
    private class ProgressSink extends ForwardingSink {
        private long bytesWritten = 0L;
        private long contentLength = 0L;
        
        public ProgressSink(Sink delegate) {
            super(delegate);
            try {
                contentLength = contentLength();
            } catch (IOException e) {
                contentLength = -1L;
            }
        }
        
        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            
            bytesWritten += byteCount;
            if (callback != null) {
                callback.onProgress(bytesWritten, contentLength);
            }
        }
    }
}