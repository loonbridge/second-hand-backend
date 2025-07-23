package cn.edu.guet.secondhandtransactionbackend.controller.order;

import cn.edu.guet.secondhandtransactionbackend.controller.api.OrdersApi;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateOrderRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.OrderListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.WeChatPayParamsVO;
import org.springframework.http.ResponseEntity;

public class OrderController implements OrdersApi {

    @Override
    public ResponseEntity<OrderListVO> ordersGet(String status, Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<WeChatPayParamsVO> ordersPost(CreateOrderRequest createOrderRequest) {
        return null;
    }
}
