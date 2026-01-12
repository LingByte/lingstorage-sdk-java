package com.lingbyte.lingstorage.model;

/**
 * 上传错误
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class UploadError {
    private String file;
    private String error;
    
    /**
     * 构造函数
     */
    public UploadError() {
    }
    
    /**
     * 构造函数
     * 
     * @param file 文件路径
     * @param error 错误信息
     */
    public UploadError(String file, String error) {
        this.file = file;
        this.error = error;
    }
    
    // Getters and Setters
    public String getFile() {
        return file;
    }
    
    public void setFile(String file) {
        this.file = file;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    @Override
    public String toString() {
        return "UploadError{" +
                "file='" + file + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}