package cn.edu.guet.secondhandtransactionbackend.mapper;

import cn.edu.guet.secondhandtransactionbackend.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Sammy
* @description 针对表【product(商品实体表)】的数据库操作Mapper
* @createDate 2025-07-22 15:03:51
* @Entity cn.edu.guet.secondhandtransactionbackend.entity.Product
*/
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}




