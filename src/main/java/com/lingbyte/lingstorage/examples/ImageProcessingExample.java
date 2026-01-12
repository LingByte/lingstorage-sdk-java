package com.lingbyte.lingstorage.examples;

import com.lingbyte.lingstorage.LingStorageClient;
import com.lingbyte.lingstorage.LingStorageConfig;
import com.lingbyte.lingstorage.exception.LingStorageException;
import com.lingbyte.lingstorage.model.UploadOptions;
import com.lingbyte.lingstorage.model.UploadResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 图片处理示例
 * 
 * @author LingByte Team
 * @version 1.0.0
 */
public class ImageProcessingExample {
    
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
            // 检查示例图片文件
            String imagePath = "sample.jpg";
            File imageFile = new File(imagePath);
            
            if (!imageFile.exists()) {
                System.out.println("请将一个 JPG 图片文件命名为 sample.jpg 放在当前目录下");
                return;
            }
            
            System.out.println("正在上传并处理图片: " + imagePath);
            
            // 创建上传选项（包含图片处理）
            UploadOptions options = new UploadOptions.Builder()
                    .bucket("images")
                    .key("processed/sample.jpg")
                    .compress(true)
                    .quality(80)
                    .watermark(true)
                    .watermarkText("LingStorage SDK")
                    .watermarkPosition("bottom-right")
                    .onProgress((uploaded, total) -> {
                        double percent = (double) uploaded / total * 100;
                        System.out.printf("上传进度: %.1f%% (%d/%d bytes)%n", percent, uploaded, total);
                    })
                    .build();
            
            // 上传并处理图片
            UploadResult result = client.uploadFile(imageFile, options);
            
            System.out.println("图片处理完成!");
            System.out.println("原始大小: " + result.getOriginalSize() + " bytes");
            System.out.println("压缩后大小: " + result.getSize() + " bytes");
            
            if (result.getOriginalSize() > 0) {
                double compressionRatio = (1.0 - (double) result.getSize() / result.getOriginalSize()) * 100;
                System.out.printf("压缩比: %.1f%%%n", compressionRatio);
            }
            
            System.out.println("是否压缩: " + (result.isCompressed() ? "是" : "否"));
            System.out.println("是否添加水印: " + (result.isWatermarked() ? "是" : "否"));
            System.out.println("访问链接: " + result.getUrl());
            
            // 演示从内存上传
            System.out.println("\n演示从内存上传...");
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            
            UploadOptions bytesOptions = new UploadOptions.Builder()
                    .bucket("images")
                    .key("memory/sample.jpg")
                    .compress(true)
                    .quality(60)
                    .onProgress((uploaded, total) -> {
                        double percent = (double) uploaded / total * 100;
                        System.out.printf("内存上传进度: %.1f%% (%d/%d bytes)%n", percent, uploaded, total);
                    })
                    .build();
            
            UploadResult bytesResult = client.uploadBytes(imageBytes, "memory-upload.jpg", bytesOptions);
            
            System.out.println("内存上传完成!");
            System.out.println("访问链接: " + bytesResult.getUrl());
            
        } catch (LingStorageException e) {
            System.err.println("图片处理失败: " + e.getMessage());
            if (e.getDetails() != null) {
                System.err.println("错误详情: " + e.getDetails());
            }
        } catch (IOException e) {
            System.err.println("文件读取失败: " + e.getMessage());
        } finally {
            // 关闭客户端
            client.close();
        }
    }
}