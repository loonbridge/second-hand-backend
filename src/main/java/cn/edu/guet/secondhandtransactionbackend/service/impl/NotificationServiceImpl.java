package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.notification.NotificationBO;
import cn.edu.guet.secondhandtransactionbackend.dto.notification.NotificationListBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Notification;
import cn.edu.guet.secondhandtransactionbackend.entity.Order;
import cn.edu.guet.secondhandtransactionbackend.mapper.NotificationMapper;
import cn.edu.guet.secondhandtransactionbackend.service.NotificationService;
import cn.edu.guet.secondhandtransactionbackend.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    @Lazy  // 使用 @Lazy 注解解决循环依赖
    private OrderService orderService;

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
    public void sendPaymentSuccessNotification(String orderNumber) {
        try {
            // 查询订单信息获取买家和卖家ID
            Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                    .eq(Order::getOrderNumber, orderNumber));

            if (order == null) {
                logger.warn("订单不存在，无法发送支付成功通知，订单号: {}", orderNumber);
                return;
            }

            // 给买家发送支付成功通知
            createNotification(
                    order.getUserId(),
                    "交易消息",
                    "支付成功",
                    String.format("您的订单 %s 支付成功，卖家将尽快为您发货", orderNumber)
            );

            // 给卖家发送新订单通知
            createNotification(
                    order.getSellerId(),
                    "交易消息",
                    "收到新订单",
                    String.format("您有新订单 %s，买家已付款，请及时发货", orderNumber)
            );

            logger.info("支付成功通知已发送，订单号: {}", orderNumber);
        } catch (Exception e) {
            logger.error("发送支付成功通知失败，订单号: {}", orderNumber, e);
        }
    }

    @Override
    @Transactional
    public void sendShippingNotification(String orderNumber, Long buyerUserId) {
        try {
            createNotification(
                    buyerUserId,
                    "交易消息",
                    "商品已发货",
                    String.format("您的订单 %s 商品已发货，请注意查收", orderNumber)
            );
            logger.info("发货通知已发送给买家，订单号: {}, 买家ID: {}", orderNumber, buyerUserId);
        } catch (Exception e) {
            logger.error("发送发货通知失败，订单号: {}, 买家ID: {}", orderNumber, buyerUserId, e);
        }
    }

    @Override
    @Transactional
    public void sendOrderCompletedNotification(String orderNumber, Long sellerUserId) {
        try {
            createNotification(
                    sellerUserId,
                    "交易消息",
                    "交易完成",
                    String.format("订单 %s 交易已完成，买家已确认收货", orderNumber)
            );
            logger.info("交易完成通知已发送给卖家，订单号: {}, 卖家ID: {}", orderNumber, sellerUserId);
        } catch (Exception e) {
            logger.error("发送交易完成通知失败，订单号: {}, 卖家ID: {}", orderNumber, sellerUserId, e);
        }
    }

    @Override
    @Transactional
    public void sendOrderCancelledNotification(String orderNumber, Long userId) {
        try {
            createNotification(
                    userId,
                    "交易消息",
                    "订单已取消",
                    String.format("订单 %s 已取消，如有疑问请联系客服", orderNumber)
            );
            logger.info("订单取消通知已发送，订单号: {}, 用户ID: {}", orderNumber, userId);
        } catch (Exception e) {
            logger.error("发送订单取消通知失败，订单号: {}, 用户ID: {}", orderNumber, userId, e);
        }
    }

    /**
     * 创建通知的通用方法
     */
    private void createNotification(Long userId, String type, String title, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(0); // 0表示未读
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());

        this.save(notification);
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
}
