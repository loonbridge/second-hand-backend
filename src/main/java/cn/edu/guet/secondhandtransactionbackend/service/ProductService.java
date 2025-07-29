package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.dto.product.CreateProductDTO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductDetailBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Sammy
* @description 针对表【product(商品实体表)】的数据库操作Service
* @createDate 2025-07-22 15:03:51
*/
public interface ProductService extends IService<Product> {

    List<ProductSummaryBO> getProducts(String query, String categoryId, String sellerId, Integer page, Integer size);


    ProductDetailBO getProductDetailById(Long productId, Long currentUserId);

    ProductDetailBO createProduct(CreateProductDTO createProductDTO,Long currentUserId);

    List<ProductSummaryBO> getFavoriteProductsByUserId(Long userId, Integer page, Integer size);
}
