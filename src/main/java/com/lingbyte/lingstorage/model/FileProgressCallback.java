package com.lingbyte.lingstorage.model;

/**
 * 文件进度回调接口
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
@FunctionalInterface
public interface FileProgressCallback {
    /**
     * 文件进度回调
     * 
     * @param completed 已完成文件数
     * @param total 总文件数
     * @param current 当前文件路径
     */
    void onProgress(int completed, int total, String current);
}