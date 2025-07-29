package cn.edu.guet.secondhandtransactionbackend.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class OrderSummaryBO {

   private Long orderId; // 订单ID
    private  String status; // 订单状态
    private BigDecimal totalPrice; // 订单总价
    private  Long productId; // 商品ID
    private  String productTitle; // 商品标题
    private  String productImageUrl; // 商品图片URL
    private BigDecimal productPrice; // 商品价格
    private Integer quantity; // 商品数量


}
