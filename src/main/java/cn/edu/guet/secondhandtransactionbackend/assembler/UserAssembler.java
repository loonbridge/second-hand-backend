package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.UpdateUserRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.UserProfileVO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileDTO;
import cn.edu.guet.secondhandtransactionbackend.entity.User;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.net.URI;

@Mapper(componentModel = "spring")
public interface UserAssembler {

    @SneakyThrows
      UserProfileVO toUserProfileVO(UserProfileBO userProfile);




      UserProfileDTO toUserProfileDTO(UpdateUserRequest updateUserRequest);


    UserProfileBO fromUser(User one);
}
