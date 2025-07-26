package cn.edu.guet.secondhandtransactionbackend.controller;

import cn.edu.guet.secondhandtransactionbackend.dto.CreateProductDTO;
import cn.edu.guet.secondhandtransactionbackend.entity.Category;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import cn.edu.guet.secondhandtransactionbackend.service.CategoryService;
import cn.edu.guet.secondhandtransactionbackend.service.ProductImageService;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import cn.edu.guet.secondhandtransactionbackend.vo.ProductDetailVO;
import cn.edu.guet.secondhandtransactionbackend.vo.ProductSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.vo.GetProductResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品控制器
 * 处理商品相关的API请求
 */
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取商品列表（首页/搜索/我的发布）
     * GET /products
     */
    @GetMapping
    public ResponseEntity<GetProductResponse> getProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String sellerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            // 构建查询条件
            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            
            // 搜索关键词
            if (query != null && !query.trim().isEmpty()) {
                queryWrapper.like("title", query)
                           .or()
                           .like("description", query);
            }
            
            // 分类筛选
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                queryWrapper.eq("category_id", categoryId);
            }
            
            // 卖家筛选（我的发布）
            if (sellerId != null && !sellerId.trim().isEmpty()) {
                if ("me".equals(sellerId)) {
                    // TODO: 从JWT token中获取当前用户ID
                    // 暂时使用固定值，实际应该从认证信息中获取
                    queryWrapper.eq("user_id", 1L);
                } else {
                    queryWrapper.eq("user_id", Long.parseLong(sellerId));
                }
            }
            
            // 只查询在售商品
            queryWrapper.eq("status", "ON_SALE");
            
            // 按创建时间倒序
            queryWrapper.orderByDesc("created_at");

            // 分页查询
            Page<Product> productPage = new Page<>(page, size);
            Page<Product> result = productService.page(productPage, queryWrapper);

            // 转换为VO
            List<ProductSummaryVO> productSummaries = result.getRecords().stream()
                    .map(this::convertToProductSummaryVO)
                    .collect(Collectors.toList());

            GetProductResponse response = new GetProductResponse();
            response.setItems(productSummaries);
            response.setTotalPages((int) result.getPages());
            response.setTotalElements((int) result.getTotal());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 测试数据库连接
     * GET /products/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> testDatabase() {
        try {
            // 测试分类表
            long categoryCount = categoryService.count();
            System.out.println("分类表记录数: " + categoryCount);

            // 测试商品表
            long productCount = productService.count();
            System.out.println("商品表记录数: " + productCount);

            return ResponseEntity.ok("数据库连接正常，分类数: " + categoryCount + ", 商品数: " + productCount);
        } catch (Exception e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("数据库连接失败: " + e.getMessage());
        }
    }

    /**
     * 发布新商品
     * POST /products
     */
    @PostMapping
    public ResponseEntity<ProductDetailVO> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        try {
            System.out.println("接收到创建商品请求: " + createProductDTO);

            // 使用MyBatis-Plus内置方法验证分类是否存在
            System.out.println("验证分类ID: " + createProductDTO.getCategoryId());
            boolean categoryExists = categoryService.count(
                new LambdaQueryWrapper<Category>()
                    .eq(Category::getCategoryId, createProductDTO.getCategoryId())
            ) > 0;

            System.out.println("分类是否存在: " + categoryExists);
            if (!categoryExists) {
                System.out.println("分类不存在，返回400错误");
                return ResponseEntity.badRequest().build(); // 分类不存在
            }

            // 创建商品实体
            Product product = new Product();
            System.out.println("开始复制属性...");
            BeanUtils.copyProperties(createProductDTO, product);
            System.out.println("属性复制完成，商品对象: " + product);

            // 设置默认值
            product.setStatus("ON_SALE");
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());

            // TODO: 从JWT token中获取当前用户ID
            // 暂时使用固定值，实际应该从认证信息中获取
            product.setUserId(1L);

            System.out.println("设置默认值后的商品对象: " + product);

            // 使用MyBatis-Plus内置方法保存商品
            System.out.println("开始保存商品到数据库...");
            boolean saved = productService.save(product);
            System.out.println("商品保存结果: " + saved + ", 商品ID: " + product.getProductId());

            if (!saved) {
                System.out.println("商品保存失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            // 暂时跳过图片保存，先测试基本商品保存
            System.out.println("跳过图片保存，直接返回商品信息");

            // 简化返回，先测试基本保存功能
            System.out.println("商品保存成功，商品ID: " + product.getProductId());

            // 创建简单的返回对象
            ProductDetailVO productDetailVO = new ProductDetailVO();
            productDetailVO.setProductId(String.valueOf(product.getProductId()));
            productDetailVO.setTitle(product.getTitle());
            productDetailVO.setDescription(product.getDescription());
            productDetailVO.setPrice(product.getPrice());
            productDetailVO.setStock(product.getStock());
            productDetailVO.setIsFavorite(false);
            productDetailVO.setPostedAt(product.getCreatedAt().toString());

            System.out.println("创建商品成功，返回结果");
            return ResponseEntity.status(HttpStatus.CREATED).body(productDetailVO);

        } catch (Exception e) {
            System.err.println("创建商品时发生异常: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取商品详情
     * GET /products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailVO> getProductById(@PathVariable String id) {
        try {
            Long productId = Long.parseLong(id);
            Product product = productService.getById(productId);
            
            if (product == null) {
                return ResponseEntity.notFound().build();
            }

            ProductDetailVO productDetailVO = convertToProductDetailVO(product);
            return ResponseEntity.ok(productDetailVO);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 转换为商品摘要VO
     */
    private ProductSummaryVO convertToProductSummaryVO(Product product) {
        ProductSummaryVO vo = new ProductSummaryVO();
        vo.setProductId(product.getProductId().toString());
        vo.setTitle(product.getTitle());
        vo.setPrice(product.getPrice());
        
        // 获取主图片（第一张图片）
        QueryWrapper<ProductImage> imageQuery = new QueryWrapper<>();
        imageQuery.eq("product_id", product.getProductId())
                  .orderByAsc("display_order")
                  .last("LIMIT 1");
        ProductImage mainImage = productImageService.getOne(imageQuery);
        if (mainImage != null) {
            vo.setMainImageUrl(mainImage.getImageUrl());
        }
        
        return vo;
    }

    /**
     * 转换为商品详情VO
     */
    private ProductDetailVO convertToProductDetailVO(Product product) {
        ProductDetailVO vo = new ProductDetailVO();
        vo.setProductId(product.getProductId().toString());
        vo.setTitle(product.getTitle());
        vo.setDescription(product.getDescription());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setPostedAt(product.getCreatedAt().toString());
        
        // 获取所有图片
        QueryWrapper<ProductImage> imageQuery = new QueryWrapper<>();
        imageQuery.eq("product_id", product.getProductId())
                  .orderByAsc("display_order");
        List<ProductImage> images = productImageService.list(imageQuery);
        List<String> imageUrls = images.stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
        vo.setImageUrls(imageUrls);
        
        // TODO: 设置卖家信息、收藏状态、评价等
        // 这里暂时设置默认值
        vo.setIsFavorite(false);
        
        return vo;
    }
}
