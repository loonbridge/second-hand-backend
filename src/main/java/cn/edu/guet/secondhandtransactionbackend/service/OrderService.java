package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.dto.CreateOrderRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.WeChatPayParamsVO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderDetailBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderListBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Optional;

/**
* @author Sammy
* @description 针对表【order(订单实体表)】的数据库操作Service
* @createDate 2025-07-25 18:08:42
*/
public interface OrderService extends IService<Order> {

    OrderListBO getOrders(String status, Integer page, Integer size, Long currentUserId);

    WeChatPayParamsVO createOrder(CreateOrderRequest createOrderRequest);

    /**
     * 获取订单详情
     *
     * @param orderId       订单ID
     * @param currentUserId 当前用户ID
     * @return 订单详情BO
     */
    Optional<OrderDetailBO> getOrderDetail(String orderId, Long currentUserId);

    /**
     * 取消订单
     *
     * @param orderId       订单ID
     * @param currentUserId 当前用户ID
     * @return 取消后的订单详情BO
     */
    Optional<OrderDetailBO> cancelOrder(String orderId, Long currentUserId);

    /**
     * 更新订单状态为已支付
     * 用于微信支付回调成功后更新订单状态
     *
     * @param orderNumber 订单号
     * @return 是否更新成功
     */
    boolean updateOrderStatusToPaid(String orderNumber);

    /**
     * 更新订单状态为已发货
     *
     * @param orderNumber   订单号
     * @param currentUserId 当前用户ID（卖家）
     * @return 是否更新成功
     */
    boolean updateOrderStatusToShipped(String orderNumber, Long currentUserId);

    /**
     * 更新订单状态为已完成
     *
     * @param orderNumber   订单号
     * @param currentUserId 当前用户ID（买家）
     * @return 是否更新成功
     */
    boolean updateOrderStatusToCompleted(String orderNumber, Long currentUserId);
}
