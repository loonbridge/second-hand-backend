package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 订单实体表
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
     * 订单号
     */
    @TableField(value = "order_number")
    private String orderNumber;

    /**
     * 状态(TO_PAY, TO_SHIP, TO_RECEIVE, COMPLETED, CANCELED)
     */
    @TableField(value = "status")
    private String status;

    /**
     * 订单总价
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

    /**
     * 买家用户ID (外键)
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
}