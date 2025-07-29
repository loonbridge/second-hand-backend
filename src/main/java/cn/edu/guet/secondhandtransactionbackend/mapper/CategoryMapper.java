package cn.edu.guet.secondhandtransactionbackend.mapper;

import cn.edu.guet.secondhandtransactionbackend.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Sammy
* @description 针对表【category(商品分类实体表)】的数据库操作Mapper
* @createDate 2025-07-25 18:08:42
* @Entity cn.edu.guet.secondhandtransactionbackend.entity.Category
*/

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




