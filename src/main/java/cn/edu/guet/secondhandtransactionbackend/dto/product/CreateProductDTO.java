package cn.edu.guet.secondhandtransactionbackend.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;


@Data
@Accessors(chain = true)
public class CreateProductDTO {

    private String title;
    private String description;
    private BigDecimal price;
    private Integer stock;


    private Long categoryId;


    private List<String> imageUrls; // 商品图片列表

}
