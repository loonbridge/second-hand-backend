package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商品实体表
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product {
    /**
     * 商品ID (主键)
     */
    @TableId(type = IdType.AUTO)
    private Long productId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 详细描述
     */
    private String description;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 状态(ON_SALE, SOLD_OUT)
     */
    private String status;

    /**
     * 卖家用户ID (外键)
     */
    private Long userId;

    /**
     * 分类ID (外键)
     */
    private Long categoryId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}