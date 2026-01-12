package com.lingbyte.lingstorage.model;

import java.util.List;

/**
 * 批量上传选项
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class BatchUploadOptions {
    private String bucket;
    private String keyPrefix;
    private List<String> allowedTypes;
    private boolean compress = false;
    private int quality = 0;
    private boolean watermark = false;
    private String watermarkText;
    private String watermarkPosition;
    private FileProgressCallback onFileProgress;
    private ProgressCallback onProgress;
    
    /**
     * 构造函数
     */
    public BatchUploadOptions() {
    }
    
    /**
     * Builder 模式构建器
     */
    public static class Builder {
        private BatchUploadOptions options = new BatchUploadOptions();
        
        public Builder bucket(String bucket) {
            options.bucket = bucket;
            return this;
        }
        
        public Builder keyPrefix(String keyPrefix) {
            options.keyPrefix = keyPrefix;
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
        
        public Builder onFileProgress(FileProgressCallback onFileProgress) {
            options.onFileProgress = onFileProgress;
            return this;
        }
        
        public Builder onProgress(ProgressCallback onProgress) {
            options.onProgress = onProgress;
            return this;
        }
        
        public BatchUploadOptions build() {
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
    
    public String getKeyPrefix() {
        return keyPrefix;
    }
    
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
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
    
    public FileProgressCallback getOnFileProgress() {
        return onFileProgress;
    }
    
    public void setOnFileProgress(FileProgressCallback onFileProgress) {
        this.onFileProgress = onFileProgress;
    }
    
    public ProgressCallback getOnProgress() {
        return onProgress;
    }
    
    public void setOnProgress(ProgressCallback onProgress) {
        this.onProgress = onProgress;
    }
}