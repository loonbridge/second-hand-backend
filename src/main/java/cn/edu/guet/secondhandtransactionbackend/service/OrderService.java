package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.dto.CreateOrderRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.WeChatPayParamsVO;
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
}
