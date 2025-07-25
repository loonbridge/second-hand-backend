package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.Review;
import cn.edu.guet.secondhandtransactionbackend.service.ReviewService;
import cn.edu.guet.secondhandtransactionbackend.mapper.ReviewMapper;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【review(评论实体表)】的数据库操作Service实现
* @createDate 2025-07-25 18:08:42
*/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
    implements ReviewService{

}




