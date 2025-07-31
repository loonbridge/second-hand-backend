package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.entity.UserProductFavoriteFnn;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotNull;

/**
* @author Sammy
* @description 针对表【user_product_favorite_fnn([行为]用户收藏商品(n-n)关系表)】的数据库操作Service
* @createDate 2025-07-25 18:08:42
*/
public interface UserProductFavoriteFnnService extends IService<UserProductFavoriteFnn> {

    boolean addUserProductFavorite(@NotNull Long currentUserId, @NotNull Long productId);
}
