package cn.edu.guet.secondhandtransactionbackend.mapper;

import cn.edu.guet.secondhandtransactionbackend.entity.ProductImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Sammy
* @description 针对表【product_image(商品图片实体表 (1对多关系))】的数据库操作Mapper
* @createDate 2025-07-22 15:03:51
* @Entity cn.edu.guet.secondhandtransactionbackend.entity.ProductImage
*/
@Mapper
public interface ProductImageMapper extends BaseMapper<ProductImage> {

}




