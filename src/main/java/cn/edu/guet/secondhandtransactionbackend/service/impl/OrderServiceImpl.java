package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.assembler.OrderAssembler;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateOrderRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.WeChatPayParamsVO;
import cn.edu.guet.secondhandtransactionbackend.dto.address.AddressBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderDetailBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderListBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductDetailBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Order;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import cn.edu.guet.secondhandtransactionbackend.entity.User;
import cn.edu.guet.secondhandtransactionbackend.mapper.OrderMapper;
import cn.edu.guet.secondhandtransactionbackend.service.*;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import cn.edu.guet.secondhandtransactionbackend.util.OrderStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sammy
 * @description 针对表【order(订单实体表)】的数据库操作Service实现
 * @createDate 2025-07-25 18:08:42
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderAssembler orderAssembler;
    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthenticationHelper authenticationHelper;

    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final UserService userService;
    private final AddressService addressService;
    private final NotificationService notificationService;
    @Value("${app.miniapp.appId}")
    private String appId;
    @Value("${wechat.pay.notifyUrl}")
    private String notifyUrl; // 微信支付回调地址

    //商户ID
    @Value("${wechat.pay.merchantId}")
    private String merchantId;

//    /**
//     * 商户API私钥路径
//     */
//    @Value("${wechat.pay.privateKeyPath}")
//    private String privateKeyPath;

    @Value("classpath:cert/apiclient_key.pem")
    private Resource privateKeyResource;

    private PrivateKey privateKey;
    /**
     * 商户证书序列号
     */
    @Value("${wechat.pay.merchantSerialNumber}")
    private String merchantSerialNumber;

    /**
     * 商户APIV3密钥
     */
    @Value("${wechat.pay.apiV3Key}")
    private String apiV3Key;


    @Autowired
    public OrderServiceImpl(OrderAssembler orderAssembler, UserService userService, OrderMapper orderMapper,
                            ProductService productService,
                            ProductImageService productImageService, AuthenticationHelper authenticationHelper, AddressService addressService, NotificationService notificationService) {
        this.orderMapper = orderMapper;
        this.productService = productService;
        this.productImageService = productImageService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.addressService = addressService;
        this.orderAssembler = orderAssembler;
        this.notificationService = notificationService;
    }

    /**
     * 加载商户私钥文件（.pem 格式），并解析为 Java 的 PrivateKey 对象
     */
    private PrivateKey loadPrivateKey() {
        try (InputStream inputStream = privateKeyResource.getInputStream()) {
            // 读取 PEM 文件并移除头尾和换行符
            String privateKeyPem = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String privateKeyContent = privateKeyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            // base64 解码并构造私钥对象
            byte[] decoded = Base64.getDecoder().decode(privateKeyContent);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("加载私钥失败", e);
        }
    }

    //构造调用后立马使用
    @PostConstruct
    void initWeChatPrivateKey() {

        this.privateKey = loadPrivateKey();

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

            return new OrderListBO().setItems(List.of()).setTotalPages((int) orderPage.getPages()).
                    setTotalElements((int) orderPage.getTotal());
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

    //TODO 支付功能，待做
    @Override
    //添加事务管理，避免订单创建和支付过程中的数据不一致问题
    @Transactional
    public WeChatPayParamsVO createOrder(CreateOrderRequest createOrderRequest) {
        //转换dto
        Integer quantity = createOrderRequest.getQuantity();
        String productId = createOrderRequest.getProductId();
        String addressId = createOrderRequest.getAddressId();
        String phoneNumber = createOrderRequest.getPhoneNumber();

        //获取商品信息并验证库存
        Product product = productService.getById(Long.valueOf(productId));
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        if (product.getStock() < quantity) {
            throw new RuntimeException("商品库存不足");
        }

        BigDecimal price = product.getPrice();

        //获取当前用户的id
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            throw new RuntimeException("未登录或用户ID无效");
        }

        Long currentId = currentUserId.get();

        //获取当前用户信息（包含openid）
        User currentUser = userService.getById(currentId);
        if (currentUser == null) {
            throw new RuntimeException("用户信息不存在");
        }
        String openid = currentUser.getOpenid();

        // 根据addressId获取地址信息
        List<AddressBO> userAddresses = addressService.getUserAddresses(currentId);
        AddressBO selectedAddress = userAddresses.stream()
                .filter(addr -> addr.getAddressId().equals(Long.valueOf(addressId)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("地址不存在或不属于当前用户"));

        //创建新订单
        Order order = new Order();

        // 订单号
        String orderId = UUID.randomUUID().toString().replace("-", "");
        order.setOrderNumber(orderId);

        //保存订单到数据库中
        order.setStatus(OrderStatus.TO_PAY);
        order.setPriceAtPurchase(price);
        order.setQuantity(quantity);
        order.setTotalPrice(price.multiply(BigDecimal.valueOf(quantity)));
        order.setUserId(currentId);
        order.setProductId(Long.valueOf(productId));
        order.setSellerId(product.getUserId()); // 从商品信息中获取卖家ID

        // 保存地址快照信息，而不是外键关联
        order.setReceiverNameSnapshot(selectedAddress.getReceiverName());
        order.setPhoneNumberSnapshot(phoneNumber != null ? phoneNumber : selectedAddress.getPhoneNumber());
        order.setShippingAddressSnapshot(selectedAddress.getFullAddress());

        order.setCreatedAt(LocalDateTime.now());

        //插入订单
        this.save(order);

        // 减少商品库存
        product.setStock(product.getStock() - quantity);
        productService.updateById(product);

        //获取到订单ID
        Long orderIdLong = order.getOrderId();

//


        //TODO：调用微信支付API创建订单，待完成
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(merchantId)
                .merchantSerialNumber(merchantSerialNumber)

                .privateKey(privateKey)
                .apiV3Key(apiV3Key)
                .build();

        System.out.println("config created completed");

        JsapiServiceExtension service = new JsapiServiceExtension.Builder().config(config).build();

        //预支付请求
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();

        //BigDecimal转成整型，单位为分
        Integer totalPriceInCents = price.multiply(BigDecimal.valueOf(100)).intValue();

        amount.setTotal(totalPriceInCents); // 实际应该是订单总金额的分为单位
        request.setAmount(amount);
        request.setAppid(appId);
        request.setMchid(merchantId);
        request.setDescription(product.getTitle());
        request.setNotifyUrl(notifyUrl);
        request.setOutTradeNo(orderId);

        Payer payer = new Payer();
        payer.setOpenid(openid);
        request.setPayer(payer);


        PrepayWithRequestPaymentResponse response = service.prepayWithRequestPayment(request);


        // TODO: 构造并返回微信支付�����数

        WeChatPayParamsVO weChatPayParamsVO = orderAssembler.toWeChatPayParamsVO(response);

        weChatPayParamsVO.setOrderId(orderId); // 设置订单ID

        return weChatPayParamsVO;
    }

    @Override
    public Optional<OrderDetailBO> getOrderDetail(String orderId, Long currentUserId) {
        // 查询订单基本信息
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getOrderNumber, orderId)
                .and(wrapper -> wrapper.eq(Order::getUserId, currentUserId)
                        .or()
                        .eq(Order::getSellerId, currentUserId)); // 买家或卖家都可以查看

        Order order = this.getOne(orderWrapper);
        if (order == null) {
            return Optional.empty();
        }

        OrderDetailBO orderDetailBO = new OrderDetailBO();
        BeanUtils.copyProperties(order, orderDetailBO);
        orderDetailBO.setOrderId(order.getOrderNumber());

        // 获取商品信息
        Product product = productService.getById(order.getProductId());
        if (product != null) {
            ProductDetailBO productDetailBO = new ProductDetailBO();
            BeanUtils.copyProperties(product, productDetailBO);

            // 获取商品图片
            LambdaQueryWrapper<ProductImage> imageWrapper = new LambdaQueryWrapper<>();
            imageWrapper.eq(ProductImage::getProductId, product.getProductId())
                    .orderByAsc(ProductImage::getDisplayOrder);
            List<ProductImage> images = productImageService.list(imageWrapper);
            List<String> imageUrls = images.stream()
                    .map(ProductImage::getImageUrl)
                    .collect(Collectors.toList());
            productDetailBO.setImageUrls(imageUrls);

            orderDetailBO.setProductSnapshot(productDetailBO);
        }

        // 获取买家信息
        User buyer = userService.getById(order.getUserId());
        if (buyer != null) {
            UserProfileBO buyerBO = new UserProfileBO();
            BeanUtils.copyProperties(buyer, buyerBO);
            orderDetailBO.setBuyerInfo(buyerBO);
        }

        // 获取卖家信息
        if (order.getSellerId() != null) {
            User seller = userService.getById(order.getSellerId());
            if (seller != null) {
                UserProfileBO sellerBO = new UserProfileBO();
                BeanUtils.copyProperties(seller, sellerBO);
                orderDetailBO.setSellerInfo(sellerBO);
            }
        }

        // 使用订单中保存的地址快照信息
        OrderDetailBO.ShippingInfoBO shippingInfo = new OrderDetailBO.ShippingInfoBO();
        shippingInfo.setReceiverName(order.getReceiverNameSnapshot());
        shippingInfo.setPhoneNumber(order.getPhoneNumberSnapshot());
        shippingInfo.setAddress(order.getShippingAddressSnapshot());

        // trackingNumber和carrier字段暂时为null，实际项目中需要从物流系统获取
        shippingInfo.setTrackingNumber(null);
        shippingInfo.setCarrier(null);
        orderDetailBO.setShippingInfo(shippingInfo);

        return Optional.of(orderDetailBO);
    }

    @Override
    @Transactional
    public Optional<OrderDetailBO> cancelOrder(String orderId, Long currentUserId) {
        // 查询订单 - 优先按订单号查询，如果查不到则按订单ID查询
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();

        // 先尝试按订单号查询
        orderWrapper.eq(Order::getOrderNumber, orderId)
                .eq(Order::getUserId, currentUserId);

        Order order = this.getOne(orderWrapper);

        // 如果按订单号查不到，尝试按订单ID查询
        if (order == null) {
            try {
                Long orderIdLong = Long.valueOf(orderId);
                orderWrapper.clear();
                orderWrapper.eq(Order::getOrderId, orderIdLong)
                        .eq(Order::getUserId, currentUserId);
                order = this.getOne(orderWrapper);
            } catch (NumberFormatException e) {
                // 如果orderId不是数字，则忽略
            }
        }

        if (order == null) {
            return Optional.empty();
        }

        // 检查订单状态是否可以取消
        if (!OrderStatus.TO_PAY.equals(order.getStatus()) && !OrderStatus.TO_SHIP.equals(order.getStatus())) {
            return Optional.empty(); // 只有待支付和待发货状态可以取消
        }

        // 更新订单状态为已取消
        order.setStatus(OrderStatus.CANCELED);
        order.setCanceledAt(java.time.LocalDateTime.now());
        this.updateById(order);

        // 恢复商品库存
        Product product = productService.getById(order.getProductId());
        if (product != null) {
            product.setStock(product.getStock() + order.getQuantity());
            productService.updateById(product);
        }

        // 返回更新后的订单详情，使用订单号
        return getOrderDetail(order.getOrderNumber(), currentUserId);
    }

    @Override
    @Transactional
    public boolean updateOrderStatusToPaid(String orderNumber) {
        try {
            // 查询订单
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Order::getOrderNumber, orderNumber);
            Order order = this.getOne(wrapper);

            if (order == null) {
                logger.error("订单不存在，订单号: {}", orderNumber);
                return false;
            }

            // 检查订单状态是否为待支付
            if (!OrderStatus.TO_PAY.equals(order.getStatus())) {
                logger.warn("订单状态不是待支付，无法更新为已支付，订单号: {}, 当前状态: {}",
                        orderNumber, order.getStatus());
                return false;
            }

            // 更新订单状态为待发货
            order.setStatus(OrderStatus.TO_SHIP);
            order.setPaidAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            boolean result = this.updateById(order);

            if (result) {
                logger.info("订单支付状态更新成功，订单号: {}", orderNumber);
            } else {
                logger.error("订单支付状态更新失败，订单号: {}", orderNumber);
            }

            return result;

        } catch (Exception e) {
            logger.error("更新订单支付状态时发生异常，订单号: {}", orderNumber, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateOrderStatusToShipped(String orderNumber, Long currentUserId) {
        try {
            // 查询订单
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Order::getOrderNumber, orderNumber)
                    .eq(Order::getSellerId, currentUserId); // 只有卖家可以发货
            Order order = this.getOne(wrapper);

            if (order == null) {
                logger.error("订单不存在或无权限操作，订单号: {}, 用户ID: {}", orderNumber, currentUserId);
                return false;
            }

            // 检查订单状态是否为待发货
            if (!OrderStatus.TO_SHIP.equals(order.getStatus())) {
                logger.warn("订单状态不是待发货，无法更新为已发货，订单号: {}, 当前状态: {}",
                        orderNumber, order.getStatus());
                return false;
            }

            // 更新订单状态为待收货
            order.setStatus(OrderStatus.TO_RECEIVE);
            order.setShippedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            boolean result = this.updateById(order);

            if (result) {
                logger.info("订单发货状态更新成功，订单号: {}", orderNumber);

                // 发送发货通知给买家
                notificationService.sendShippingNotification(orderNumber, order.getUserId());
            } else {
                logger.error("订单发货状态更新失败，订单号: {}", orderNumber);
            }

            return result;

        } catch (Exception e) {
            logger.error("更新订单发货状态时发生异常，订单号: {}", orderNumber, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateOrderStatusToCompleted(String orderNumber, Long currentUserId) {
        try {
            // 查询订单
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Order::getOrderNumber, orderNumber)
                    .eq(Order::getUserId, currentUserId); // 只有买家可以确认收货
            Order order = this.getOne(wrapper);

            if (order == null) {
                logger.error("订单不存在或无权限操作，订单号: {}, 用户ID: {}", orderNumber, currentUserId);
                return false;
            }

            // 检查订单状态是否为待收货
            if (!OrderStatus.TO_RECEIVE.equals(order.getStatus())) {
                logger.warn("订单状态不是待收货，无法更新为已完成，订单号: {}, 当前状态: {}",
                        orderNumber, order.getStatus());
                return false;
            }

            // 更新订单状态为已完成
            order.setStatus(OrderStatus.COMPLETED);
            order.setCompletedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            boolean result = this.updateById(order);

            if (result) {
                logger.info("订单完成状态更新成功，订单号: {}", orderNumber);

                // 发送交易完成通知给卖家
                notificationService.sendOrderCompletedNotification(orderNumber, order.getSellerId());
            } else {
                logger.error("订单完成状态更新失败，订单号: {}", orderNumber);
            }

            return result;

        } catch (Exception e) {
            logger.error("更新订单完成状态时发生异常，订单号: {}", orderNumber, e);
            return false;
        }
    }
}
