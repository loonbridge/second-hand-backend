package cn.edu.guet.secondhandtransactionbackend.dto.auth;

import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponseBO {

    private  String jwt;

    private UserProfileBO userProfile;



}
