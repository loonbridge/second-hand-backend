package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewListBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Review;
import cn.edu.guet.secondhandtransactionbackend.mapper.ReviewMapper;
import cn.edu.guet.secondhandtransactionbackend.service.ReviewService;
import cn.edu.guet.secondhandtransactionbackend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                            .setRating(review.getRating())
                            .setCreatedAt(review.getCreatedAt());

                    // 获取评论作者信息
                    reviewBO.setAuthor(userService.getUserProfileById(review.getUserId()));
                    return reviewBO;
                }).toList();
    }

    @Override
    public ReviewListBO getReviewsByProductIdWithPagination(Long productId, Integer page, Integer size) {
        // 设置分页参数
        Page<Review> pageParam = new Page<>(page, size);

        LambdaQueryWrapper<Review> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Review::getProductId, productId)
                .orderByDesc(Review::getCreatedAt);

        IPage<Review> reviewPage = reviewMapper.selectPage(pageParam, queryWrapper);

        // 转换为BO对象
        List<ReviewBO> reviewBOList = reviewPage.getRecords().stream()
                .map(review -> {
                    ReviewBO reviewBO = new ReviewBO()
                            .setReviewId(review.getReviewId())
                            .setContent(review.getContent())
                            .setRating(review.getRating())
                            .setCreatedAt(review.getCreatedAt());

                    // 获取评论作者信息
                    reviewBO.setAuthor(userService.getUserProfileById(review.getUserId()));
                    return reviewBO;
                }).toList();

        ReviewListBO reviewListBO = new ReviewListBO();
        reviewListBO.setItems(reviewBOList);
        reviewListBO.setTotalPages((int) reviewPage.getPages());
        reviewListBO.setTotalElements(reviewPage.getTotal());

        return reviewListBO;
    }

    @Override
    public Optional<ReviewBO> getReviewById(Long reviewId) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null) {
            return Optional.empty();
        }

        ReviewBO reviewBO = new ReviewBO()
                .setReviewId(review.getReviewId())
                .setContent(review.getContent())
                .setRating(review.getRating())
                .setCreatedAt(review.getCreatedAt());

        // 获取评论作者信息
        reviewBO.setAuthor(userService.getUserProfileById(review.getUserId()));

        return Optional.of(reviewBO);
    }

    @Override
    public ReviewBO createReview(Long productId, Long userId, String content, Integer rating) {
        Review review = new Review();
        review.setProductId(productId);
        review.setUserId(userId);
        review.setContent(content);
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());

        reviewMapper.insert(review);

        // 返回创建的评论BO
        ReviewBO reviewBO = new ReviewBO()
                .setReviewId(review.getReviewId())
                .setContent(review.getContent())
                .setRating(review.getRating())
                .setCreatedAt(review.getCreatedAt());

        // 获取评论作者信息
        reviewBO.setAuthor(userService.getUserProfileById(userId));

        return reviewBO;
    }

    @Override
    public Optional<ReviewBO> updateReview(Long reviewId, Long userId, String content, Integer rating) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null || !review.getUserId().equals(userId)) {
            return Optional.empty();
        }

        // 更新评论内容
        if (content != null) {
            review.setContent(content);
        }
        if (rating != null) {
            review.setRating(rating);
        }

        reviewMapper.updateById(review);

        // 返回更新后的评论BO
        ReviewBO reviewBO = new ReviewBO()
                .setReviewId(review.getReviewId())
                .setContent(review.getContent())
                .setRating(review.getRating())
                .setCreatedAt(review.getCreatedAt());

        // 获取评论作者信息
        reviewBO.setAuthor(userService.getUserProfileById(userId));

        return Optional.of(reviewBO);
    }

    @Override
    public boolean deleteReview(Long reviewId, Long userId) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null || !review.getUserId().equals(userId)) {
            return false;
        }

        return reviewMapper.deleteById(reviewId) > 0;
    }
}
