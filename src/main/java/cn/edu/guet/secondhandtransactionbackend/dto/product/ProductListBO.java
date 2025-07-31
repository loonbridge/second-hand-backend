package cn.edu.guet.secondhandtransactionbackend.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ProductListBO {

    private List<ProductSummaryBO> products; // 商品列表
    private Integer totalPages;
    private  Integer totalElements; // 总商品数
}
