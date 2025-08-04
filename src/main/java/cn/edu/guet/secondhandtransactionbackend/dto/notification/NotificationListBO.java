package cn.edu.guet.secondhandtransactionbackend.dto.notification;

import java.util.List;

/**
 * 通知列表业务对象
 */
public class NotificationListBO {
    private List<NotificationBO> items;
    private Integer totalPages;
    private Long totalElements;

    // Getters and Setters
    public List<NotificationBO> getItems() {
        return items;
    }

    public void setItems(List<NotificationBO> items) {
        this.items = items;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
