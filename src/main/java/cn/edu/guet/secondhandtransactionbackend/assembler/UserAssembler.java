package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.UserProfileVO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import lombok.SneakyThrows;

import java.net.URI;

public class UserAssembler {

    @SneakyThrows
    public static UserProfileVO toUserProfileVO(UserProfileBO userProfile) {

        if (userProfile == null) {
            return null;
        }

        return new UserProfileVO()
                .userId(userProfile.getUserId().toString())
                .nickname(userProfile.getNickname())
                .avatarUrl(userProfile.getAvatarUrl() != null ? new URI(userProfile.getAvatarUrl())  : null)
                .joinDate(userProfile.getCreatedAt() != null ? userProfile.getCreatedAt().toLocalDate() : null);
    }

}
