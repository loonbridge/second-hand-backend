package cn.edu.guet.secondhandtransactionbackend.controller.order;

import cn.edu.guet.secondhandtransactionbackend.assembler.OrderAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.OrdersApi;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateOrderRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.OrderDetailVO;
import cn.edu.guet.secondhandtransactionbackend.dto.OrderListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.WeChatPayParamsVO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderDetailBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderListBO;
import cn.edu.guet.secondhandtransactionbackend.service.OrderService;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    private final AuthenticationHelper authenticationHelper;

    private  final  OrderAssembler orderAssembler;


        @Autowired
        public OrderController(OrderService orderService,AuthenticationHelper    authenticationHelper,OrderAssembler orderAssembler) {
            this.orderService = orderService;
            this.authenticationHelper = authenticationHelper;
            this.orderAssembler = orderAssembler ;
        }

    @Override
    public ResponseEntity<OrderListVO> ordersGet(String status, Integer page, Integer size
    ) {
//
//
//            //获取当前用户的身份信息
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//
//        //获取身份信息中的principal
//        String userId = authentication.getName();


        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();

        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        OrderListBO orders = orderService.getOrders(status, page, size, currentUserId.get());


        // 转换为 OrderListVO

        OrderListVO orderListVO = orderAssembler.toOrderListVO(orders);


        return Optional.ofNullable(orderListVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<OrderDetailVO> ordersIdCancelPost(String id) {
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<OrderDetailBO> canceledOrder = orderService.cancelOrder(id, currentUserId.get());

        return canceledOrder
                .map(orderAssembler::toOrderDetailVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<OrderDetailVO> ordersIdGet(String id) {
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<OrderDetailBO> orderDetail = orderService.getOrderDetail(id, currentUserId.get());

        return orderDetail
                .map(orderAssembler::toOrderDetailVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<WeChatPayParamsVO> ordersPost(CreateOrderRequest createOrderRequest) {

        WeChatPayParamsVO pay = orderService.createOrder(createOrderRequest);
        return Optional.ofNullable(pay)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
