package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * [行为]用户收藏商品(n-n)关系表
 * @TableName user_product_favorite_fnn
 */
@TableName(value ="user_product_favorite_fnn")
@Data
public class UserProductFavoriteFnn {
    /**
     * 用户ID (主键, 外键)
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 商品ID (主键, 外键)
     */
    @TableId(value = "product_id")
    private Long productId;

    /**
     * 收藏时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
}