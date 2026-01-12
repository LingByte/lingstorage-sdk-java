package com.lingbyte.lingstorage.examples;

import com.lingbyte.lingstorage.LingStorageClient;
import com.lingbyte.lingstorage.LingStorageConfig;
import com.lingbyte.lingstorage.exception.LingStorageException;
import com.lingbyte.lingstorage.model.UploadOptions;
import com.lingbyte.lingstorage.model.UploadResult;

import java.io.File;

/**
 * 基本上传示例
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class BasicUploadExample {
    
    public static void main(String[] args) {
        // 创建客户端配置
        LingStorageConfig config = new LingStorageConfig.Builder()
                .baseUrl("http://localhost:7075")
                .apiKey("test-key")
                .apiSecret("test-secret")
                .build();
        
        // 创建客户端
        LingStorageClient client = new LingStorageClient(config);
        
        try {
            // 准备上传文件
            String filePath = "../../../LICENSE";
            File file = new File(filePath);
            
            if (!file.exists()) {
                System.err.println("文件不存在: " + filePath);
                return;
            }
            
            System.out.println("正在上传文件: " + filePath);
            
            // 创建上传选项
            UploadOptions options = new UploadOptions.Builder()
                    .bucket("cetide")
                    .onProgress((uploaded, total) -> {
                        double percent = (double) uploaded / total * 100;
                        System.out.printf("上传进度: %.1f%% (%d/%d bytes)%n", percent, uploaded, total);
                    })
                    .build();
            
            // 上传文件
            UploadResult result = client.uploadFile(file, options);
            
            System.out.println("上传成功!");
            System.out.println("文件信息: " + result);
            System.out.println("访问链接: " + result.getUrl());
            
        } catch (LingStorageException e) {
            System.err.println("上传失败: " + e.getMessage());
            if (e.getDetails() != null) {
                System.err.println("错误详情: " + e.getDetails());
            }
        } finally {
            // 关闭客户端
            client.close();
        }
    }
}