package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageAssembler {


    /*
    * 从ProductImage实体转换为urls
    * */

    @Named("productImageToUrl")
    default String productImageToUrl(ProductImage productImage) {
        if (productImage == null || productImage.getImageUrl() == null) {
            return null; // 或者返回一个默认值
        }
        return productImage.getImageUrl();
    }

    @IterableMapping(qualifiedByName = "productImageToUrl")
    List<String> productImagesToUrls(List<ProductImage> productImages);
}
