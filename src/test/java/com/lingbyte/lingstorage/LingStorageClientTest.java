package com.lingbyte.lingstorage;

import com.lingbyte.lingstorage.exception.LingStorageException;
import com.lingbyte.lingstorage.model.BatchUploadOptions;
import com.lingbyte.lingstorage.model.BatchUploadResult;
import com.lingbyte.lingstorage.model.UploadOptions;
import com.lingbyte.lingstorage.model.UploadResult;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LingStorageClient 测试类
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class LingStorageClientTest {
    
    private MockWebServer mockWebServer;
    private LingStorageClient client;
    
    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        
        String baseUrl = mockWebServer.url("/").toString();
        
        LingStorageConfig config = new LingStorageConfig.Builder()
                .baseUrl(baseUrl)
                .apiKey("test-key")
                .apiSecret("test-secret")
                .retryCount(0) // 禁用重试以便测试
                .build();
        
        client = new LingStorageClient(config);
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        client.close();
        mockWebServer.shutdown();
    }
    
    @Test
    public void testUploadBytesSuccess() throws LingStorageException {
        // 模拟成功响应
        String responseJson = "{\n" +
                "  \"code\": 200,\n" +
                "  \"msg\": \"Upload successful\",\n" +
                "  \"data\": {\n" +
                "    \"key\": \"test.txt\",\n" +
                "    \"bucket\": \"cetide\",\n" +
                "    \"filename\": \"test.txt\",\n" +
                "    \"size\": 11,\n" +
                "    \"originalSize\": 11,\n" +
                "    \"compressed\": false,\n" +
                "    \"watermarked\": false,\n" +
                "    \"url\": \"http://localhost:7075/uploads/test.txt\"\n" +
                "  }\n" +
                "}";
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
                .addHeader("Content-Type", "application/json"));
        
        // 测试上传
        byte[] data = "Hello World".getBytes();
        UploadOptions options = new UploadOptions.Builder()
                .bucket("cetide")
                .build();
        
        UploadResult result = client.uploadBytes(data, "test.txt", options);
        
        assertNotNull(result);
        assertEquals("test.txt", result.getKey());
        assertEquals("cetide", result.getBucket());
        assertEquals("test.txt", result.getFilename());
        assertEquals(11, result.getSize());
        assertEquals(11, result.getOriginalSize());
        assertFalse(result.isCompressed());
        assertFalse(result.isWatermarked());
        assertEquals("http://localhost:7075/uploads/test.txt", result.getUrl());
    }
    
    @Test
    public void testUploadBytesError() {
        // 模拟错误响应
        String errorJson = "{\n" +
                "  \"code\": 400,\n" +
                "  \"msg\": \"Invalid file type\"\n" +
                "}";
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(errorJson)
                .addHeader("Content-Type", "application/json"));
        
        // 测试上传错误
        byte[] data = "Hello World".getBytes();
        UploadOptions options = new UploadOptions.Builder()
                .bucket("cetide")
                .build();
        
        LingStorageException exception = assertThrows(LingStorageException.class, () -> {
            client.uploadBytes(data, "test.txt", options);
        });
        
        assertTrue(exception.getMessage().contains("Upload failed"));
    }
    
    @Test
    public void testUploadBytesWithProgress() throws LingStorageException {
        // 模拟成功响应
        String responseJson = "{\n" +
                "  \"code\": 200,\n" +
                "  \"msg\": \"Upload successful\",\n" +
                "  \"data\": {\n" +
                "    \"key\": \"test.txt\",\n" +
                "    \"bucket\": \"cetide\",\n" +
                "    \"filename\": \"test.txt\",\n" +
                "    \"size\": 11,\n" +
                "    \"originalSize\": 11,\n" +
                "    \"compressed\": false,\n" +
                "    \"watermarked\": false,\n" +
                "    \"url\": \"http://localhost:7075/uploads/test.txt\"\n" +
                "  }\n" +
                "}";
        
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
                .addHeader("Content-Type", "application/json"));
        
        // 测试带进度回调的上传
        final boolean[] progressCalled = {false};
        
        byte[] data = "Hello World".getBytes();
        UploadOptions options = new UploadOptions.Builder()
                .bucket("cetide")
                .onProgress((uploaded, total) -> {
                    progressCalled[0] = true;
                    assertTrue(uploaded >= 0);
                    assertTrue(total >= 0);
                })
                .build();
        
        UploadResult result = client.uploadBytes(data, "test.txt", options);
        
        assertNotNull(result);
        assertTrue(progressCalled[0], "Progress callback should be called");
    }
    
    @Test
    public void testBatchUploadEmptyList() {
        List<String> emptyFiles = new ArrayList<>();
        BatchUploadOptions options = new BatchUploadOptions.Builder()
                .bucket("cetide")
                .build();
        
        BatchUploadResult result = client.batchUpload(emptyFiles, options);
        
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getSuccess().isEmpty());
        assertTrue(result.getFailed().isEmpty());
    }
    
    @Test
    public void testConfigBuilder() {
        LingStorageConfig config = new LingStorageConfig.Builder()
                .baseUrl("https://example.com")
                .apiKey("key")
                .apiSecret("secret")
                .timeout(60000)
                .retryCount(5)
                .userAgent("Custom-Agent/1.0")
                .build();
        
        assertEquals("https://example.com", config.getBaseUrl());
        assertEquals("key", config.getApiKey());
        assertEquals("secret", config.getApiSecret());
        assertEquals(60000, config.getTimeout());
        assertEquals(5, config.getRetryCount());
        assertEquals("Custom-Agent/1.0", config.getUserAgent());
    }
    
    @Test
    public void testConfigBuilderValidation() {
        // 测试缺少必需参数的情况
        assertThrows(IllegalArgumentException.class, () -> {
            new LingStorageConfig.Builder()
                    .apiKey("key")
                    .apiSecret("secret")
                    .build(); // 缺少 baseUrl
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new LingStorageConfig.Builder()
                    .baseUrl("https://example.com")
                    .apiSecret("secret")
                    .build(); // 缺少 apiKey
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new LingStorageConfig.Builder()
                    .baseUrl("https://example.com")
                    .apiKey("key")
                    .build(); // 缺少 apiSecret
        });
    }
}