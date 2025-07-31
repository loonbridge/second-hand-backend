package cn.edu.guet.secondhandtransactionbackend.controller.product;

import cn.edu.guet.secondhandtransactionbackend.controller.api.ProductsApi;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateProductRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductDetailVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ReviewVO;
import cn.edu.guet.secondhandtransactionbackend.dto.UserSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.entity.User;
import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import cn.edu.guet.secondhandtransactionbackend.entity.Review;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import cn.edu.guet.secondhandtransactionbackend.service.UserService;
import cn.edu.guet.secondhandtransactionbackend.service.ProductImageService;
import cn.edu.guet.secondhandtransactionbackend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
public class ProductController implements ProductsApi {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductImageService productImageService;


    @Autowired
    private ReviewService reviewService;

    @Override
    public ResponseEntity<ProductListVO> productsGet(String query, String categoryId, String sellerId, Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDetailVO> productsIdGet(String id) {
        // 将字符串ID转换为Long类型
        Long productId;
        try {
            productId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        
        // 查询商品信息
        Product product = productService.getById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        
        // 查询卖家信息
        User seller = userService.getById(product.getUserId());
        
        // 查询商品图片
        List<ProductImage> productImages = productImageService.lambdaQuery()
                .eq(ProductImage::getProductId, productId)
                .orderByAsc(ProductImage::getDisplayOrder)
                .list();
        
        // 查询商品评论
        List<Review> reviews = reviewService.lambdaQuery()
                .eq(Review::getProductId, productId)
                .orderByDesc(Review::getCreatedAt)
                .list();
        
        // 构建ProductDetailVO对象
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setProductId(String.valueOf(product.getProductId()));
        productDetailVO.setTitle(product.getTitle());
        productDetailVO.setDescription(product.getDescription());
        
        // 类型转换：BigDecimal转Float
        productDetailVO.setPrice(product.getPrice() != null ? product.getPrice().floatValue() : null);
        productDetailVO.setStock(product.getStock());
        
        // 类型转换：String列表转URI列表
        List<URI> imageUrls = productImages.stream()
                .map(ProductImage::getImageUrl)
                .map(url -> {
                    try {
                        return new URI(url);
                    } catch (URISyntaxException e) {
                        return null;
                    }
                })
                .filter(uri -> uri != null)
                .toList();
        productDetailVO.setImageUrls(imageUrls);
        
        // 设置卖家信息
        if (seller != null) {
            UserSummaryVO sellerInfo = new UserSummaryVO();
            sellerInfo.setUserId(String.valueOf(seller.getUserId()));
            sellerInfo.setNickname(seller.getNickname());
            
            // 类型转换：String转URI
            try {
                sellerInfo.setAvatarUrl(seller.getAvatarUrl() != null ? new URI(seller.getAvatarUrl()) : null);
            } catch (URISyntaxException e) {
                sellerInfo.setAvatarUrl(null);
            }
            
            productDetailVO.setSellerInfo(sellerInfo);
        }
        
        // 设置评论信息
        List<ReviewVO> reviewVOs = reviews.stream().map(review -> {
            ReviewVO reviewVO = new ReviewVO();
            reviewVO.setReviewId(String.valueOf(review.getReviewId()));
            reviewVO.setContent(review.getContent());
            
            // 设置评论作者信息
            User author = userService.getById(review.getUserId());
            if (author != null) {
                UserSummaryVO authorVO = new UserSummaryVO();
                authorVO.setUserId(String.valueOf(author.getUserId()));
                authorVO.setNickname(author.getNickname());
                
                try {
                    authorVO.setAvatarUrl(author.getAvatarUrl() != null ? new URI(author.getAvatarUrl()) : null);
                } catch (URISyntaxException e) {
                    authorVO.setAvatarUrl(null);
                }
                
                reviewVO.setAuthor(authorVO);
            }
            
            // 类型转换：LocalDateTime转OffsetDateTime
            if (review.getCreatedAt() != null) {
                OffsetDateTime offsetDateTime = review.getCreatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime();
                reviewVO.setCreatedAt(offsetDateTime);
            }
            
            return reviewVO;
        }).toList();
        productDetailVO.setReviews(reviewVOs);
        
        // 设置其他字段的默认值
        productDetailVO.setIsFavorite(false);
        productDetailVO.setIsFollowingSeller(false);
        
        // 类型转换：LocalDateTime转OffsetDateTime
        if (product.getCreatedAt() != null) {
            OffsetDateTime offsetDateTime = product.getCreatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime();
            productDetailVO.setPostedAt(offsetDateTime);
        }
        
        return ResponseEntity.ok(productDetailVO);
    }

    @Override
    public ResponseEntity<ProductDetailVO> productsPost(CreateProductRequest createProductRequest) {
        return null;
    }
}