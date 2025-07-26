package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Sammy
* @description 针对表【product(商品实体表)】的数据库操作Service
* @createDate 2025-07-22 15:03:51
*/
public interface ProductService extends IService<Product> {

    /**
     * 根据分类ID查询商品列表（使用MyBatis-Plus内置方法）
     */
    List<Product> getProductsByCategory(Long categoryId);

    /**
     * 根据用户ID查询商品列表（使用MyBatis-Plus内置方法）
     */
    List<Product> getProductsByUser(Long userId);

    /**
     * 分页查询商品（使用MyBatis-Plus内置方法）
     */
    IPage<Product> getProductsPage(Page<Product> page, String keyword, Long categoryId, String status);

    /**
     * 根据价格区间查询商品（使用MyBatis-Plus内置方法）
     */
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * 批量更新商品状态（使用MyBatis-Plus内置方法）
     */
    boolean updateProductsStatus(List<Long> productIds, String status);

    /**
     * 统计用户发布的商品数量（使用MyBatis-Plus内置方法）
     */
    long countProductsByUser(Long userId);

}
