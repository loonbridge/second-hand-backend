package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import cn.edu.guet.secondhandtransactionbackend.service.ProductImageService;
import cn.edu.guet.secondhandtransactionbackend.mapper.ProductImageMapper;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【product_image(商品图片实体表 (1对多关系))】的数据库操作Service实现
* @createDate 2025-07-22 15:03:51
*/
@Service
public class ProductImageServiceImpl extends ServiceImpl<ProductImageMapper, ProductImage>
    implements ProductImageService{

}




