package cn.edu.guet.secondhandtransactionbackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建商品的数据传输对象
 * 对应前端发布商品时提交的数据
 */
@Data
public class CreateProductDTO {
    
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
     * 库存数量
     */
    private Integer stock;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 商品图片URL列表
     */
    private List<String> imageUrls;
}
