package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.LoginResponseVO;
import cn.edu.guet.secondhandtransactionbackend.dto.auth.LoginResponseBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring" ,uses = {UserAssembler.class})
// 使用 UserAssembler 作为依赖，确保 UserProfile 的映射正确
public interface AuthAssembler {


    @Mappings({
            @Mapping(source = "jwt", target = "token"),
            @Mapping(source = "userProfile", target = "user")
    })
    LoginResponseVO toLoginResponseVO(LoginResponseBO loginResponse);

}
