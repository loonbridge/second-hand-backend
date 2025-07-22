package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 商品分类实体表
 * @TableName category
 */
@TableName(value ="category")
@Data
public class Category {
    /**
     * 分类ID (主键)
     */
    @TableId(type = IdType.AUTO)
    private Long categoryId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 图标链接
     */
    private String iconUrl;
}