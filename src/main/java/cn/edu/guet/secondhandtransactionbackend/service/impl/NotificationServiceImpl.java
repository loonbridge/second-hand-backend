package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.notification.NotificationBO;
import cn.edu.guet.secondhandtransactionbackend.dto.notification.NotificationListBO;
import cn.edu.guet.secondhandtransactionbackend.dto.notification.NotificationVo;
import cn.edu.guet.secondhandtransactionbackend.entity.Notification;
import cn.edu.guet.secondhandtransactionbackend.mapper.NotificationMapper;
import cn.edu.guet.secondhandtransactionbackend.service.NotificationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Sammy
* @description 针对表【notification(消息实体表)】的数据库操作Service实现
* @createDate 2025-07-25 18:13:45
*/
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
    implements NotificationService{

    @Override
    public NotificationListBO getNotifications(String type, Integer page, Integer size, Long currentUserId) {
        // 构建查询条件
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getUserId, currentUserId);

        if (type != null && !type.isEmpty()) {
            queryWrapper.eq(Notification::getType, type);
        }

        queryWrapper.orderByDesc(Notification::getCreatedAt);

        // 分页查询
        Page<Notification> mpPage = new Page<>((long) page, (long) size);
        Page<Notification> notificationPage = this.page(mpPage, queryWrapper);

        List<Notification> notifications = notificationPage.getRecords();

        // 转换为BO
        List<NotificationBO> notificationBOs = notifications.stream().map(notification -> {
            NotificationBO notificationBO = new NotificationBO();
            BeanUtils.copyProperties(notification, notificationBO);
            notificationBO.setNotificationId(notification.getNotificationId().toString());
            // 转换Integer类型的isRead为Boolean类型
            notificationBO.setIsRead(notification.getIsRead() != null && notification.getIsRead() == 1);
            return notificationBO;
        }).collect(Collectors.toList());

        // 组装返回结果
        NotificationListBO notificationListBO = new NotificationListBO();
        notificationListBO.setItems(notificationBOs);
        notificationListBO.setTotalPages((int) notificationPage.getPages());
        notificationListBO.setTotalElements(notificationPage.getTotal());

        return notificationListBO;
    }

    @Override
    @Transactional
    public void markAllAsRead(Long currentUserId) {
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Notification::getUserId, currentUserId)
                .eq(Notification::getIsRead, 0) // 0表示未读
                .set(Notification::getIsRead, 1) // 1表示已读
                .set(Notification::getUpdatedAt, LocalDateTime.now());

        this.update(updateWrapper);
    }

    @Override
    @Transactional
    public boolean markAsRead(String notificationId, Long currentUserId) {
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Notification::getNotificationId, Long.valueOf(notificationId))
                .eq(Notification::getUserId, currentUserId)
                .set(Notification::getIsRead, 1) // 1表示已读
                .set(Notification::getUpdatedAt, LocalDateTime.now());

        return this.update(updateWrapper);
    }

    @Override
    @Transactional
    public boolean deleteNotification(String notificationId, Long currentUserId) {
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getNotificationId, Long.valueOf(notificationId))
                .eq(Notification::getUserId, currentUserId);

        return this.remove(queryWrapper);
    }

    @Override
    @Transactional
    public void deleteBatchNotifications(List<String> notificationIds, Long currentUserId) {
        if (notificationIds == null || notificationIds.isEmpty()) {
            return;
        }

        List<Long> ids = notificationIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Notification::getNotificationId, ids)
                .eq(Notification::getUserId, currentUserId);

        this.remove(queryWrapper);
    }

    private NotificationMapper notificationMapper;
    @Autowired
    public void setNotificationMapper(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    @Transactional
    public Notification inNotifications(NotificationVo notificationVo) {
        // 1. 参数校验


        try {
            // 2. 插入数据库（需确保mapper配置了主键回填）
            notificationMapper.insertNotification(notificationVo);

            // 3. 从Vo转换为实体（如果需要）
            Notification notification = new Notification();
            BeanUtils.copyProperties(notificationVo, notification);


            return notification;

        } catch (Exception e) {
            // 抛出自定义异常，便于上层统一处理
            throw new RuntimeException("创建通知失败，请稍后重试", e);
        }
    }
}


