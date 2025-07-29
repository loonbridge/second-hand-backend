package cn.edu.guet.secondhandtransactionbackend.dto.order;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OrderListBO {

    private List<OrderSummaryBO> items;

    private  Integer     totalPages;

    private Integer totalElements;



}
