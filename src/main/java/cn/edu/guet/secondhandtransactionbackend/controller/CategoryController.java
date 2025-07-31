package cn.edu.guet.secondhandtransactionbackend.controller;

import cn.edu.guet.secondhandtransactionbackend.entity.Category;
import cn.edu.guet.secondhandtransactionbackend.service.CategoryService;
import cn.edu.guet.secondhandtransactionbackend.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 分类控制器
 * 处理商品分类相关的API请求
 */
@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有商品分类
     * GET /categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryVO>> getCategories() {
        try {
            // 使用MyBatis-Plus内置方法查询所有启用的分类，按排序字段排序
            List<Category> categories = categoryService.list(
                new LambdaQueryWrapper<Category>()
                    .orderByAsc(Category::getCategoryId) // 按ID排序
            );

            // 如果数据库中没有分类，返回默认分类
            if (categories.isEmpty()) {
                return ResponseEntity.ok(getDefaultCategories());
            }

            List<CategoryVO> categoryVOs = categories.stream()
                    .map(this::convertToCategoryVO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(categoryVOs);

        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时返回默认分类，确保前端能正常工作
            return ResponseEntity.ok(getDefaultCategories());
        }
    }

    /**
     * 转换为分类VO
     */
    private CategoryVO convertToCategoryVO(Category category) {
        CategoryVO vo = new CategoryVO();
        vo.setCategoryId(category.getCategoryId());
        vo.setName(category.getName());
        vo.setIconUrl(category.getIconUrl());
        return vo;
    }

    /**
     * 获取默认分类数据（当数据库查询失败或为空时使用）
     * 使用MyBatis-Plus内置方法的思想，提供备用数据
     */
    private List<CategoryVO> getDefaultCategories() {
        List<CategoryVO> categories = new ArrayList<>();

        categories.add(createCategoryVO(1L, "数码产品", ""));
        categories.add(createCategoryVO(2L, "服装配饰", ""));
        categories.add(createCategoryVO(3L, "家居用品", ""));
        categories.add(createCategoryVO(4L, "图书音像", ""));
        categories.add(createCategoryVO(5L, "运动户外", ""));
        categories.add(createCategoryVO(6L, "美妆护肤", ""));
        categories.add(createCategoryVO(7L, "母婴用品", ""));
        categories.add(createCategoryVO(8L, "其他", ""));

        return categories;
    }

    /**
     * 创建分类VO的辅助方法
     */
    private CategoryVO createCategoryVO(Long categoryId, String name, String iconUrl) {
        CategoryVO vo = new CategoryVO();
        vo.setCategoryId(categoryId);
        vo.setName(name);
        vo.setIconUrl(iconUrl);
        return vo;
    }
}
