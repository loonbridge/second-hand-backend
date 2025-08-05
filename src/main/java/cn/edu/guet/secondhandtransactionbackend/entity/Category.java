package cn.edu.guet.secondhandtransactionbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品分类实体表
 * @TableName category
 */
@TableName(value ="category")
@Data
@Accessors(chain = true)

public class Category {
    /**
     * 分类ID (主键)
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /**
     * 分类名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 图标链接
     */
    @TableField(value = "icon_url")
    private String iconUrl;
}