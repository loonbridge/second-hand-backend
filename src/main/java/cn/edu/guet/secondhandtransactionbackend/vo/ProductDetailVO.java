package cn.edu.guet.secondhandtransactionbackend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情视图对象
 * 用于商品详情页展示
 */
@Data
public class ProductDetailVO {
    
    /**
     * 商品ID
     */
    private String productId;
    
    /**
     * 商品标题
     */
    private String title;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 商品图片URL列表
     */
    private List<String> imageUrls;
    
    /**
     * 库存数量
     */
    private Integer stock;
    
    /**
     * 卖家信息
     */
    private UserSummaryVO sellerInfo;
    
    /**
     * 是否已收藏
     */
    private Boolean isFavorite;
    
    /**
     * 发布时间
     */
    private String postedAt;
    
    /**
     * 评价列表
     */
    private List<ReviewVO> reviews;
}
