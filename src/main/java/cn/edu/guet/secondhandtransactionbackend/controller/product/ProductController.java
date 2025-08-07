package cn.edu.guet.secondhandtransactionbackend.controller.product;

import cn.edu.guet.secondhandtransactionbackend.assembler.ProductAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.ProductsApi;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateProductRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductDetailVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.UpdateProductRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.product.CreateProductDTO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductDetailBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * 商品控制器
 * 负责处理与商品相关的HTTP请求，实现ProductsApi接口定义的API
 */
@RestController
public class ProductController implements ProductsApi {

  private  final  ProductService productService;

  private  final AuthenticationHelper authenticationHelper;

  private  final ProductAssembler productAssembler;

    /**
     * 构造函数，注入所需服务和组件
     * @param productAssembler 商品数据组装器，用于DTO和BO之间的转换
     * @param productService 商品服务，处理商品业务逻辑
     * @param authenticationHelper 认证工具，处理用户认证相关功能
     */
    @Autowired
    public ProductController( ProductAssembler productAssembler,ProductService productService , AuthenticationHelper    authenticationHelper) {
        this.productService = productService;
    this.productAssembler = productAssembler;
        this.authenticationHelper = authenticationHelper;
    }

    /**
     * 获取商品列表
     * 支持按关键词、分类、卖家筛选，并进行分页
     * 
     * @param query 搜索关键词
     * @param categoryId 分类ID
     * @param sellerId 卖家ID
     * @param page 页码
     * @param size 每页大小
     * @return 商品列表响应
     */
    @Override
    public ResponseEntity<ProductListVO> productsGet(String query, String categoryId, String sellerId, Integer page, Integer size) {

        // 处理sellerId为"me"的情况
        String actualSellerId = sellerId;
        if ("me".equals(sellerId)) {
            Optional<Long> currentUserIdOpt = authenticationHelper.getCurrentUserId();
            actualSellerId = currentUserIdOpt.map(Object::toString).orElse(null);
        }

        List<ProductSummaryBO> products = productService.getProducts(query, categoryId, actualSellerId, page, size);

        // 转成ProductListVO
        ProductListVO productListVO = productAssembler.toProductListVO(products, size);

        // 如果productListVO不为null，则返回200状态码和productListVO，否则返回404状态码
        return Optional.ofNullable(productListVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> productsIdDelete(String id) {
        return null;
    }

    /**
     * 获取商品详情
     * 根据商品ID获取详细信息，包括卖家信息、是否已收藏、是否关注卖家等状态
     * 
     * @param id 商品ID
     * @return 商品详情响应
     */
    @Override
    public ResponseEntity<ProductDetailVO> productsIdGet(String id) {


        // 获取当前用户ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();


        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 未认证用户
        }

        // 获取当前用户ID

        ProductDetailBO productDetailBO = productService.getProductDetailById(Long.valueOf(id), currentUserId.get());

        // 转成ProductDetailVO
        ProductDetailVO productDetailVO = productAssembler.toProductDetailVO(productDetailBO);
        return Optional.ofNullable(productDetailVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ProductDetailVO> productsIdPut(String id, UpdateProductRequest updateProductRequest) {
        return null;
    }

    /**
     * 创建商品
     * 处理商品发布请求，包含完整的错误处理和日志记录
     * 
     * @param createProductRequest 创建商品的请求对象
     * @return 创建的商品详情响应
     */
    @Override
    public ResponseEntity<ProductDetailVO> productsPost(CreateProductRequest createProductRequest) {
        try {
            System.out.println("接收到创建商品请求: " + createProductRequest);

            // 验证图片URL是否为空
            if (createProductRequest.getImageUrls() == null || createProductRequest.getImageUrls().isEmpty()) {
                System.err.println("错误: 图片URL列表为空");
                return ResponseEntity.badRequest().build();
            }

            // 打印图片URL，检查格式
            System.out.println("图片URLs:");
            createProductRequest.getImageUrls().forEach(url -> {
                System.out.println(" - " + url);
            });

            // 转换请求对象为DTO
            CreateProductDTO createProductDTO = productAssembler.toCreateProductDTO(createProductRequest);
            System.out.println("转换后的DTO: " + createProductDTO);

            // 获取当前用户ID
            Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();

            // 验证用户是否已登录
            if (currentUserId.isEmpty()) {
                System.err.println("错误: 未授权的请求，缺少有效的用户ID");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 未认证用户
            }

            System.out.println("开始创建商品，用户ID: " + currentUserId.get());
            try {
                // 创建商品
                ProductDetailBO product = productService.createProduct(createProductDTO, currentUserId.get());
                System.out.println("商品创建成功，ID: " + product.getProductId());

                // 转换为响应DTO
                ProductDetailVO productDetailVO = productAssembler.toProductDetailVO(product);
                System.out.println("响应生成成功");

                return ResponseEntity.ok(productDetailVO);
            } catch (Exception e) {
                System.err.println("创建商品时发生异常: " + e.getMessage());
                e.printStackTrace();

                // 返回带有详细错误信息的响应
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("创建失败", "服务器内部错误: " + e.getMessage()));
            }
        } catch (Exception e) {
            System.err.println("处理请求时发生异常: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 创建包含错误信息的响应对象
     * 用于返回统一格式的错误信息
     * 
     * @param title 错误标题
     * @param description 错误详细描述
     * @return 错误响应对象
     */
    private ProductDetailVO createErrorResponse(String title, String description) {
        ProductDetailVO errorResponse = new ProductDetailVO();
        errorResponse.setTitle(title);
        errorResponse.setDescription(description);
        return errorResponse;
    }
}
