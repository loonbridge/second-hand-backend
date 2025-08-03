package cn.edu.guet.secondhandtransactionbackend.dto.order;

import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductDetailBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情业务对象
 */
public class OrderDetailBO {
    private String orderId;
    private String status;
    private BigDecimal totalPrice;
    private ProductDetailBO productSnapshot;
    private UserProfileBO sellerInfo;
    private UserProfileBO buyerInfo;
    private ShippingInfoBO shippingInfo;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;
    private LocalDateTime canceledAt;

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ProductDetailBO getProductSnapshot() {
        return productSnapshot;
    }

    public void setProductSnapshot(ProductDetailBO productSnapshot) {
        this.productSnapshot = productSnapshot;
    }

    public UserProfileBO getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(UserProfileBO sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public UserProfileBO getBuyerInfo() {
        return buyerInfo;
    }

    public void setBuyerInfo(UserProfileBO buyerInfo) {
        this.buyerInfo = buyerInfo;
    }

    public ShippingInfoBO getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfoBO shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public LocalDateTime getShippedAt() {
        return shippedAt;
    }

    public void setShippedAt(LocalDateTime shippedAt) {
        this.shippedAt = shippedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    /**
     * 物流信息业务对象
     */
    public static class ShippingInfoBO {
        private String receiverName;
        private String phoneNumber;
        private String address;
        private String trackingNumber;
        private String carrier;

        // Getters and Setters
        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }
    }
}
