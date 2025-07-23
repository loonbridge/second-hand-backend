package cn.edu.guet.secondhandtransactionbackend.controller.file;

import cn.edu.guet.secondhandtransactionbackend.controller.api.FilesApi;
import cn.edu.guet.secondhandtransactionbackend.dto.FileUploadResponseVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class FileController  implements FilesApi {

    @Override
    public ResponseEntity<FileUploadResponseVO> filesUploadPost(MultipartFile file) {
        return null;
    }
}
