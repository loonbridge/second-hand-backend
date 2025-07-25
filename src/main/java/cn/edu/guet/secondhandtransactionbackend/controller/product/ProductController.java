package cn.edu.guet.secondhandtransactionbackend.controller.product;

import cn.edu.guet.secondhandtransactionbackend.assembler.ProductAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.ProductsApi;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateProductRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductDetailVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.productDetailBO;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController implements ProductsApi {

    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ResponseEntity<ProductListVO> productsGet(String query, String categoryId, String sellerId, Integer page, Integer size) {

        List<ProductSummaryBO> products = productService.getProducts(query, categoryId, sellerId, page, size);

        //转成summaryList
        List<ProductSummaryVO> summaryVOList = ProductAssembler.toSummaryVOList(products);

        //转成ProductListVO
        ProductListVO productListVO = ProductAssembler.toProductListVO(summaryVOList, size);

        //如果productListVO不为null，则返回200状态码和productListVO，否则返回404状态码

        return Optional.ofNullable(productListVO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ProductDetailVO> productsIdGet(String id) {

        //TODO：后续要引入authorization

        productDetailBO productDetailBO = productService.getProductDetailById(Long.valueOf(id), null);

        //转成ProductDetailVO
        ProductDetailVO productDetailVO = ProductAssembler.toProductDetailVO(productDetailBO);
        return null;
    }

    @Override
    public ResponseEntity<ProductDetailVO> productsPost(CreateProductRequest createProductRequest) {
        return null;
    }
}
