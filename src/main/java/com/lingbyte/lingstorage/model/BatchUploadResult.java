package com.lingbyte.lingstorage.model;

import java.util.List;

/**
 * 批量上传结果
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class BatchUploadResult {
    private List<UploadResult> success;
    private List<UploadError> failed;
    private int total;
    
    /**
     * 构造函数
     */
    public BatchUploadResult() {
    }
    
    // Getters and Setters
    public List<UploadResult> getSuccess() {
        return success;
    }
    
    public void setSuccess(List<UploadResult> success) {
        this.success = success;
    }
    
    public List<UploadError> getFailed() {
        return failed;
    }
    
    public void setFailed(List<UploadError> failed) {
        this.failed = failed;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    
    @Override
    public String toString() {
        return "BatchUploadResult{" +
                "success=" + (success != null ? success.size() : 0) +
                ", failed=" + (failed != null ? failed.size() : 0) +
                ", total=" + total +
                '}';
    }
}