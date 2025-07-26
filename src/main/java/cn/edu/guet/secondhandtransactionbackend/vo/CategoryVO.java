package cn.edu.guet.secondhandtransactionbackend.vo;

import lombok.Data;

/**
 * 分类视图对象
 * 用于展示商品分类信息
 */
@Data
public class CategoryVO {
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称
     */
    private String name;
    
    /**
     * 分类图标URL
     */
    private String iconUrl;
}
