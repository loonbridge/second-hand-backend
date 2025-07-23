package cn.edu.guet.secondhandtransactionbackend.controller.auth;

import cn.edu.guet.secondhandtransactionbackend.controller.api.AuthApi;
import cn.edu.guet.secondhandtransactionbackend.dto.AuthLoginPost200Response;
import cn.edu.guet.secondhandtransactionbackend.dto.AuthLoginPostRequest;
import org.springframework.http.ResponseEntity;

public class AuthController  implements  AuthApi{
    @Override
    public ResponseEntity<AuthLoginPost200Response> authLoginPost(AuthLoginPostRequest authLoginPostRequest) {
        return null;
    }
}
