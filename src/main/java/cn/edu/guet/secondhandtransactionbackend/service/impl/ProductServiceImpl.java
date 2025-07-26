package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.mapper.ProductMapper;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Sammy
* @description 针对表【product(商品实体表)】的数据库操作Service实现
* @createDate 2025-07-22 15:03:51
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

    /**
     * 根据分类ID查询商品列表（使用MyBatis-Plus内置方法）
     */
    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return list(new LambdaQueryWrapper<Product>()
                .eq(Product::getCategoryId, categoryId)
                .eq(Product::getStatus, "ON_SALE")
                .orderByDesc(Product::getCreatedAt));
    }

    /**
     * 根据用户ID查询商品列表（使用MyBatis-Plus内置方法）
     */
    @Override
    public List<Product> getProductsByUser(Long userId) {
        return list(new LambdaQueryWrapper<Product>()
                .eq(Product::getUserId, userId)
                .orderByDesc(Product::getCreatedAt));
    }

    /**
     * 分页查询商品（使用MyBatis-Plus内置方法）
     */
    @Override
    public IPage<Product> getProductsPage(Page<Product> page, String keyword, Long categoryId, String status) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();

        // 关键词搜索
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                    .like(Product::getTitle, keyword)
                    .or()
                    .like(Product::getDescription, keyword));
        }

        // 分类筛选
        if (categoryId != null && categoryId > 0) {
            queryWrapper.eq(Product::getCategoryId, categoryId);
        }

        // 状态筛选
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(Product::getStatus, status);
        } else {
            // 默认只查询在售商品
            queryWrapper.eq(Product::getStatus, "ON_SALE");
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc(Product::getCreatedAt);

        return page(page, queryWrapper);
    }

    /**
     * 根据价格区间查询商品（使用MyBatis-Plus内置方法）
     */
    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, "ON_SALE");

        if (minPrice != null) {
            queryWrapper.ge(Product::getPrice, minPrice);
        }

        if (maxPrice != null) {
            queryWrapper.le(Product::getPrice, maxPrice);
        }

        return list(queryWrapper.orderByAsc(Product::getPrice));
    }

    /**
     * 批量更新商品状态（使用MyBatis-Plus内置方法）
     */
    @Override
    public boolean updateProductsStatus(List<Long> productIds, String status) {
        return update(new LambdaUpdateWrapper<Product>()
                .set(Product::getStatus, status)
                .in(Product::getProductId, productIds));
    }

    /**
     * 统计用户发布的商品数量（使用MyBatis-Plus内置方法）
     */
    @Override
    public long countProductsByUser(Long userId) {
        return count(new LambdaQueryWrapper<Product>()
                .eq(Product::getUserId, userId));
    }

}




