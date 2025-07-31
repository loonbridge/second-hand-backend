package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.CreateOrderRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.WeChatPayParamsVO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderListBO;
import cn.edu.guet.secondhandtransactionbackend.dto.order.OrderSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Order;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import cn.edu.guet.secondhandtransactionbackend.entity.User;
import cn.edu.guet.secondhandtransactionbackend.mapper.OrderMapper;
import cn.edu.guet.secondhandtransactionbackend.service.OrderService;
import cn.edu.guet.secondhandtransactionbackend.service.ProductImageService;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import cn.edu.guet.secondhandtransactionbackend.service.UserService;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Sammy
 * @description 针对表【order(订单实体表)】的数据库操作Service实现
 * @createDate 2025-07-25 18:08:42
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthenticationHelper authenticationHelper;

    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final UserService userService;
    @Value("${app.miniapp.appId}")
    private String appId;
    @Value("${wechat.pay.notifyUrl}")
    private String notifyUrl; // 微信支付回调地址

    //商户ID
    @Value("${wechat.pay.merchantId}")
    private String merchantId;

    /**
     * 商户API私钥路径
     */
    @Value("${wechat.pay.privateKeyPath}")
    private String privateKeyPath;


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
    public OrderServiceImpl(UserService userService,OrderMapper orderMapper,
                            ProductService productService,
                            ProductImageService productImageService, AuthenticationHelper authenticationHelper) {
        this.orderMapper = orderMapper;
        this.productService = productService;
        this.productImageService = productImageService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
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

        //获取商品id中的价格

        String productId = createOrderRequest.getProductId();

        //TODO：可能的错误
        Product product = productService.getById(Long.valueOf(productId));

        BigDecimal price = product.getPrice();


        //获取当前用户的openid

        //获取当前用户的id

        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();

        if (currentUserId.isEmpty()) {
            throw new RuntimeException("未登录或用户ID无效");
        }

        Long currentId = currentUserId.get();

        //获取当前用户的openid

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserId, currentId);
        User currentUser = userService.getOne(userLambdaQueryWrapper);

        String openid = currentUser.getOpenid();


        //创建新订单
        Order order = new Order();

        // 订单号
        String orderId = UUID.randomUUID().toString().replace("-", "");

        order.setOrderNumber(orderId);


        //保存订单到数据库中
        order.setStatus("TO_PAY");
        order.setPriceAtPurchase(price);
        order.setQuantity(quantity);
        order.setTotalPrice(price.multiply(BigDecimal.valueOf(quantity)));
        order.setUserId(currentId);
        order.setProductId(Long.valueOf(productId));
        order.setOrderId(null); // 让Mybatis-Plus自动生成ID


        //插入
        this.save(order);

        //获取到订单ID
        Long orderIdLong = order.getOrderId();

        //调用微信支付API创建订单

        //打印变量
        System.out.println("AppId: " + appId);
        System.out.println("MerchantId: " + merchantId);
        System.out.println("PrivateKeyPath: " + privateKeyPath);
        System.out.println("MerchantSerialNumber: " + merchantSerialNumber);
        System.out.println("ApiV3Key: " + apiV3Key);

        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(merchantId)
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(apiV3Key)
                        .build();

        System.out.println("Config created: " + config.toString());

        JsapiService service = new JsapiService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(1);
        request.setAmount(amount);
        request.setAppid("wxcfa592ba7403e3e3");
        request.setMchid("1623889015");
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        // 订单号
//        String orderId = UUID.randomUUID().toString().replace("-", "");
        request.setOutTradeNo(orderId);
        Payer payer = new Payer();
        payer.setOpenid(openid);
//        payer.setOpenid("oFHpr7ftyAbPpAaS0TSuCUx4MyIU");
        request.setPayer(payer);
        PrepayResponse response = service.prepay(request);
        System.out.println(response.getPrepayId());

//        JsapiServiceExtension service = new JsapiServiceExtension.Builder()
//                .config(config)
//                .signType("RSA") // 不填默认为RSA
//                .build();
//
//
//        System.out.println("Creating WeChat Pay order with parameters:");
//
//// 跟之前下单示例一样，填充预下单参数
//
//
//        PrepayRequest request = new PrepayRequest();
//        Amount amount = new Amount();
//        amount.setTotal(quantity);
//        request.setAmount(amount);
//        request.setAppid(appId);
//        request.setMchid(merchantId);
//
//        request.setDescription("测试商品标题");
//        //设置调用微信支付的回调地址
//        //注意：这里的notify_url是微信支付回调地址，必须是https协议
//        request.setNotifyUrl(notifyUrl);
//        //交易订单号
////        request.setOutTradeNo("out_trade_no_001");
//
//        request.setOutTradeNo(orderId);
//
//
//
//        Payer payer = new Payer();
//        payer.setOpenid(openid);
//        request.setPayer(payer);
//
//
//// 一步获取调起支付所需的全部参数（包含已构造好的签名）
//        PrepayWithRequestPaymentResponse response = service.prepayWithRequestPayment(request);
//
//        System.out.println(response.toString());
//
//
//        WeChatPayParamsVO weChatPayParamsVO = new WeChatPayParamsVO();
//
//        weChatPayParamsVO.appId(appId)
//
//                .paySign(response.getPaySign()).signType(response.getSignType())
//                .timeStamp(response.getTimeStamp()).nonceStr(response.getNonceStr())
//                .orderId(orderIdLong.toString())
//                .setPackage(response.getPackageVal());
//                ;



        return  null;

    }
}
