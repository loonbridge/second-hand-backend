package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import cn.edu.guet.secondhandtransactionbackend.service.ProductService;
import cn.edu.guet.secondhandtransactionbackend.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【product(商品实体表)】的数据库操作Service实现
* @createDate 2025-07-22 15:03:51
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

}




