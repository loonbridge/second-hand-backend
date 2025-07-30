package cn.edu.guet.secondhandtransactionbackend.controller.product;

import cn.edu.guet.secondhandtransactionbackend.assembler.ProductAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.ProductsApi;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateProductRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductDetailVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.CreateProductDTO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductDetailBO;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController implements ProductsApi {

  private  final  ProductService productService;

  private  final AuthenticationHelper authenticationHelper;

  private  final ProductAssembler productAssembler;

    @Autowired
    public ProductController( ProductAssembler productAssembler,ProductService productService , AuthenticationHelper    authenticationHelper) {
        this.productService = productService;
    this.productAssembler = productAssembler;
        this.authenticationHelper = authenticationHelper;
    }

    @Override
    public ResponseEntity<ProductListVO> productsGet(String query, String categoryId, String sellerId, Integer page, Integer size) {

        List<ProductSummaryBO> products = productService.getProducts(query, categoryId, sellerId, page, size);

        //转成ProductListVO
        ProductListVO productListVO = productAssembler.toProductListVO(products, size);

        //如果productListVO不为null，则返回200状态码和productListVO，否则返回404状态码

        return Optional.ofNullable(productListVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ProductDetailVO> productsIdGet(String id) {


//        获取当前用户ID


        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();


        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 未认证用户
        }

        //获取当前用户ID


        ProductDetailBO productDetailBO = productService.getProductDetailById(Long.valueOf(id), currentUserId.get());

        //转成ProductDetailVO
        ProductDetailVO productDetailVO = productAssembler.toProductDetailVO(productDetailBO);
        return Optional.ofNullable(productDetailVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ProductDetailVO> productsPost(CreateProductRequest createProductRequest) {

        CreateProductDTO createProductDTO = productAssembler.toCreateProductDTO(createProductRequest);

        //获取当前用户ID

        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();

        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 未认证用户
        }


        ProductDetailBO product = productService.createProduct(createProductDTO,currentUserId.get());


        ProductDetailVO productDetailVO = productAssembler.toProductDetailVO(product);


        return Optional.ofNullable(productDetailVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
