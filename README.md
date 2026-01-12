# LingStorage Java SDK

LingStorage 的官方 Java SDK，提供简单易用的文件上传和存储功能。

## 特性

- 🚀 **简单易用** - 几行代码即可完成文件上传
- 📁 **多种上传方式** - 支持文件路径、字节数组上传
- 🖼️ **图片处理** - 内置图片压缩、水印功能
- 📊 **进度监控** - 实时上传进度回调
- 🔄 **自动重试** - 网络异常自动重试
- 🛡️ **类型安全** - 完整的类型定义和异常处理
- ⚡ **高性能** - 基于 OkHttp 的高效 HTTP 客户端
- 🔧 **Builder 模式** - 链式调用，配置灵活

## 环境要求

- Java 8 或更高版本
- Maven 3.6+ 或 Gradle 6.0+

## 安装

### Maven

在 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.lingbyte</groupId>
    <artifactId>lingstorage-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

在 `build.gradle` 中添加依赖：

```gradle
implementation 'com.lingbyte:lingstorage-sdk:1.0.0'
```

### 手动安装

1. 克隆项目：
```bash
git clone https://github.com/LingByte/LingStorage.git
cd LingStorage/sdks/java
```

2. 编译安装：
```bash
mvn clean install
```

## 快速开始

### 基本用法

```java
import com.lingbyte.lingstorage.LingStorageClient;
import com.lingbyte.lingstorage.LingStorageConfig;
import com.lingbyte.lingstorage.model.UploadOptions;
import com.lingbyte.lingstorage.model.UploadResult;

// 创建客户端配置
LingStorageConfig config = new LingStorageConfig.Builder()
    .baseUrl("https://your-lingstorage-server.com")
    .apiKey("your-api-key")
    .apiSecret("your-api-secret")
    .build();

// 创建客户端
LingStorageClient client = new LingStorageClient(config);

try {
    // 创建上传选项
    UploadOptions options = new UploadOptions.Builder()
        .bucket("images")
        .onProgress((uploaded, total) -> {
            double percent = (double) uploaded / total * 100;
            System.out.printf("进度: %.1f%%\n", percent);
        })
        .build();
    
    // 上传文件
    UploadResult result = client.uploadFile("./image.jpg", options);
    System.out.println("上传成功: " + result.getUrl());
    
} catch (LingStorageException e) {
    System.err.println("上传失败: " + e.getMessage());
} finally {
    client.close();
}
```

## API 文档

### 客户端配置

```java
LingStorageConfig config = new LingStorageConfig.Builder()
    .baseUrl("https://your-server.com")     // 服务器地址（必需）
    .apiKey("your-api-key")                 // API 密钥（必需）
    .apiSecret("your-api-secret")           // API 密钥对应的 Secret（必需）
    .timeout(30000)                         // 请求超时时间（毫秒，默认 30000）
    .retryCount(3)                          // 重试次数（默认 3）
    .userAgent("Custom-Agent/1.0")          // 用户代理（可选）
    .build();
```

### 上传选项

```java
UploadOptions options = new UploadOptions.Builder()
    .bucket("bucket-name")                  // 存储桶名称
    .key("file-key")                        // 文件键名（可选）
    .allowedTypes(Arrays.asList("jpg", "png")) // 允许的文件类型
    .compress(true)                         // 是否压缩图片
    .quality(80)                            // 压缩质量 1-100
    .watermark(true)                        // 是否添加水印
    .watermarkText("Copyright 2024")        // 水印文字
    .watermarkPosition("bottom-right")      // 水印位置
    .onProgress((uploaded, total) -> {      // 进度回调
        // 处理进度更新
    })
    .build();
```

### 上传结果

```java
public class UploadResult {
    private String key;           // 文件键名
    private String bucket;        // 存储桶名称
    private String filename;      // 文件名
    private long size;           // 文件大小
    private long originalSize;   // 原始大小
    private boolean compressed;  // 是否已压缩
    private boolean watermarked; // 是否已添加水印
    private String url;          // 访问链接
    
    // getter 方法...
}
```

## 使用示例

### 1. 单文件上传

```java
UploadOptions options = new UploadOptions.Builder()
    .bucket("documents")
    .key("reports/2024/document.pdf")
    .build();

UploadResult result = client.uploadFile("./document.pdf", options);
System.out.println("文件链接: " + result.getUrl());
```

### 2. 从字节数组上传

