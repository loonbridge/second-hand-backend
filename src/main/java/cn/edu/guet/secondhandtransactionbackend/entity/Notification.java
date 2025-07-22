package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 消息实体表
 * @TableName notification
 */
@TableName(value ="notification")
@Data
public class Notification {
    /**
     * 消息ID (主键)
     */
    @TableId(type = IdType.AUTO)
    private Long notificationId;

    /**
     * 消息类型(SYSTEM, TRANSACTION)
     */
    private String type;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Integer isRead;

    /**
     * 接收用户ID (外键)
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}