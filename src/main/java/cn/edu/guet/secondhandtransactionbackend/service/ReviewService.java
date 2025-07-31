package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Review;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Sammy
* @description 针对表【review(评论实体表)】的数据库操作Service
* @createDate 2025-07-25 18:08:42
*/
public interface ReviewService extends IService<Review> {

    List<ReviewBO> getReviewsByProductId(Long productId);
}
