package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.OrderItem;
import cn.edu.guet.secondhandtransactionbackend.service.OrderItemService;
import cn.edu.guet.secondhandtransactionbackend.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【order_item(订单项实体表 (属于订单))】的数据库操作Service实现
* @createDate 2025-07-25 18:08:42
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

}




