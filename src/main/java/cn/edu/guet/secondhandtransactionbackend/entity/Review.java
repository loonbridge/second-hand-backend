package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 评论实体表
 * @TableName review
 */
@TableName(value ="review")
@Data
public class Review {
    /**
     * 评论ID (主键)
     */
    @TableId(value = "review_id", type = IdType.AUTO)
    private Long reviewId;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 评分 (1-5)
     */
    @TableField(value = "rating")
    private Integer rating;

    /**
     * 商品ID (外键)
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 作者用户ID (外键)
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 评论时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
}