package cn.edu.guet.secondhandtransactionbackend.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class ProductSummaryBO {

    private Long productId;
    private  String title;
    private String mainImageUrl;


    private BigDecimal price;


}
