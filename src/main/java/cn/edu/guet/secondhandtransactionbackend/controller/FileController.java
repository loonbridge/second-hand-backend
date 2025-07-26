package cn.edu.guet.secondhandtransactionbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 * 处理文件上传相关的API请求
 */
@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*")
public class FileController {

    // 文件上传目录配置
    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    // 文件访问URL前缀配置
    @Value("${file.access.url:http://localhost:8080/files}")
    private String fileAccessUrl;

    /**
     * 上传文件（如商品图片、用户头像）
     * POST /files/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("productImage") MultipartFile file) {
        
        try {
            // 验证文件
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("文件不能为空"));
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !isValidImageType(contentType)) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("只支持图片文件 (JPG, PNG, GIF, WEBP)"));
            }

            // 验证文件大小 (10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("文件大小不能超过10MB"));
            }

            // 创建上传目录
            createUploadDirectory();

            // 生成文件名
            String fileName = generateFileName(file.getOriginalFilename());
            
            // 创建日期子目录
            String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path dateDirPath = Paths.get(uploadPath, dateDir);
            Files.createDirectories(dateDirPath);

            // 保存文件
            Path filePath = dateDirPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 生成访问URL
            String fileUrl = fileAccessUrl + "/" + dateDir + "/" + fileName;

            // 返回成功响应
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("fileName", fileName);
            response.put("size", String.valueOf(file.getSize()));
            
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("文件上传失败: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("服务器内部错误"));
        }
    }

    /**
     * 获取文件（用于文件访问）
     * GET /files/{year}/{month}/{day}/{fileName}
     */
    @GetMapping("/{year}/{month}/{day}/{fileName}")
    public ResponseEntity<byte[]> getFile(
            @PathVariable String year,
            @PathVariable String month, 
            @PathVariable String day,
            @PathVariable String fileName) {
        
        try {
            Path filePath = Paths.get(uploadPath, year, month, day, fileName);
            
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileContent = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            
            return ResponseEntity.ok()
                .header("Content-Type", contentType != null ? contentType : "application/octet-stream")
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 验证是否为有效的图片类型
     */
    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg") ||
               contentType.equals("image/jpg") ||
               contentType.equals("image/png") ||
               contentType.equals("image/gif") ||
               contentType.equals("image/webp");
    }

    /**
     * 生成唯一文件名
     */
    private String generateFileName(String originalFileName) {
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * 创建上传目录
     */
    private void createUploadDirectory() throws IOException {
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    /**
     * 创建错误响应
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
}
