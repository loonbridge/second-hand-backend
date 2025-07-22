package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 商品图片实体表 (1对多关系)
 * @TableName product_image
 */
@TableName(value ="product_image")
@Data
public class ProductImage {
    /**
     * 图片ID (主键)
     */
    @TableId(type = IdType.AUTO)
    private Long productImageId;

    /**
     * 所属商品ID (外键)
     */
    private Long productId;

    /**
     * 图片存储URL
     */
    private String imageUrl;

    /**
     * 显示顺序 (0为主图)
     */
    private Integer displayOrder;
}