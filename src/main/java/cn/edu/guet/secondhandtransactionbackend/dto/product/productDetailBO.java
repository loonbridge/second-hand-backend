package cn.edu.guet.secondhandtransactionbackend.dto.product;

import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
//集成Product实体类的属性，product的属性都要返回
//需要的实体有，product，user,review
public class productDetailBO  {
    private Long productId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer stock;

    private Boolean isFavorite; // 是否已收藏

    private Boolean isFollowingSeller; // 是否已关注卖家

    private LocalDateTime CreatedAt;

    private  UserSummaryBO sellerInfo;

    private List<String> imageUrls; // 商品图片列表

    private List<ReviewBO> reviews; // 商品评论列表
}
