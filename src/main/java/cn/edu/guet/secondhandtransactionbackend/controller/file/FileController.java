package cn.edu.guet.secondhandtransactionbackend.controller.file;

import cn.edu.guet.secondhandtransactionbackend.controller.api.FilesApi;
import cn.edu.guet.secondhandtransactionbackend.dto.FilesUploadPost200Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class FileController  implements FilesApi {
    @Override
    public ResponseEntity<FilesUploadPost200Response> filesUploadPost(MultipartFile file) {
        return null;
    }
}
