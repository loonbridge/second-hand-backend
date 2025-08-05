package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户收货地址表
 * @TableName address
 */
@TableName(value = "address")
@Data
@Accessors(chain = true)
public class Address {
    /**
     * 地址ID (主键)
     */
    @TableId(value = "address_id", type = IdType.AUTO)
    private Long addressId;

    /**
     * 所属用户ID (外键)
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 收件人姓名
     */
    @TableField(value = "receiver_name")
    private String receiverName;

    /**
     * 收件人手机号
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 省市区街道等详细地址
     */
    @TableField(value = "full_address")
    private String fullAddress;

    /**
     * 是否为默认地址 (0:否, 1:是)
     */
    @TableField(value = "is_default")
    private Integer isDefault;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;
}