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
    @TableId(type = IdType.AUTO)
    private Long reviewId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评分 (1-5)
     */
    private Integer rating;

    /**
     * 商品ID (外键)
     */
    private Long productId;

    /**
     * 作者用户ID (外键)
     */
    private Long userId;

    /**
     * 评论时间
     */
    private LocalDateTime createdAt;
}