package cn.edu.guet.secondhandtransactionbackend.controller.product;

import cn.edu.guet.secondhandtransactionbackend.controller.api.ProductsApi;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateProductDTO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductDetailVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductsGet200Response;
import org.springframework.http.ResponseEntity;

public class ProductController implements ProductsApi {
    @Override
    public ResponseEntity<ProductsGet200Response> productsGet(String query, String categoryId, String sellerId, Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDetailVO> productsIdGet(String id) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDetailVO> productsPost(CreateProductDTO createProductDTO) {
        return null;
    }
}
