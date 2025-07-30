package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import cn.edu.guet.secondhandtransactionbackend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.Review;
import cn.edu.guet.secondhandtransactionbackend.service.ReviewService;
import cn.edu.guet.secondhandtransactionbackend.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sammy
 * @description 针对表【review(评论实体表)】的数据库操作Service实现
 * @createDate 2025-07-25 18:08:42
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
        implements ReviewService {


    private final ReviewMapper reviewMapper;

    private final UserService userService;


    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper, UserService userService) {
        this.reviewMapper = reviewMapper;
        this.userService = userService;
    }

    /**
     * 根据商品ID获取评论列表
     *
     * @param productId 商品ID
     * @return 评论列表
     */
    @Override
    public List<ReviewBO> getReviewsByProductId(Long productId) {


        LambdaQueryWrapper<Review> reviewLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件：根据商品ID查询评论，获取所有这个id的评论
        reviewLambdaQueryWrapper.in(Review::getProductId, productId);

        List<Review> reviews = reviewMapper.selectList(reviewLambdaQueryWrapper);


        // 将评论转换为BO对象，并填充作者信息
        // 获取评论作者信息

        return reviews.stream()
                .map(review ->{
                    ReviewBO reviewBO = new ReviewBO()
                            .setReviewId(review.getReviewId())
                            .setContent(review.getContent())
                            .setCreatedAt(review.getCreatedAt());

                    // 获取评论作者信息
                    reviewBO.setAuthor(userService.getUserProfileById(review.getUserId()));
                    return reviewBO;
                }).toList();

    }

}




