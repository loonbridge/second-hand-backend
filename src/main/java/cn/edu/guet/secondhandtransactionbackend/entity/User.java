package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户实体表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * 用户ID (主键)
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像链接
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}