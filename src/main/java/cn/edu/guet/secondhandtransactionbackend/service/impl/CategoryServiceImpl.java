package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.Category;
import cn.edu.guet.secondhandtransactionbackend.service.CategoryService;
import cn.edu.guet.secondhandtransactionbackend.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【category(商品分类实体表)】的数据库操作Service实现
* @createDate 2025-07-22 15:03:50
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




