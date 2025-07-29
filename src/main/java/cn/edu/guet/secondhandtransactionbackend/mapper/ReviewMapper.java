package cn.edu.guet.secondhandtransactionbackend.mapper;

import cn.edu.guet.secondhandtransactionbackend.entity.Review;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Sammy
* @description 针对表【review(评论实体表)】的数据库操作Mapper
* @createDate 2025-07-25 18:08:42
* @Entity cn.edu.guet.secondhandtransactionbackend.entity.Review
*/
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

}




