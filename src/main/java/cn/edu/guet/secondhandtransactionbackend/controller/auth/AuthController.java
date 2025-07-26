package cn.edu.guet.secondhandtransactionbackend.controller.auth;

import cn.edu.guet.secondhandtransactionbackend.assembler.AuthAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.AuthApi;
import cn.edu.guet.secondhandtransactionbackend.dto.LoginRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.LoginResponseVO;
import cn.edu.guet.secondhandtransactionbackend.dto.auth.LoginResponseBO;
import cn.edu.guet.secondhandtransactionbackend.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController  implements  AuthApi{

   private WxUserService wxUserService;


   @Autowired
   public  AuthController(WxUserService wxUserService) {
        this.wxUserService = wxUserService;
    }

    @Override
    public ResponseEntity<LoginResponseVO> authLoginPost(LoginRequest loginRequest) {
        //获取wx.login()获取的code
        String code = loginRequest.getCode();

        if (code == null || code.trim().isEmpty()) {
            //为空，返回错误请求
            return  ResponseEntity.badRequest().build();
        }

        //TODO: 调用微信API，用codequ换取用户的openid和session_key
        //这里需要实现具体的逻辑来处理登录请求

        LoginResponseBO response = wxUserService.login(code);



        //如果用户存在，则返回登录信息包含，jwt，用户profile对象


        return AuthAssembler.toLoginResponseVO(response) != null
                ? ResponseEntity.ok(AuthAssembler.toLoginResponseVO(response))
                : ResponseEntity.badRequest().build();

    }
}
