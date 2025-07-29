package cn.edu.guet.secondhandtransactionbackend.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserProfileDTO {
  private   String nickname;
    private      String avatarUrl;
}
