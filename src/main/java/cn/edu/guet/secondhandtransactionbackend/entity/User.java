package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 用户实体表
 * @TableName user
 */
@TableName(value ="user")
@Data
@Entity
public class User {
    /**
     * 用户ID (主键)
     */
    @Id
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 微信openid
     */
    @TableField(value = "openid")
    private String openid;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 头像链接
     */
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
}