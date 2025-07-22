package cn.edu.guet.secondhandtransactionbackend.mapper;

import cn.edu.guet.secondhandtransactionbackend.entity.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Sammy
* @description 针对表【order_item(订单项实体表 (属于订单))】的数据库操作Mapper
* @createDate 2025-07-22 15:03:51
* @Entity cn.edu.guet.secondhandtransactionbackend.entity.OrderItem
*/
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

}




