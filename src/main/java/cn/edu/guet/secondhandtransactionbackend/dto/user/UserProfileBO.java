package cn.edu.guet.secondhandtransactionbackend.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserProfileBO {

    private Long userId;

    private String openid;
    private String nickname;
    private String avatarUrl;
    private LocalDateTime createdAt;
}
