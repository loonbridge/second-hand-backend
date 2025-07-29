package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.CreateProductRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductDetailVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ProductSummaryVO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.CreateProductDTO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductSummaryBO;
import cn.edu.guet.secondhandtransactionbackend.dto.product.ProductDetailBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.util.CommonMappingUtils;
import org.mapstruct.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring" , uses = {UserAssembler.class, CommonMappingUtils.class,ReviewAssembler.class}
,
//忽略未映射的目标属性

unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductAssembler {

    // --- Mappings for ProductSummary ---



    /**
     * Converts a single ProductSummaryBO to a ProductSummaryVO.
     */
    @Mappings({
            @Mapping(target = "price", source = "price"), // BigDecimal -> Float
            @Mapping(target = "mainImageUrl", source = "mainImageUrl",qualifiedByName = "toUri") // String -> URI
    })
    ProductSummaryVO toSummaryVO(ProductSummaryBO product);

    /**
     * MapStruct automatically creates a list-mapping method.
     */
    List<ProductSummaryVO> toSummaryVOList(List<ProductSummaryBO> products);

    // --- Custom Logic with Default Method ---


    /**
     * Replaces your static `toProductListVO` method.
     */
    default ProductListVO toProductListVO(List<ProductSummaryBO> products, Integer size) {
        if (products == null || size == null || size <= 0) {
            return new ProductListVO().items(List.of()).totalPages(0).totalElements(0);
        }

        List<ProductSummaryVO> items = this.toSummaryVOList(products);
        int totalElements = items.size();
        int totalPages = (totalElements + size - 1) / size;

        return new ProductListVO()
                .items(items)
                .totalPages(totalPages)
                .totalElements(totalElements);
    }

// --- Mappings for ProductDetail ---

    /**
     * Converts ProductDetailBO to ProductDetailVO.
     * Replaces your static `toProductDetailVO` method.
     */
    @Mappings({
            // Fix for type mismatch: BO.createdAt -> VO.postedAt
            @Mapping(source = "createdAt", target = "postedAt"),
            // Nested object mapping for sellerInfo will be handled by UserAssembler
            @Mapping(source = "sellerInfo", target = "sellerInfo"),
            // Nested list mapping for reviews will be handled by ReviewAssembler
            @Mapping(source = "reviews", target = "reviews"),
            // imageUris are handled by the String -> URI mapping below
            @Mapping(source = "imageUrls", target = "imageUrls",qualifiedByName = "toUri"),
    })
    ProductDetailVO toProductDetailVO(ProductDetailBO productDetailBO);


    // --- Mappings for CreateProduct ---

    /**
     * Converts CreateProductRequest to CreateProductDTO.
     * 处理类型转换和字段映射
     */
    @Mappings({
            @Mapping(target = "price", source = "price"), // Float -> BigDecimal
            @Mapping(target = "categoryId", source = "categoryId"), // String -> Long (需要自定义转换)
            @Mapping(target = "imageUrls", source = "imageUrls",qualifiedByName = "fromUri") // List<URI> -> List<String>
    })
    CreateProductDTO toCreateProductDTO(CreateProductRequest createProductRequest);


    /**
     * 提供一个需要 mainImageUrl 列表的重载方法
     */
    default List<ProductSummaryBO> fromProductsInLine(List<Product> products, List<String> mainImageUrls) {
        if (products == null || mainImageUrls == null) {
            return null;
        }
        if (products.size() != mainImageUrls.size()) {
            throw new IllegalArgumentException("Products 和 mainImageUrls 的数量必须一致");
        }

        return products.stream()
                .map(product -> {
                    int index = products.indexOf(product);
                    String mainImageUrl = index < mainImageUrls.size() ? mainImageUrls.get(index) : null;
                    return fromProduct(product, mainImageUrl);
                })
                .collect(Collectors.toList());
    }


    /**
     * 从单个 Product 实体转换为 ProductSummaryBO
     *
     * TODO：不保证编译错误，但是可用
     */
    @Mappings({
            @Mapping(source = "product.productId", target = "productId"),
            @Mapping(source = "product.title", target = "title"),
            @Mapping(source = "product.price", target = "price"),
            @Mapping(source = "mainImageUrl", target = "mainImageUrl")
    })
    ProductSummaryBO fromProduct(Product product, String mainImageUrl);
}
