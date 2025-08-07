package cn.edu.guet.secondhandtransactionbackend.controller.payment;

import cn.edu.guet.secondhandtransactionbackend.service.NotificationService;
import cn.edu.guet.secondhandtransactionbackend.service.OrderService;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.model.Transaction.TradeStateEnum;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付回调处理Controller
 * 处理微信支付成功后的回调通知
 */
@RestController
@RequestMapping("/payment/callback")
public class PaymentCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private NotificationService notificationService;

    // 微信支付配置参数
    @Value("${app.miniapp.appId}")
    private String appId;

    @Value("${wechat.pay.merchantId}")
    private String merchantId;

    @Value("${wechat.pay.merchantSerialNumber}")
    private String merchantSerialNumber;

    @Value("${wechat.pay.apiV3Key}")
    private String apiV3Key;

    @Value("classpath:cert/apiclient_key.pem")
    private Resource privateKeyResource;

    // 配置对象
    private Config config;

    @PostConstruct
    public void initWeChatPayConfig() {
        try {
            // 加载私钥
            PrivateKey privateKey = loadPrivateKey();

            // 创建微信支付配置 - 使用构造函数
            this.config = new RSAAutoCertificateConfig.Builder()
                    .merchantId(merchantId)
                    .privateKey(privateKey)
                    .merchantSerialNumber(merchantSerialNumber)
                    .apiV3Key(apiV3Key)
                    .build();

            logger.info("微信支付配置初始化成功");

        } catch (Exception e) {
            logger.error("微信支付配置初始化失败", e);
            throw new RuntimeException("微信支付配置初始化失败", e);
        }
    }

    /**
     * 微信支付结果回调接口
     */
    @PostMapping("/wechat")
    public ResponseEntity<Map<String, String>> wechatPayCallback(HttpServletRequest request) {
        logger.info("收到微信支付回调");

        try {
            // 创建通知解析器
            NotificationParser parser = new NotificationParser((com.wechat.pay.java.core.notification.NotificationConfig) config);

            // 构建请求参数
            RequestParam requestParam = buildRequestParam(request);

            // 验证并解析回调数据
            Transaction transaction = parser.parse(requestParam, Transaction.class);

            logger.info("微信支付回调解析成功，订单号: {}, 交易状态: {}",
                    transaction.getOutTradeNo(), transaction.getTradeState());

            // 处理支付成功逻辑
            if (TradeStateEnum.SUCCESS.equals(transaction.getTradeState())) {
                handlePaymentSuccess(transaction);
                return buildSuccessResponse();
            } else {
                logger.warn("支付未成功，订单号: {}, 状态: {}",
                        transaction.getOutTradeNo(), transaction.getTradeState());
                return buildFailResponse("支付状态异常");
            }

        } catch (Exception e) {
            logger.error("处理微信支付回调失败", e);
            return buildFailResponse("回调处理失败");
        }
    }

    /**
     * 构建微信回调请求参数
     */
    private RequestParam buildRequestParam(HttpServletRequest request) throws Exception {
        String body = getRequestBody(request);
        String signature = request.getHeader("Wechatpay-Signature");
        String serial = request.getHeader("Wechatpay-Serial");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");

        return new RequestParam.Builder()
                .serialNumber(serial)
                .nonce(nonce)
                .signature(signature)
                .timestamp(timestamp)
                .body(body)
                .build();
    }

    /**
     * 获取请求体内容
     */
    private String getRequestBody(HttpServletRequest request) throws Exception {
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    /**
     * 处理支付成功逻辑
     */
    private void handlePaymentSuccess(Transaction transaction) {
        String orderNumber = transaction.getOutTradeNo();

        try {
            // 更新订单状态为已支付
            boolean updated = orderService.updateOrderStatusToPaid(orderNumber);

            if (updated) {
                logger.info("订单 {} 支付成功，状态已更新", orderNumber);

                // 发送支付成功通知给买家和卖家
                sendPaymentSuccessNotifications(orderNumber);
            } else {
                logger.error("更新订单状态失败，订单号: {}", orderNumber);
            }

        } catch (Exception e) {
            logger.error("处理支付成功逻辑失败，订单号: {}", orderNumber, e);
        }
    }

    /**
     * 发送支付成功通知
     */
    private void sendPaymentSuccessNotifications(String orderNumber) {
        try {
            // 这里可以发送消息通知、邮件通知等
            // 示例：发送站内通知
            notificationService.sendPaymentSuccessNotification(orderNumber);
            logger.info("支付成功通知已发送，订单号: {}", orderNumber);
        } catch (Exception e) {
            logger.error("发送支付成功通知失败，订单号: {}", orderNumber, e);
        }
    }

    /**
     * 构建成功响应
     */
    private ResponseEntity<Map<String, String>> buildSuccessResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 构建失败响应
     */
    private ResponseEntity<Map<String, String>> buildFailResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("code", "FAIL");
        response.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 加载商户私钥文件
     */
    private PrivateKey loadPrivateKey() {
        try (InputStream inputStream = privateKeyResource.getInputStream()) {
            String privateKeyPem = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String privateKeyContent = privateKeyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(privateKeyContent);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("加载私钥失败", e);
        }
    }
}
