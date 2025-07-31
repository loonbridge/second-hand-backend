package cn.edu.guet.secondhandtransactionbackend.vo;

import lombok.Data;

/**
 * 评价视图对象
 * 用于展示商品评价信息
 */
@Data
public class ReviewVO {
    
    /**
     * 评价ID
     */
    private String reviewId;
    
    /**
     * 评价作者信息
     */
    private UserSummaryVO author;
    
    /**
     * 评价内容
     */
    private String content;
    
    /**
     * 评价时间
     */
    private String createdAt;
}
