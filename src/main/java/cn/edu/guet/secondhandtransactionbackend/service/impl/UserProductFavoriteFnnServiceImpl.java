package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.UserProductFavoriteFnn;
import cn.edu.guet.secondhandtransactionbackend.service.UserProductFavoriteFnnService;
import cn.edu.guet.secondhandtransactionbackend.mapper.UserProductFavoriteFnnMapper;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【user_product_favorite_fnn([行为]用户收藏商品(n-n)关系表)】的数据库操作Service实现
* @createDate 2025-07-25 18:08:42
*/
@Service
public class UserProductFavoriteFnnServiceImpl extends ServiceImpl<UserProductFavoriteFnnMapper, UserProductFavoriteFnn>
    implements UserProductFavoriteFnnService{

}




