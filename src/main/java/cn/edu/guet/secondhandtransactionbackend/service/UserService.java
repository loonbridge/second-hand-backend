package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileDTO;
import cn.edu.guet.secondhandtransactionbackend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Sammy
* @description 针对表【user(用户实体表)】的数据库操作Service
* @createDate 2025-07-25 18:08:42
*/
public interface UserService extends IService<User> {

    UserProfileBO getUserProfileById(Long userId);


    UserProfileBO pathchUser(UserProfileDTO userProfileDTO,Long currentUserId);

    List<ProductSummaryBO> getUserFavorites(Long userId, Integer page, Integer size);
}
