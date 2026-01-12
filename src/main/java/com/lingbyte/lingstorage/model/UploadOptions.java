package com.lingbyte.lingstorage.model;

import java.util.List;

/**
 * 上传选项
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class UploadOptions {
    private String bucket;
    private String key;
    private List<String> allowedTypes;
    private boolean compress = false;
    private int quality = 0;
    private boolean watermark = false;
    private String watermarkText;
    private String watermarkPosition;
    private ProgressCallback onProgress;
    
    /**
     * 构造函数
     */
    public UploadOptions() {
    }
    
    /**
     * Builder 模式构建器
     */
    public static class Builder {
        private UploadOptions options = new UploadOptions();
        
        public Builder bucket(String bucket) {
            options.bucket = bucket;
            return this;
        }
        
        public Builder key(String key) {
            options.key = key;
            return this;
        }
        
        public Builder allowedTypes(List<String> allowedTypes) {
            options.allowedTypes = allowedTypes;
            return this;
        }
        
        public Builder compress(boolean compress) {
            options.compress = compress;
            return this;
        }
        
        public Builder quality(int quality) {
            options.quality = quality;
            return this;
        }
        
        public Builder watermark(boolean watermark) {
            options.watermark = watermark;
            return this;
        }
        
        public Builder watermarkText(String watermarkText) {
            options.watermarkText = watermarkText;
            return this;
        }
        
        public Builder watermarkPosition(String watermarkPosition) {
            options.watermarkPosition = watermarkPosition;
            return this;
        }
        
        public Builder onProgress(ProgressCallback onProgress) {
            options.onProgress = onProgress;
            return this;
        }
        
        public UploadOptions build() {
            return options;
        }
    }
    
    // Getters and Setters
    public String getBucket() {
        return bucket;
    }
    
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public List<String> getAllowedTypes() {
        return allowedTypes;
    }
    
    public void setAllowedTypes(List<String> allowedTypes) {
        this.allowedTypes = allowedTypes;
    }
    
    public boolean isCompress() {
        return compress;
    }
    
    public void setCompress(boolean compress) {
        this.compress = compress;
    }
    
    public int getQuality() {
        return quality;
    }
    
    public void setQuality(int quality) {
        this.quality = quality;
    }
    
    public boolean isWatermark() {
        return watermark;
    }
    
    public void setWatermark(boolean watermark) {
        this.watermark = watermark;
    }
    
    public String getWatermarkText() {
        return watermarkText;
    }
    
    public void setWatermarkText(String watermarkText) {
        this.watermarkText = watermarkText;
    }
    
    public String getWatermarkPosition() {
        return watermarkPosition;
    }
    
    public void setWatermarkPosition(String watermarkPosition) {
        this.watermarkPosition = watermarkPosition;
    }
    
    public ProgressCallback getOnProgress() {
        return onProgress;
    }
    
    public void setOnProgress(ProgressCallback onProgress) {
        this.onProgress = onProgress;
    }
}