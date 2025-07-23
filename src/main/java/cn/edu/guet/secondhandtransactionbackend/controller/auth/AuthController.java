package cn.edu.guet.secondhandtransactionbackend.controller.auth;

import cn.edu.guet.secondhandtransactionbackend.controller.api.AuthApi;
import cn.edu.guet.secondhandtransactionbackend.dto.LoginRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.LoginResponseVO;
import org.springframework.http.ResponseEntity;

public class AuthController  implements  AuthApi{

    @Override
    public ResponseEntity<LoginResponseVO> authLoginPost(LoginRequest loginRequest) {
        return null;
    }
}
