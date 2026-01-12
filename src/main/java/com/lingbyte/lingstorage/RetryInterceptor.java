package com.lingbyte.lingstorage;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 重试拦截器
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class RetryInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(RetryInterceptor.class);
    
    private final int maxRetries;
    
    public RetryInterceptor(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException lastException = null;
        
        for (int i = 0; i <= maxRetries; i++) {
            try {
                response = chain.proceed(request);
                
                // 如果响应成功或者是客户端错误（4xx），不重试
                if (response.isSuccessful() || (response.code() >= 400 && response.code() < 500)) {
                    return response;
                }
                
                // 服务器错误（5xx），关闭响应并准备重试
                if (response != null) {
                    response.close();
                }
                
            } catch (IOException e) {
                lastException = e;
                logger.warn("Request failed (attempt {}/{}): {}", i + 1, maxRetries + 1, e.getMessage());
            }
            
            // 如果不是最后一次尝试，等待一段时间后重试
            if (i < maxRetries) {
                try {
                    Thread.sleep((i + 1) * 1000); // 递增等待时间
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Request interrupted", e);
                }
            }
        }
        
        // 所有重试都失败了
        if (lastException != null) {
            throw lastException;
        }
        
        return response;
    }
}