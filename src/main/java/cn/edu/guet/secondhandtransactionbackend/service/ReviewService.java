package cn.edu.guet.secondhandtransactionbackend.service;

import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewListBO;
import cn.edu.guet.secondhandtransactionbackend.entity.Review;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author Sammy
* @description 针对表【review(评论实体表)】的数据库操作Service
* @createDate 2025-07-25 18:08:42
*/
public interface ReviewService extends IService<Review> {

    List<ReviewBO> getReviewsByProductId(Long productId);

    /**
     * 分页获取商品评论列表
     *
     * @param productId 商品ID
     * @param page      页码
     * @param size      每页大小
     * @return 评论列表BO
     */
    ReviewListBO getReviewsByProductIdWithPagination(Long productId, Integer page, Integer size);

    /**
     * 根据评论ID获取评论详情
     *
     * @param reviewId 评论ID
     * @return 评论BO
     */
    Optional<ReviewBO> getReviewById(Long reviewId);

    /**
     * 创建评论
     *
     * @param productId 商品ID
     * @param userId    用户ID
     * @param content   评论内容
     * @param rating    评分
     * @return 创建的评论BO
     */
    ReviewBO createReview(Long productId, Long userId, String content, Integer rating);

    /**
     * 更新评论
     *
     * @param reviewId 评论ID
     * @param userId   当前用户ID
     * @param content  新的评论内容
     * @param rating   新的评分
     * @return 更新后的评论BO
     */
    Optional<ReviewBO> updateReview(Long reviewId, Long userId, String content, Integer rating);

    /**
     * 删除评论
     *
     * @param reviewId 评论ID
     * @param userId   当前用户ID
     * @return 是否删除成功
     */
    boolean deleteReview(Long reviewId, Long userId);
}
