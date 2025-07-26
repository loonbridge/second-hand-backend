package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.LoginResponseVO;
import cn.edu.guet.secondhandtransactionbackend.dto.auth.LoginResponseBO;

public class AuthAssembler {


    private AuthAssembler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a login response object to a string representation.
     *
     * @param loginResponse the login response object
     * @return a string representation of the login response
     */


    public  static LoginResponseVO toLoginResponseVO(LoginResponseBO loginResponse) {
        if (loginResponse == null) {
            return null;
        }

        LoginResponseVO responseVO = new LoginResponseVO()
                .token(loginResponse.getJwt())

                //TODO：增加User传输对象DTO的转换器
                .user(UserAssembler.toUserProfileVO(loginResponse.getUserProfile()));

        return responseVO;
    }
}
