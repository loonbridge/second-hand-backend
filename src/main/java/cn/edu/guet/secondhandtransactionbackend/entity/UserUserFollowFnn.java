package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * [行为]用户关注用户(n-n)关系表
 * @TableName user_user_follow_fnn
 */
@TableName(value ="user_user_follow_fnn")
@Data
public class UserUserFollowFnn {
    /**
     * 关注者ID (主键, 外键)
     */
    @TableId(value = "follower_user_id")
    private Long followerUserId;

    /**
     * 被关注者ID (主键, 外键)
     */
    @TableId(value = "following_user_id")
    private Long followingUserId;

    /**
     * 关注时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
}