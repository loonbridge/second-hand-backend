package cn.edu.guet.secondhandtransactionbackend.controller.order;

import cn.edu.guet.secondhandtransactionbackend.controller.api.OrdersApi;
import cn.edu.guet.secondhandtransactionbackend.dto.OrderSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.dto.OrdersPostRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class OrderController implements OrdersApi {
    @Override
    public ResponseEntity<List<OrderSummaryVO>> ordersGet(String status) {
        return null;
    }

    @Override
    public ResponseEntity<OrderSummaryVO> ordersPost(OrdersPostRequest ordersPostRequest) {
        return null;
    }
}
