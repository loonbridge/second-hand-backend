package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 订单项实体表 (属于订单)
 * @TableName order_item
 */
@TableName(value ="order_item")
@Data
public class OrderItem {
    /**
     * 订单商品项ID (主键)
     */
    @TableId(type = IdType.AUTO)
    private Long orderItemId;

    /**
     * 所属订单ID (外键)
     */
    private Long orderId;

    /**
     * 关联商品ID (外键)
     */
    private Long productId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 购买时单价
     */
    private BigDecimal priceAtPurchase;
}