package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.OrderListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.OrderSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderListBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.util.CommonMappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.net.URI;

@Mapper(componentModel = "spring" , uses = {CommonMappingUtils.class})
public interface OrderAssembler {


    OrderListVO toOrderListVO(OrderListBO orderListBO);



    @Mappings({
            @Mapping(source = "orderId", target = "orderId"),
            @Mapping(source = "productImageUrl",target = "productMainImageUrl",qualifiedByName = "toUri")


    })
    OrderSummaryVO toOrderSummaryVO(OrderSummaryBO orderSummaryBO);
}
