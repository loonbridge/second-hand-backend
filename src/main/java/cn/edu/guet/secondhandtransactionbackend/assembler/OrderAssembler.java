package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.OrderDetailVO;
import cn.edu.guet.secondhandtransactionbackend.dto.OrderListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.OrderSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderDetailBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderListBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.util.CommonMappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {CommonMappingUtils.class, ReviewAssembler.class})
public interface OrderAssembler {

    OrderListVO toOrderListVO(OrderListBO orderListBO);

    @Mappings({
            @Mapping(source = "orderId", target = "orderId"),
            @Mapping(source = "productImageUrl", target = "productMainImageUrl", qualifiedByName = "toUri"),
            @Mapping(source = "productPrice", target = "priceAtPurchase"),
            @Mapping(target = "createdAt", ignore = true)
    })
    OrderSummaryVO toOrderSummaryVO(OrderSummaryBO orderSummaryBO);


    /**
     * 将OrderDetailBO转换为OrderDetailVO
     */
    @Mappings({
            @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "toOffsetDateTime"),
            @Mapping(source = "paidAt", target = "paidAt", qualifiedByName = "toOffsetDateTime"),
            @Mapping(source = "shippedAt", target = "shippedAt", qualifiedByName = "toOffsetDateTime"),
            @Mapping(source = "completedAt", target = "completedAt", qualifiedByName = "toOffsetDateTime"),
            @Mapping(source = "canceledAt", target = "canceledAt", qualifiedByName = "toOffsetDateTime"),
            // 处理ProductDetailVO中的字段映射
            @Mapping(source = "productSnapshot.imageUrls", target = "productSnapshot.imageUrls", qualifiedByName = "stringListToUriList"),
            @Mapping(source = "productSnapshot.sellerInfo.avatarUrl", target = "productSnapshot.sellerInfo.avatarUrl", qualifiedByName = "toUri"),
            // 处理用户头像URL映射
            @Mapping(source = "sellerInfo.avatarUrl", target = "sellerInfo.avatarUrl", qualifiedByName = "toUri"),
            @Mapping(source = "buyerInfo.avatarUrl", target = "buyerInfo.avatarUrl", qualifiedByName = "toUri")
    })
    OrderDetailVO toOrderDetailVO(OrderDetailBO orderDetailBO);
}
