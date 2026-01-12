package com.lingbyte.lingstorage.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 上传结果
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class UploadResult {
    private String key;
    private String bucket;
    private String filename;
    private long size;
    
    @JsonProperty("originalSize")
    private long originalSize;
    
    private boolean compressed;
    private boolean watermarked;
    private String url;
    
    /**
     * 构造函数
     */
    public UploadResult() {
    }
    
    // Getters and Setters
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getBucket() {
        return bucket;
    }
    
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public long getSize() {
        return size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
    
    public long getOriginalSize() {
        return originalSize;
    }
    
    public void setOriginalSize(long originalSize) {
        this.originalSize = originalSize;
    }
    
    public boolean isCompressed() {
        return compressed;
    }
    
    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }
    
    public boolean isWatermarked() {
        return watermarked;
    }
    
    public void setWatermarked(boolean watermarked) {
        this.watermarked = watermarked;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public String toString() {
        return "UploadResult{" +
                "key='" + key + '\'' +
                ", bucket='" + bucket + '\'' +
                ", filename='" + filename + '\'' +
                ", size=" + size +
                ", originalSize=" + originalSize +
                ", compressed=" + compressed +
                ", watermarked=" + watermarked +
                ", url='" + url + '\'' +
                '}';
    }
}