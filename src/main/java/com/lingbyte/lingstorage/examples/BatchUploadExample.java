package com.lingbyte.lingstorage.examples;

import com.lingbyte.lingstorage.LingStorageClient;
import com.lingbyte.lingstorage.LingStorageConfig;
import com.lingbyte.lingstorage.model.BatchUploadOptions;
import com.lingbyte.lingstorage.model.BatchUploadResult;
import com.lingbyte.lingstorage.model.UploadError;
import com.lingbyte.lingstorage.model.UploadResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量上传示例
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class BatchUploadExample {
    
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
            // 准备要上传的文件列表
            List<String> files = new ArrayList<>();
            files.add("../../../LICENSE");
            files.add("../../../README.md");
            files.add("../../../go.mod");
            
            // 过滤存在的文件
            List<String> existingFiles = new ArrayList<>();
            for (String filePath : files) {
                File file = new File(filePath);
                if (file.exists()) {
                    existingFiles.add(filePath);
                }
            }
            
            System.out.println("准备批量上传 " + existingFiles.size() + " 个文件...");
            
            // 创建批量上传选项
            BatchUploadOptions options = new BatchUploadOptions.Builder()
                    .bucket("documents")
                    .keyPrefix("batch-upload")
                    .onFileProgress((completed, total, current) -> {
                        if (current != null && !current.isEmpty()) {
                            File file = new File(current);
                            System.out.println("正在上传: " + file.getName() + " (" + completed + "/" + total + ")");
                        } else {
                            System.out.println("批量上传完成: " + completed + "/" + total);
                        }
                    })
                    .build();
            
            // 批量上传
            BatchUploadResult result = client.batchUpload(existingFiles, options);
            
            System.out.println("\n批量上传结果:");
            System.out.println("总文件数: " + result.getTotal());
            System.out.println("成功上传: " + result.getSuccess().size());
            System.out.println("失败文件: " + result.getFailed().size());
            
            if (!result.getSuccess().isEmpty()) {
                System.out.println("\n成功上传的文件:");
                int index = 1;
                for (UploadResult uploadResult : result.getSuccess()) {
                    System.out.println(index + ". " + uploadResult.getFilename() + " -> " + uploadResult.getUrl());
                    index++;
                }
            }
            
            if (!result.getFailed().isEmpty()) {
                System.out.println("\n失败的文件:");
                int index = 1;
                for (UploadError error : result.getFailed()) {
                    System.out.println(index + ". " + error.getFile() + ": " + error.getError());
                    index++;
                }
            }
            
        } catch (Exception e) {
            System.err.println("批量上传失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭客户端
            client.close();
        }
    }
}