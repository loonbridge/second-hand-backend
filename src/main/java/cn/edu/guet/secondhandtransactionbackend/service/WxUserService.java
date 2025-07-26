package cn.edu.guet.secondhandtransactionbackend.service;


import cn.edu.guet.secondhandtransactionbackend.dto.auth.LoginResponseBO;

public interface WxUserService {
    LoginResponseBO login(String code);
}
