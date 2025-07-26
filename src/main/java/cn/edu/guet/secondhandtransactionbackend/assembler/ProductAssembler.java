package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.ProductDetailVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.productDetailBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class ProductAssembler {


    private ProductAssembler() {
        // 私有构造函数，防止实例化
    }

    //lombok处理受检异常
    @SneakyThrows
    public static ProductSummaryVO toSummaryVO(ProductSummaryBO product)  {

      return   new ProductSummaryVO()
                .productId(product.getProductId().toString())
                .title(product.getTitle())
                .mainImageUrl( new URI(product.getMainImageUrl()))
                .price(product.getPrice().floatValue());


    }

    @SneakyThrows
    public static List<ProductSummaryVO> toSummaryVOList(List<ProductSummaryBO> products)  {
        return products.stream()
                .map(ProductAssembler::toSummaryVO)
                .toList();
    }


//    根据上层传入的每页数量来计算页数，向上取整
    public  static ProductListVO toProductListVO(List<ProductSummaryVO> products,Integer size) {
        return new ProductListVO()
                .items(products)
                .totalPages(products.size() / size + (products.size() % size == 0 ? 0 : 1))
                .totalElements(products.size());
    }
@SneakyThrows
    public static ProductDetailVO toProductDetailVO(productDetailBO productDetailBO) {

        return new ProductDetailVO()
                .productId(productDetailBO.getProductId().toString())
                .title(productDetailBO.getTitle())
                .description(productDetailBO.getDescription())
                .price(productDetailBO.getPrice().floatValue())
                .imageUrls(productDetailBO.getImageUrls()!=null ? productDetailBO.getImageUrls().stream()
                        .map(URI::new).toList() : null)

                .stock(productDetailBO.getStock())
//                TODO：需要实现toUserSummary的转换器。
                .sellerInfo(productDetailBO.getSellerInfo() != null ? productDetailBO.getSellerInfo().toUserSummaryVO() : null)
                .isFavorite(productDetailBO.getIsFavorite())
                .isFollowingSeller(productDetailBO.getIsFollowingSeller())
                .postedAt(productDetailBO.getCreatedAt())
//                TODO：需要实现ReviewVO的转换器。
                .reviews(productDetailBO.getReviews() != null ? productDetailBO.getReviews().stream().map(review -> review.toReviewVO()).toList() : null);



    }


}
