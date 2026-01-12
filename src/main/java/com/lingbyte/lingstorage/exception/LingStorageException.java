package com.lingbyte.lingstorage.exception;

/**
 * LingStorage 异常类
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class LingStorageException extends Exception {
    private int statusCode;
    private String details;
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     */
    public LingStorageException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param cause 原因
     */
    public LingStorageException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param statusCode HTTP 状态码
     */
    public LingStorageException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param statusCode HTTP 状态码
     * @param details 详细信息
     */
    public LingStorageException(String message, int statusCode, String details) {
        super(message);
        this.statusCode = statusCode;
        this.details = details;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getDetails() {
        return details;
    }
}