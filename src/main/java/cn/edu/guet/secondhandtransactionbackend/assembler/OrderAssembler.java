package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.OrderListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.OrderSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderListBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderSummaryBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderAssembler {


    OrderListVO toOrderListVO(OrderListBO orderListBO);


    @Mappings({
            @Mapping(source = "orderId", target = "orderId"),
            @Mapping(source = "productImageUrl",target = "productMainImageUrl")


    })
    OrderSummaryVO toOrderSummaryVO(OrderSummaryBO orderSummaryBO);
}
