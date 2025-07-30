package cn.edu.guet.secondhandtransactionbackend.controller.file;

import cn.edu.guet.secondhandtransactionbackend.controller.api.FilesApi;
import cn.edu.guet.secondhandtransactionbackend.dto.FileUploadResponseVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
public class FileController implements FilesApi {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.access.url}")
    private String fileAccessUrl;

    @Override
    public ResponseEntity<FileUploadResponseVO> filesUploadPost(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            System.out.println("接收到文件上传请求: " + file.getOriginalFilename() + ", 大小: " + file.getSize() + " bytes");
            
            // 创建上传目录（如果不存在）
            Path uploadPathObj = Paths.get(uploadPath);
            if (!Files.exists(uploadPathObj)) {
                System.out.println("创建上传根目录: " + uploadPathObj.toAbsolutePath());
                Files.createDirectories(uploadPathObj);
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            System.out.println("生成唯一文件名: " + uniqueFilename);

            // 使用当前日期，不再使用固定日期
            String datePath = java.time.LocalDate.now().toString().replace("-", "/");
            System.out.println("使用当前日期路径: " + datePath);
            
            Path datePathObj = uploadPathObj.resolve(datePath);
            if (!Files.exists(datePathObj)) {
                System.out.println("创建日期目录: " + datePathObj.toAbsolutePath());
                Files.createDirectories(datePathObj);
            }

            Path filePath = datePathObj.resolve(uniqueFilename);
            System.out.println("文件将保存至: " + filePath.toAbsolutePath());
            
            // 保存文件
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件保存成功");

            // 构建访问URL
            String fileUrl = fileAccessUrl + "/" + datePath + "/" + uniqueFilename;
            System.out.println("生成访问URL: " + fileUrl);

            // 创建响应对象
            FileUploadResponseVO response = new FileUploadResponseVO();
            response.setUrl(URI.create(fileUrl));
            
            // 输出日志以便调试
            System.out.println("文件上传成功: " + fileUrl);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            System.err.println("文件上传失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}