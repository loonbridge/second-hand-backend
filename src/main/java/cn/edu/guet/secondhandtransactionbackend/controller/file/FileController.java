package cn.edu.guet.secondhandtransactionbackend.controller.file;

import cn.edu.guet.secondhandtransactionbackend.assembler.FileAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.FilesApi;
import cn.edu.guet.secondhandtransactionbackend.dto.FileUploadResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FileController implements FilesApi {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private FileAssembler fileAssembler;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.endpoint}")
    private String endpoint;

    @Value("${aws.s3.show-base-url}")
    private String showBaseUrl;

    /**
     * @param files 前端需要以 "files" 作为 key 来上传文件列表
     * @return 返回包含所有上传成功文件的 URL 列表
     */
    @Override
    public ResponseEntity<FileUploadResponseVO> filesUploadPost(@RequestParam("files") List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // 跳过空文件
            }

            try {
                // 1. 生成唯一的文件名，保留原始文件扩展名
                String originalFilename = file.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                //上传的文件名称+原始扩展
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                // 2. 创建上传请求
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(uniqueFileName)
                        .contentType(file.getContentType()) // 设置Content-Type，以便浏览器正确解析
                        .build();

                // 3. 上传文件
                s3Client.putObject(putObjectRequest,
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                // 4. 拼接并添加 URL 到列表
                // 注意：确保您的代理域名后面不需要再跟 bucket 名称
                String fileUrl = showBaseUrl + "/" + uniqueFileName;
                urls.add(fileUrl);

            } catch (S3Exception | IOException e) {
                // 记录日志，处理异常
                e.printStackTrace();
                // 如果有一个文件上传失败，可以根据业务需求决定是继续还是直接返回错误
                // 这里选择返回一个服务器内部错误，并附带错误信息
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();


            }
        }

        // 所有文件上传成功后，返回 URL 列表
        return ResponseEntity.ok(new FileUploadResponseVO().urls(fileAssembler.toUriList(urls)));
    }
}