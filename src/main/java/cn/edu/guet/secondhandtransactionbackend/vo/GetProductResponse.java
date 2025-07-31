package cn.edu.guet.secondhandtransactionbackend.vo;

import lombok.Data;

import java.util.List;

/**
 * 获取商品列表的响应对象
 * 包含分页信息
 */
@Data
public class GetProductResponse {
    
    /**
     * 商品列表
     */
    private List<ProductSummaryVO> items;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 总记录数
     */
    private Integer totalElements;
}
