package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.CreateOrderRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.WeChatPayParamsVO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderListBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import cn.edu.guet.secondhandtransactionbackend.service.ProductImageService;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.Order;
import cn.edu.guet.secondhandtransactionbackend.service.OrderService;
import cn.edu.guet.secondhandtransactionbackend.mapper.OrderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sammy
 * @description 针对表【order(订单实体表)】的数据库操作Service实现
 * @createDate 2025-07-25 18:08:42
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {


    private OrderMapper orderMapper;

    private ProductService productService;

    private ProductImageService productImageService;



    @Autowired

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderListBO getOrders(String status, Integer page, Integer size, Long currentUserId) {
        // --- 1. 分页查询基础订单信息 ---
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        // 注意：数据库字段是 user_id，而不是 buyer_id
        queryWrapper.eq("user_id", currentUserId);
        queryWrapper.orderByDesc("created_at"); // 按创建时间降序排序

        Page<Order> mpPage = new Page<>((long)page, (long)size);
        Page<Order> orderPage = this.page(mpPage, queryWrapper); // 使用 this.page() 是 Mybatis-Plus ServiceImpl 自带的方法

        List<Order> orderRecords = orderPage.getRecords();

        // 如果当前页没有订单，直接返回空结果
        if (CollectionUtils.isEmpty(orderRecords)) {
//            TODO 解决
//            return new OrderListBO(List.of(), orderPage.getPages(), orderPage.getTotal());
        }

        // --- 2. 一次性获取所有关联的商品和图片信息，避免N+1查询 ---

        // 提取所有订单关联的商品ID
        List<Long> productIds = orderRecords.stream()
                .map(Order::getProductId)
                .distinct()
                .collect(Collectors.toList());

        // 一次性查询所有相关的商品信息，并转为Map方便查找
        Map<Long, Product> productMap = productService.listByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getProductId, product -> product));

        // 一次性查询所有商品的主图（假设有对应方法）
        //TODO： 缺少获取主图方法。（实现了，但是未认证。）
//        Map<Long, String> mainImageMap = productImageService.listMainImagesByProductIds(productIds);



        LambdaQueryWrapper<ProductImage> productImageLambdaQueryWrapper = new LambdaQueryWrapper<>();

        productImageLambdaQueryWrapper.in(ProductImage::getProductId, productIds)
                .eq(ProductImage::getDisplayOrder, 0); // 获取封面图片

        List<ProductImage> productImages = productImageService.list(productImageLambdaQueryWrapper);


        //转成和 List<Product>对应的List<string>

        Map<Long, String> mainImageMap  = productImages.stream()
                .collect(Collectors.toMap(ProductImage::getProductId, ProductImage::getImageUrl, (existing, replacement) -> existing));


//        Map<Long,String> mainImageMap=null;

        // --- 3. 组装成最终的 BO (Business Object) 列表 ---
        List<OrderSummaryBO> orderSummaryBOS = orderRecords.stream().map(order -> {
            OrderSummaryBO summaryBO = new OrderSummaryBO();

            // 拷贝订单自身的基础属性
            BeanUtils.copyProperties(order, summaryBO);

            // 从Map中获取并设置关联的商品信息
            Product product = productMap.get(order.getProductId());
            if (product != null) {
                summaryBO.setProductTitle(product.getTitle());
            }

            // 从Map中获取并设置商品主图
            String mainImageUrl = mainImageMap.get(order.getProductId());
            if (mainImageUrl != null) {
                summaryBO.setProductImageUrl(mainImageUrl);
            }

            // 旧的 orderItems 和 productCount 已经不需要了

            return summaryBO;
        }).collect(Collectors.toList());


        // --- 4. 组装并返回最终的分页结果对象 ---
        //TODO 修复
        OrderListBO orderListBO = new OrderListBO();
        orderListBO.setItems(orderSummaryBOS);
        orderListBO.setTotalPages((int)orderPage.getPages()); // 从Mybatis-Plus Page对象获取总页数
        orderListBO.setTotalElements( (int)orderPage.getTotal()); // 从Mybatis-Plus Page对象获取总记录数

        return orderListBO;
    }


    @Override
    public WeChatPayParamsVO createOrder(CreateOrderRequest createOrderRequest) {


        //创建新订单
        Order order = new Order();




        return  null;

    }
}
