package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.Order;
import cn.edu.guet.secondhandtransactionbackend.service.OrderService;
import cn.edu.guet.secondhandtransactionbackend.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【order(订单实体表)】的数据库操作Service实现
* @createDate 2025-07-25 18:08:42
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




