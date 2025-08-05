package cn.edu.guet.secondhandtransactionbackend.dto.product;

import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
//集成Product实体类的属性，product的属性都要返回
//需要的实体有，product，user,review
@Accessors(chain = true)
public class ProductDetailBO {
    private Long productId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer stock;

    private Boolean isFavorite; // 是否已收藏

    private Boolean isFollowingSeller; // 是否已关注卖家

    private LocalDateTime CreatedAt;

    // 新增分类字段
    private Long categoryId;
    private String categoryName;

    private UserProfileBO sellerInfo;

    private List<String> imageUrls; // 商品图片列表

//    TODO：需要实现ReviewBO类。

    private List<ReviewBO> reviews; // 商品评论列表
}
