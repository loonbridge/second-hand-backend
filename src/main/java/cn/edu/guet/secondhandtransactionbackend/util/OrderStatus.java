package cn.edu.guet.secondhandtransactionbackend.util;

/**
 * 订单状态常量
 * 与swagger文档中定义的状态枚举保持一致
 */
public class OrderStatus {

    /**
     * 待付款
     */
    public static final String TO_PAY = "TO_PAY";

    /**
     * 待发货
     */
    public static final String TO_SHIP = "TO_SHIP";

    /**
     * TODO：没有待收货
     */
    public static final String TO_RECEIVE = "TO_RECEIVE";

    /**
     * TODO：没有已完成
     */
    public static final String COMPLETED = "COMPLETED";

    /**
     * 已取消
     */
    public static final String CANCELED = "CANCELED";

    private OrderStatus() {
        // 工具类，禁止实例化
    }
}
