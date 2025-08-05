package cn.edu.guet.secondhandtransactionbackend.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ProductSummaryBO {

    private Long productId;
    private String title;
    private String mainImageUrl;
    private BigDecimal price;

    // 新增分类字段
    private Long categoryId;
    private String categoryName;
}