```java
byte[] imageBytes = Files.readAllBytes(Paths.get("./image.jpg"));

UploadOptions options = new UploadOptions.Builder()
    .bucket("images")
    .compress(true)
    .quality(85)
    .build();

UploadResult result = client.uploadBytes(imageBytes, "image.jpg", options);
```

### 3. 批量上传

```java
List<String> files = Arrays.asList("./file1.jpg", "./file2.png", "./file3.gif");

BatchUploadOptions options = new BatchUploadOptions.Builder()
    .bucket("gallery")
    .keyPrefix("photos/2024")
    .onFileProgress((completed, total, current) -> {
        System.out.println(completed + "/" + total + ": " + current);
    })
    .build();

BatchUploadResult result = client.batchUpload(files, options);
System.out.println("成功: " + result.getSuccess().size() + 
                  ", 失败: " + result.getFailed().size());
```

### 4. 图片处理

```java
UploadOptions options = new UploadOptions.Builder()
    .bucket("images")
    .compress(true)
    .quality(80)
    .watermark(true)
    .watermarkText("Copyright 2024")
    .watermarkPosition("bottom-right")
    .build();

UploadResult result = client.uploadFile("./photo.jpg", options);

double compressionRatio = (1.0 - (double) result.getSize() / result.getOriginalSize()) * 100;
System.out.printf("压缩比: %.1f%%\n", compressionRatio);
```

### 5. 进度监控

```java
UploadOptions options = new UploadOptions.Builder()
    .bucket("files")
    .onProgress((uploaded, total) -> {
        double percent = (double) uploaded / total * 100;
        double uploadedMB = uploaded / 1024.0 / 1024.0;
        double totalMB = total / 1024.0 / 1024.0;
        
        System.out.printf("进度: %.1f%% (%.1fMB / %.1fMB)\n", 
                         percent, uploadedMB, totalMB);
    })
    .build();

client.uploadFile("./large-file.zip", options);
```

## 错误处理

SDK 提供了详细的异常信息：

```java
try {
    UploadResult result = client.uploadFile("./file.jpg", options);
} catch (LingStorageException e) {
    System.err.println("状态码: " + e.getStatusCode());
    System.err.println("错误信息: " + e.getMessage());
    System.err.println("详细信息: " + e.getDetails());
}
```

### 常见错误类型

- `401` - 认证失败，检查 API Key 和 Secret
- `413` - 文件过大，超出服务器限制
- `415` - 不支持的文件类型
- `429` - 请求过于频繁，触发限流
- `500` - 服务器内部错误

## 环境变量配置

支持通过环境变量配置客户端：

```bash
export LINGSTORAGE_BASE_URL="https://your-server.com"
export LINGSTORAGE_API_KEY="your-api-key"
export LINGSTORAGE_API_SECRET="your-api-secret"
```

```java
LingStorageConfig config = new LingStorageConfig.Builder()
    .baseUrl(System.getenv("LINGSTORAGE_BASE_URL"))
    .apiKey(System.getenv("LINGSTORAGE_API_KEY"))
    .apiSecret(System.getenv("LINGSTORAGE_API_SECRET"))
    .build();
```

## 开发

### 构建项目

```bash
mvn clean compile
```

### 运行测试

```bash
mvn test
```

### 生成测试覆盖率报告

```bash
mvn jacoco:report
```

### 打包

```bash
mvn clean package
```

### 运行示例

```bash
# 编译项目
mvn clean compile

# 基本上传示例
mvn exec:java -Dexec.mainClass="com.lingbyte.lingstorage.examples.BasicUploadExample"

# 批量上传示例
mvn exec:java -Dexec.mainClass="com.lingbyte.lingstorage.examples.BatchUploadExample"

# 图片处理示例
mvn exec:java -Dexec.mainClass="com.lingbyte.lingstorage.examples.ImageProcessingExample"
```

## 日志配置

SDK 使用 SLF4J 进行日志记录，你可以选择任何 SLF4J 兼容的日志实现：

### Logback 配置示例

```xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <logger name="com.lingbyte.lingstorage" level="DEBUG"/>
    
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

## 兼容性

- Java 8 或更高版本
- Android API Level 21+ (Android 5.0+)
- 支持所有主流 Java 框架（Spring Boot、Quarkus、Micronaut 等）

## 许可证

MIT License - 详见 [LICENSE](./LICENSE) 文件

## 支持

- 📖 [完整文档](https://docs.lings.com)
- 🐛 [问题反馈](https://github.com/LingByte/LingStorage/issues)
- 💬 [社区讨论](https://github.com/LingByte/LingStorage/discussions)