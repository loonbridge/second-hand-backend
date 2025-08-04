package cn.edu.guet.secondhandtransactionbackend.dto.review;

import java.util.List;

/**
 * 评论列表业务对象
 */
public class ReviewListBO {
    private List<ReviewBO> items;
    private Integer totalPages;
    private Long totalElements;

    public List<ReviewBO> getItems() {
        return items;
    }

    public void setItems(List<ReviewBO> items) {
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
