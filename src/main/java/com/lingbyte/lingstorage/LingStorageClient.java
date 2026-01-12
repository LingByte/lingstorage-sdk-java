package com.lingbyte.lingstorage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingbyte.lingstorage.exception.LingStorageException;
import com.lingbyte.lingstorage.model.*;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * LingStorage Java SDK 客户端
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class LingStorageClient {
    private static final Logger logger = LoggerFactory.getLogger(LingStorageClient.class);
    
    private final LingStorageConfig config;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    /**
     * 构造函数
     * 
     * @param config 客户端配置
     */
    public LingStorageClient(LingStorageConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
        
        // 构建 HTTP 客户端
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
        
        // 添加重试拦截器
        if (config.getRetryCount() > 0) {
            builder.addInterceptor(new RetryInterceptor(config.getRetryCount()));
        }
        
        this.httpClient = builder.build();
    }
    
    /**
     * 上传单个文件
     * 
     * @param filePath 文件路径
     * @param options 上传选项
     * @return 上传结果
     * @throws LingStorageException 上传异常
     */
    public UploadResult uploadFile(String filePath, UploadOptions options) throws LingStorageException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new LingStorageException("File not found: " + filePath);
        }
        
        return uploadFile(file, options);
    }
    
    /**
     * 上传单个文件
     * 
     * @param file 文件对象
     * @param options 上传选项
     * @return 上传结果
     * @throws LingStorageException 上传异常
     */
    public UploadResult uploadFile(File file, UploadOptions options) throws LingStorageException {
        if (options == null) {
            options = new UploadOptions();
        }
        
        // 构建请求体
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        
        // 添加文件
        RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
        
        // 如果有进度回调，包装请求体
        if (options.getOnProgress() != null) {
            fileBody = new ProgressRequestBody(fileBody, options.getOnProgress());
        }
        
        bodyBuilder.addFormDataPart("file", file.getName(), fileBody);
        
        // 添加其他参数
        addFormParameters(bodyBuilder, options);
        
        RequestBody requestBody = bodyBuilder.build();
        
        // 构建请求
        Request.Builder requestBuilder = new Request.Builder()
                .url(buildUploadUrl(options))
                .post(requestBody)
                .addHeader("User-Agent", config.getUserAgent())
                .addHeader("X-API-Key", config.getApiKey())
                .addHeader("X-API-Secret", config.getApiSecret());
        
        Request request = requestBuilder.build();
        
        // 发送请求
        try (Response response = httpClient.newCall(request).execute()) {
            return parseUploadResponse(response);
        } catch (IOException e) {
            throw new LingStorageException("Upload failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 上传字节数据
     * 
     * @param data 字节数据
     * @param filename 文件名
     * @param options 上传选项
     * @return 上传结果
     * @throws LingStorageException 上传异常
     */
    public UploadResult uploadBytes(byte[] data, String filename, UploadOptions options) throws LingStorageException {
        if (options == null) {
            options = new UploadOptions();
        }
        
        // 构建请求体
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        
        // 添加文件数据
        RequestBody fileBody = RequestBody.create(data, MediaType.parse("application/octet-stream"));
        
        // 如果有进度回调，包装请求体
        if (options.getOnProgress() != null) {
            fileBody = new ProgressRequestBody(fileBody, options.getOnProgress());
        }
        
        bodyBuilder.addFormDataPart("file", filename, fileBody);
        
        // 添加其他参数
        addFormParameters(bodyBuilder, options);
        
        RequestBody requestBody = bodyBuilder.build();
        
        // 构建请求
        Request.Builder requestBuilder = new Request.Builder()
                .url(buildUploadUrl(options))
                .post(requestBody)
                .addHeader("User-Agent", config.getUserAgent())
                .addHeader("X-API-Key", config.getApiKey())
                .addHeader("X-API-Secret", config.getApiSecret());
        
        Request request = requestBuilder.build();
        
        // 发送请求
        try (Response response = httpClient.newCall(request).execute()) {
            return parseUploadResponse(response);
        } catch (IOException e) {
            throw new LingStorageException("Upload failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 批量上传文件
     * 
     * @param filePaths 文件路径列表
     * @param options 批量上传选项
     * @return 批量上传结果
     */
    public BatchUploadResult batchUpload(List<String> filePaths, BatchUploadOptions options) {
        if (options == null) {
            options = new BatchUploadOptions();
        }
        
        BatchUploadResult result = new BatchUploadResult();
        result.setTotal(filePaths.size());
        result.setSuccess(new ArrayList<>());
        result.setFailed(new ArrayList<>());
        
        for (int i = 0; i < filePaths.size(); i++) {
            String filePath = filePaths.get(i);
            
            // 调用进度回调
            if (options.getOnFileProgress() != null) {
                options.getOnFileProgress().onProgress(i, filePaths.size(), filePath);
            }
            
            try {
                // 构建单个文件上传选项
                UploadOptions uploadOptions = new UploadOptions();
                uploadOptions.setBucket(options.getBucket());
                uploadOptions.setAllowedTypes(options.getAllowedTypes());
                uploadOptions.setCompress(options.isCompress());
                uploadOptions.setQuality(options.getQuality());
                uploadOptions.setWatermark(options.isWatermark());
                uploadOptions.setWatermarkText(options.getWatermarkText());
                uploadOptions.setWatermarkPosition(options.getWatermarkPosition());
                uploadOptions.setOnProgress(options.getOnProgress());
                
                // 设置键名
                if (options.getKeyPrefix() != null && !options.getKeyPrefix().isEmpty()) {
                    File file = new File(filePath);
                    uploadOptions.setKey(options.getKeyPrefix() + "/" + file.getName());
                }
                
                // 上传文件
                UploadResult uploadResult = uploadFile(filePath, uploadOptions);
                result.getSuccess().add(uploadResult);
                
            } catch (Exception e) {
                UploadError error = new UploadError();
                error.setFile(filePath);
                error.setError(e.getMessage());
                result.getFailed().add(error);
            }
        }
        
        // 最终进度回调
        if (options.getOnFileProgress() != null) {
            options.getOnFileProgress().onProgress(filePaths.size(), filePaths.size(), "");
        }
        
        return result;
    }
    
    /**
     * 添加表单参数
     */
    private void addFormParameters(MultipartBody.Builder builder, UploadOptions options) {
        if (options.getBucket() != null) {
            builder.addFormDataPart("bucket", options.getBucket());
        }
        if (options.getKey() != null) {
            builder.addFormDataPart("key", options.getKey());
        }
        if (options.isCompress()) {
            builder.addFormDataPart("compress", "true");
            if (options.getQuality() > 0) {
                builder.addFormDataPart("quality", String.valueOf(options.getQuality()));
            }
        }
        if (options.isWatermark()) {
            builder.addFormDataPart("watermark", "true");
            if (options.getWatermarkText() != null) {
                builder.addFormDataPart("watermarkText", options.getWatermarkText());
            }
            if (options.getWatermarkPosition() != null) {
                builder.addFormDataPart("watermarkPosition", options.getWatermarkPosition());
            }
        }
    }
    
    /**
     * 构建上传 URL
     */
    private String buildUploadUrl(UploadOptions options) {
        String baseUrl = config.getBaseUrl().replaceAll("/$", "");
        String url = baseUrl + "/api/public/upload";
        
        // 添加允许的文件类型查询参数
        if (options.getAllowedTypes() != null && !options.getAllowedTypes().isEmpty()) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            for (String type : options.getAllowedTypes()) {
                urlBuilder.addQueryParameter("allowedTypes", type);
            }
            url = urlBuilder.build().toString();
        }
        
        return url;
    }
    
    /**
     * 解析上传响应
     */
    private UploadResult parseUploadResponse(Response response) throws LingStorageException, IOException {
        String responseBody = response.body().string();
        
        if (!response.isSuccessful()) {
            throw new LingStorageException("Upload failed with status: " + response.code() + ", body: " + responseBody);
        }
        
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            
            // 支持两种响应格式
            boolean isSuccess = false;
            JsonNode dataNode = null;
            String message = "";
            
            if (jsonNode.has("success")) {
                // 标准格式
                isSuccess = jsonNode.get("success").asBoolean();
                message = jsonNode.has("message") ? jsonNode.get("message").asText() : "";
                dataNode = jsonNode.get("data");
            } else if (jsonNode.has("code")) {
                // LingStorage 服务器格式
                int code = jsonNode.get("code").asInt();
                isSuccess = code == 200;
                message = jsonNode.has("msg") ? jsonNode.get("msg").asText() : "";
                dataNode = jsonNode.get("data");
            }
            
            if (!isSuccess) {
                throw new LingStorageException("Upload failed: " + message);
            }
            
            return objectMapper.treeToValue(dataNode, UploadResult.class);
            
        } catch (Exception e) {
            throw new LingStorageException("Failed to parse response: " + e.getMessage(), e);
        }
    }
    
    /**
     * 关闭客户端
     */
    public void close() {
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
            httpClient.connectionPool().evictAll();
        }
    }
}