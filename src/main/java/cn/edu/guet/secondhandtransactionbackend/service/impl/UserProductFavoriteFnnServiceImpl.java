package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.UserProductFavoriteFnn;
import cn.edu.guet.secondhandtransactionbackend.service.UserProductFavoriteFnnService;
import cn.edu.guet.secondhandtransactionbackend.mapper.UserProductFavoriteFnnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【user_product_favorite_fnn([行为]用户收藏商品(n-n)关系表)】的数据库操作Service实现
* @createDate 2025-07-25 18:08:42
*/
@Service
public class UserProductFavoriteFnnServiceImpl extends ServiceImpl<UserProductFavoriteFnnMapper, UserProductFavoriteFnn>
    implements UserProductFavoriteFnnService{


    private  final UserProductFavoriteFnnMapper userProductFavoriteFnnMapper;

    @Autowired
    public UserProductFavoriteFnnServiceImpl(UserProductFavoriteFnnMapper userProductFavoriteFnnMapper) {
        this.userProductFavoriteFnnMapper = userProductFavoriteFnnMapper;
    }



    /**
     * 添加用户收藏商品
     *
     * @param currentUserId 当前用户ID
     * @param productId 商品ID
     * @return 是否添加成功
     */
    @Override
    public boolean addUserProductFavorite(Long currentUserId, Long productId) {

        //创建记录

        UserProductFavoriteFnn newFav = new UserProductFavoriteFnn().setProductId(productId)
                .setUserId(currentUserId);

        int insert = userProductFavoriteFnnMapper.insert(newFav);

        //检查是否添加成功，productId，userId是联合组件

        return insert > 0;



    }
}




