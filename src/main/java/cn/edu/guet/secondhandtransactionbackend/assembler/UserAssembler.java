package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.UpdateUserRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.UserProfileVO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileDTO;
import cn.edu.guet.secondhandtransactionbackend.entity.User;
import cn.edu.guet.secondhandtransactionbackend.util.CommonMappingUtils;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.net.URI;

@Mapper(componentModel = "spring",uses = {CommonMappingUtils.class})
public interface UserAssembler {

    @Mapping(target = "avatarUrl", source = "avatarUrl", qualifiedByName = "toUri")

    UserProfileVO toUserProfileVO(UserProfileBO userProfile);



    @Mapping(target = "avatarUrl", source = "avatarUrl", qualifiedByName = "fromUri")

      UserProfileDTO toUserProfileDTO(UpdateUserRequest updateUserRequest);


    UserProfileBO fromUser(User one);


}
