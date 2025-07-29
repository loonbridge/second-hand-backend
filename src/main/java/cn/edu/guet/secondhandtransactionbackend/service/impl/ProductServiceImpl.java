package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.assembler.ProductAssembler;
import cn.edu.guet.secondhandtransactionbackend.dto.product.CreateProductDTO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductDetailBO;
import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import cn.edu.guet.secondhandtransactionbackend.entity.UserProductFavoriteFnn;
import cn.edu.guet.secondhandtransactionbackend.entity.UserUserFollowFnn;
import cn.edu.guet.secondhandtransactionbackend.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.mapper.ProductMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Sammy
 * @description 针对表【product(商品实体表)】的数据库操作Service实现
 * @createDate 2025-07-22 15:03:51
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

   private  final  ProductMapper productMapper;
   private  final  ProductImageService productImageService;
   private  final  UserService userService;
   private  final  ReviewService reviewService;
   private  final  UserUserFollowFnnService userUserFollowFnnService;
   private  final  UserProductFavoriteFnnService userProductFavoriteFnnService;

    private  final  ProductAssembler productAssembler;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, ProductImageService productImageService,
                              UserService userService, ReviewService reviewService,
                              UserUserFollowFnnService userUserFollowFnnService,
                              UserProductFavoriteFnnService userProductFavoriteFnnService,
                              ProductAssembler productAssembler) {
        this.productMapper = productMapper;
        this.productImageService = productImageService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.userUserFollowFnnService = userUserFollowFnnService;
        this.userProductFavoriteFnnService = userProductFavoriteFnnService;
        this.productAssembler = productAssembler;
    }


    @Override
    public List<ProductSummaryBO> getProducts(String query, String categoryId, String sellerId, Integer page, Integer size) {
        // 构建查询条件（可根据实际需求完善）
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (query != null && !query.isEmpty()) {
            queryWrapper.like("title", query);
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (sellerId != null && !sellerId.isEmpty()) {
            queryWrapper.eq("user_id", sellerId);
        }

        // 分页查询
        int pageNum = (page == null || page < 1) ? 1 : page;
        int pageSize = (size == null || size < 1) ? 10 : size;
        Page<Product> mpPage =
                new Page<>(pageNum, pageSize);
        Page<Product> productPage = productMapper.selectPage(mpPage, queryWrapper);
        List<Product> products = productPage.getRecords();

        //TODO：N+1陷阱
        List<ProductSummaryBO> productSummaryBOList = products.stream()
                .map(product -> new ProductSummaryBO()
                        .setProductId(product.getProductId())
                        .setTitle(product.getTitle())
                        .setPrice(product.getPrice())
                        .setMainImageUrl(productImageService.getOne(
                                new QueryWrapper<ProductImage>()
                                        .eq("product_id", product.getProductId())
                                        .eq("disaplay_order", 0)
                        ) != null ? productImageService.getOne(
                                new QueryWrapper<ProductImage>()
                                        .eq("product_id", product.getProductId())
                                        .eq("disaplay_order", 0)
                        ).getImageUrl() : null)
                ).toList();

        return productSummaryBOList;
    }

    @Override
    public ProductDetailBO getProductDetailById(Long productId, Long currentUserId) {

        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<Product>().eq("product_id", productId);

        Product product = productMapper.selectOne(productQueryWrapper);


        //使用spring utils的bean拷贝工具，拷贝同名属性
        ProductDetailBO target = new ProductDetailBO();

        //拷贝Product的属性到productDetailBO
        BeanUtils.copyProperties(product, target);

        //当前用户是否收藏过该商品
        target.setIsFavorite(userProductFavoriteFnnService.exists(
                new QueryWrapper<UserProductFavoriteFnn>()
                        .eq("user_id", currentUserId)
                        .eq("product_id", productId)
        ));

        //当前用户是否关注过卖家
        target.setIsFollowingSeller(userUserFollowFnnService.exists(
                new QueryWrapper<UserUserFollowFnn>()
                        .eq("follower_id", currentUserId)
                        .eq("following_user_id", product.getUserId())
        ));

       /*
       *
        * TODO:需要实现 UserSummaryBO UserService.getUserInfoById(Long userId)方法
       * */
//        target.setSellerInfo(userService.getUserInfoById(product.getUserId()));   ;
//
////        TODO：需要实现 ReviewBO   ReviewService.getReviewsByProductId(Long productId)方法
//        target.setReviews(reviewService.getReviewsByProductId(productId));


        return target;
    }

    @Override
    public ProductDetailBO createProduct(CreateProductDTO createProductDTO,Long currentUserId) {

        // 创建Product实体


        Product product = new Product();


        BeanUtils.copyProperties(createProductDTO, product);




        //创建和用户的关联

        product.setUserId(currentUserId);




        //保存Product实体到数据库

        productMapper.insert(product);



        //创建和image实体的关联

        ArrayList<ProductImage> productImages = new ArrayList<>();

        List<String> imageUrls = createProductDTO.getImageUrls();

        productImages.addAll(IntStream.range(0, imageUrls.size())
                .mapToObj(i -> {
                    ProductImage productImage = new ProductImage();
                    productImage.setProductId(product.getProductId());
                    productImage.setImageUrl(imageUrls.get(i));
                    productImage.setDisplayOrder(i ); // 从0开始
                    return productImage;
                })
                .toList());


        productImageService.saveBatch(productImages); // 批量保存ProductImage实体




        //创建和category的关联

        product.setCategoryId(createProductDTO.getCategoryId());

        // 不需要再查一次，productId 已自动回填
        // 直接返回创建的 ProductDetailBO
        return getProductDetailById(product.getProductId(), currentUserId);
    }


    /*返回用户的收藏商品列表*/
    @Override
    public List<ProductSummaryBO> getFavoriteProductsByUserId(Long userId, Integer page, Integer size) {

        LambdaQueryWrapper<UserProductFavoriteFnn> userProductFavoriteFnnLambdaQueryWrapper = new LambdaQueryWrapper<>();

        userProductFavoriteFnnLambdaQueryWrapper.eq(UserProductFavoriteFnn::getUserId, userId);
        List<UserProductFavoriteFnn> list = userProductFavoriteFnnService.list(userProductFavoriteFnnLambdaQueryWrapper);

        if (list.isEmpty()) {
            return new ArrayList<>(); // 如果没有收藏，返回空列表
        }




        //获取商品的summary信息
        List<Long> productIds = list.stream()
                .map(UserProductFavoriteFnn::getProductId)
                .collect(Collectors.toList());

        // 分页查询
        int pageNum = (page == null || page < 1) ? 1 : page;
        int pageSize = (size == null || size < 1) ? 10 : size;
        Page<Product> mpPage =
                new Page<>(pageNum, pageSize);


        LambdaQueryWrapper<Product> productLambdaQueryWrapper = new LambdaQueryWrapper<>();

        productLambdaQueryWrapper.in(Product::getProductId, productIds);
//                .orderByDesc(Product::getCreatedAt); // 按创建时间降序排列
        Page<Product> productPage = productMapper.selectPage(mpPage, productLambdaQueryWrapper);

        List<Product> records = productPage.getRecords();
        // TODO：将Product转换为ProductSummaryBO
        
        //获取封面列表

        LambdaQueryWrapper<ProductImage> productImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        
        productImageLambdaQueryWrapper.in(ProductImage::getProductId, productIds)
                .eq(ProductImage::getDisplayOrder, 0); // 获取封面图片

        List<ProductImage> productImages = productImageService.list(productImageLambdaQueryWrapper);

        //转成和 List<Product>对应的List<string>

        Map<Long, String> productIdToImageUrlMap  = productImages.stream()
                .collect(Collectors.toMap(ProductImage::getProductId, ProductImage::getImageUrl, (existing, replacement) -> existing));



// 根据 products 的顺序生成对应的 mainImageUrls
        List<String> mainImageUrls = records.stream()
                .map(product -> productIdToImageUrlMap.get(product.getProductId()))
                .collect(Collectors.toList());

        return productAssembler.fromProductsInLine(records, mainImageUrls);

    }
}
