package com.lingbyte.lingstorage.model;

/**
 * 上传进度回调接口
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
@FunctionalInterface
public interface ProgressCallback {
    /**
     * 进度回调
     * 
     * @param uploaded 已上传字节数
     * @param total 总字节数
     */
    void onProgress(long uploaded, long total);
}