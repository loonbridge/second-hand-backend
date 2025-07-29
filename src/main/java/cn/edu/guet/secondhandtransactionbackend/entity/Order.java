package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 订单表 - 已为单商品购买场景优化
 * @TableName order
 */
@TableName(value ="order")
@Data
public class Order {
    /**
     * 订单ID (主键)
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 唯一的订单号
     */
    @TableField(value = "order_number")
    private String orderNumber;

    /**
     * 状态(TO_PAY, TO_SHIP, TO_RECEIVE, COMPLETED, CANCELED)
     */
    @TableField(value = "status")
    private String status;

    /**
     * 下单时单价快照
     */
    @TableField(value = "price_at_purchase")
    private BigDecimal priceAtPurchase;

    /**
     * 购买数量
     */
    @TableField(value = "quantity")
    private Integer quantity;

    /**
     * 订单总金额 (price_at_purchase * quantity)
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

    /**
     * 买家用户ID (外键, 关联user表)
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 购买的商品ID (外键, 关联product表)
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
}