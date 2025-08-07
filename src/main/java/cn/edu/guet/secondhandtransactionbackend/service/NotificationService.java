package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.dto.notification.NotificationListBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Notification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Sammy
* @description 针对表【notification(消息实体表)】的数据库操作Service
* @createDate 2025-07-25 18:13:45
*/
public interface NotificationService extends IService<Notification> {

    /**
     * 获取用户的通知列表
     *
     * @param type          通知类型
     * @param page          页码
     * @param size          每页大小
     * @param currentUserId 当前用户ID
     * @return 通知列表BO
     */
    NotificationListBO getNotifications(String type, Integer page, Integer size, Long currentUserId);

    /**
     * 标记所有通知为已读
     *
     * @param currentUserId 当前用户ID
     */
    void markAllAsRead(Long currentUserId);

    /**
     * 标记单条通知为已读
     *
     * @param notificationId 通知ID
     * @param currentUserId  当前用户ID
     * @return 是否操作成功
     */
    boolean markAsRead(String notificationId, Long currentUserId);

    /**
     * 删除单条通知
     *
     * @param notificationId 通知ID
     * @param currentUserId  当前用户ID
     * @return 是否操作成功
     */
    boolean deleteNotification(String notificationId, Long currentUserId);

    /**
     * 发送支付成功通知
     *
     * @param orderNumber 订单号
     */
    void sendPaymentSuccessNotification(String orderNumber);

    /**
     * 发送发货通知给买家
     *
     * @param orderNumber 订单号
     * @param buyerUserId 买家用户ID
     */
    void sendShippingNotification(String orderNumber, Long buyerUserId);

    /**
     * 发送订单完成通知给卖家
     *
     * @param orderNumber  订单号
     * @param sellerUserId 卖家用户ID
     */
    void sendOrderCompletedNotification(String orderNumber, Long sellerUserId);

    /**
     * 发送订单取消通知
     *
     * @param orderNumber 订单号
     * @param userId      用户ID
     */
    void sendOrderCancelledNotification(String orderNumber, Long userId);

    /**
     * 批量删除通知
     *
     * @param notificationIds 通知ID列表
     * @param currentUserId  当前用户ID
     */
    void deleteBatchNotifications(List<String> notificationIds, Long currentUserId);
}
