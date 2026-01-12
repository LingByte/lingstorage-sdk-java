package com.lingbyte.lingstorage;

/**
 * LingStorage 客户端配置
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class LingStorageConfig {
    private String baseUrl;
    private String apiKey;
    private String apiSecret;
    private long timeout = 30000; // 30 seconds
    private int retryCount = 3;
    private String userAgent = "LingStorage-Java-SDK/1.0.0";
    
    /**
     * 构造函数
     */
    public LingStorageConfig() {
    }
    
    /**
     * 构造函数
     * 
     * @param baseUrl 服务器地址
     * @param apiKey API 密钥
     * @param apiSecret API 密钥对应的 Secret
     */
    public LingStorageConfig(String baseUrl, String apiKey, String apiSecret) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }
    
    /**
     * Builder 模式构建器
     */
    public static class Builder {
        private LingStorageConfig config = new LingStorageConfig();
        
        public Builder baseUrl(String baseUrl) {
            config.baseUrl = baseUrl;
            return this;
        }
        
        public Builder apiKey(String apiKey) {
            config.apiKey = apiKey;
            return this;
        }
        
        public Builder apiSecret(String apiSecret) {
            config.apiSecret = apiSecret;
            return this;
        }
        
        public Builder timeout(long timeout) {
            config.timeout = timeout;
            return this;
        }
        
        public Builder retryCount(int retryCount) {
            config.retryCount = retryCount;
            return this;
        }
        
        public Builder userAgent(String userAgent) {
            config.userAgent = userAgent;
            return this;
        }
        
        public LingStorageConfig build() {
            if (config.baseUrl == null || config.baseUrl.isEmpty()) {
                throw new IllegalArgumentException("baseUrl is required");
            }
            if (config.apiKey == null || config.apiKey.isEmpty()) {
                throw new IllegalArgumentException("apiKey is required");
            }
            if (config.apiSecret == null || config.apiSecret.isEmpty()) {
                throw new IllegalArgumentException("apiSecret is required");
            }
            return config;
        }
    }
    
    // Getters and Setters
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getApiSecret() {
        return apiSecret;
    }
    
    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    
    public int getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}