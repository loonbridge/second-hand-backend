package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.productDetailBO;
import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import cn.edu.guet.secondhandtransactionbackend.entity.UserProductFavoriteFnn;
import cn.edu.guet.secondhandtransactionbackend.entity.UserUserFollowFnn;
import cn.edu.guet.secondhandtransactionbackend.mapper.ProductImageMapper;
import cn.edu.guet.secondhandtransactionbackend.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.mapper.ProductMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sammy
 * @description 针对表【product(商品实体表)】的数据库操作Service实现
 * @createDate 2025-07-22 15:03:51
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

    ProductMapper productMapper;
    ProductImageService productImageService;
    UserService userService;

    ReviewService reviewService;

    UserUserFollowFnnService userUserFollowFnnService;
    UserProductFavoriteFnnService userProductFavoriteFnnService;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, ProductImageService productImageMapper, UserService userService, UserUserFollowFnnService userUserFollowFnnService, UserProductFavoriteFnnService userProductFavoriteFnnService) {

        this.productMapper = productMapper;
        this.productImageService = productImageMapper;
        this.userService = userService;
        this.userUserFollowFnnService = userUserFollowFnnService;
        this.userProductFavoriteFnnService = userProductFavoriteFnnService;
    }


    @Override
    public List<ProductSummaryBO> getProducts(String query, String categoryId, String sellerId, Integer page, Integer size) {


        List<Product> products = productMapper.selectList(null);

        //N+1陷阱
        List<ProductSummaryBO> productSummaryBOList = products.stream()
                .map(product -> new ProductSummaryBO()
                        .setProductId(product.getProductId())
                        .setTitle(product.getTitle())
                        .setPrice(product.getPrice())
                        .setMainImageUrl(productImageService.getOne(

                                new QueryWrapper<ProductImage>()
                                        .eq("product_id", product.getProductId())
                                        .eq("disaplay_order", 1)
                        ).getImageUrl())
                ).toList();


        return productSummaryBOList;
    }

    @Override
    public productDetailBO getProductDetailById(Long productId, Long currentUserId) {

        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<Product>().eq("product_id", productId);

        Product product = productMapper.selectOne(productQueryWrapper);


        //使用spring utils的bean拷贝工具，拷贝同名属性
        productDetailBO target = new productDetailBO();

        //拷贝Product的属性到productDetailBO
        BeanUtils.copyProperties(product, target);

        //当前用户是否收藏过该商品
        target.setIsFavorite(userProductFavoriteFnnService.exists(
                new QueryWrapper<UserProductFavoriteFnn>()
                        .eq("user_id", currentUserId)
                        .eq("product_id", productId)
        ));

        //当前用户是否关注过卖家
        target.setIsFollowingSeller(userUserFollowFnnService.exists(
                new QueryWrapper<UserUserFollowFnn>()
                        .eq("follower_id", currentUserId)
                        .eq("following_user_id", product.getUserId())
        ));

        target.setSellerInfo(userService.getUserInfoById(product.getUserId()));   ;


        target.setReviews(reviewService.getReviewsByProductId(productId));


        return target;
    }
}




