package cn.edu.guet.secondhandtransactionbackend.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品摘要视图对象
 * 用于商品列表展示
 */
@Data
public class ProductSummaryVO {
    
    /**
     * 商品ID
     */
    private String productId;
    
    /**
     * 商品标题
     */
    private String title;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 主图片URL
     */
    private String mainImageUrl;
}
