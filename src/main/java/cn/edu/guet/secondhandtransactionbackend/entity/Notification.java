package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.context.annotation.Primary;

/**
 * 消息实体表
 *
 * @TableName notification
 */
//@TableName(value ="notification")
//@Data
@Entity

public class Notification {
    /**
     * 消息ID (主键)
     */
    //主键
    @Id
    @TableId(value = "notification_id", type = IdType.AUTO)
    private Long notificationId;

    /**
     * 消息类型(SYSTEM, TRANSACTION)
     */
    @TableField(value = "type")
    private String type;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 是否已读
     */
    @TableField(value = "is_read")
    private Integer isRead;

    /**
     * 接收用户ID (外键)
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
}