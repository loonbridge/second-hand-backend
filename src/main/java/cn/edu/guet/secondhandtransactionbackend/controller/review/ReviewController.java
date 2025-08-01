package cn.edu.guet.secondhandtransactionbackend.controller.review;

import cn.edu.guet.secondhandtransactionbackend.assembler.ReviewAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.ReviewsApi;
import cn.edu.guet.secondhandtransactionbackend.dto.CreateReviewRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.ReviewListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ReviewVO;
import cn.edu.guet.secondhandtransactionbackend.dto.UpdateReviewRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewListBO;
import cn.edu.guet.secondhandtransactionbackend.service.ReviewService;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ReviewController implements ReviewsApi {

    private final ReviewService reviewService;
    private final AuthenticationHelper authenticationHelper;
    private final ReviewAssembler reviewAssembler;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            AuthenticationHelper authenticationHelper,
                            ReviewAssembler reviewAssembler) {
        this.reviewService = reviewService;
        this.authenticationHelper = authenticationHelper;
        this.reviewAssembler = reviewAssembler;
    }

    @Override
    public ResponseEntity<ReviewListVO> productsProductIdReviewsGet(String productId, Integer page, Integer size) {
        try {
            Long productIdLong = Long.parseLong(productId);

            // 设置默认值
            if (page == null || page < 1) {
                page = 1;
            }
            if (size == null || size < 1) {
                size = 10;
            }

            ReviewListBO reviewListBO = reviewService.getReviewsByProductIdWithPagination(productIdLong, page, size);
            ReviewListVO reviewListVO = reviewAssembler.toReviewListVO(reviewListBO);

            return ResponseEntity.ok(reviewListVO);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<ReviewVO> productsProductIdReviewsPost(String productId, CreateReviewRequest createReviewRequest) {
        // 获取当前用户ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long productIdLong = Long.parseLong(productId);

            // 验证请求参数
            if (createReviewRequest.getContent() == null || createReviewRequest.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            if (createReviewRequest.getRating() == null ||
                    createReviewRequest.getRating() < 1 ||
                    createReviewRequest.getRating() > 5) {
                return ResponseEntity.badRequest().build();
            }

            ReviewBO reviewBO = reviewService.createReview(
                    productIdLong,
                    currentUserId.get(),
                    createReviewRequest.getContent().trim(),
                    createReviewRequest.getRating()
            );

            ReviewVO reviewVO = reviewAssembler.toReviewVO(reviewBO);
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewVO);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> productsProductIdReviewsReviewIdDelete(String productId, String reviewId) {
        // 获取当前用户ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long reviewIdLong = Long.parseLong(reviewId);

            boolean deleted = reviewService.deleteReview(reviewIdLong, currentUserId.get());

            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<ReviewVO> productsProductIdReviewsReviewIdGet(String productId, String reviewId) {
        try {
            Long reviewIdLong = Long.parseLong(reviewId);

            Optional<ReviewBO> reviewBO = reviewService.getReviewById(reviewIdLong);

            if (reviewBO.isPresent()) {
                ReviewVO reviewVO = reviewAssembler.toReviewVO(reviewBO.get());
                return ResponseEntity.ok(reviewVO);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<ReviewVO> productsProductIdReviewsReviewIdPatch(String productId, String reviewId, UpdateReviewRequest updateReviewRequest) {
        // 获取当前用户ID
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long reviewIdLong = Long.parseLong(reviewId);

            // 验证请求参数
            if (updateReviewRequest.getRating() != null &&
                    (updateReviewRequest.getRating() < 1 || updateReviewRequest.getRating() > 5)) {
                return ResponseEntity.badRequest().build();
            }

            String content = updateReviewRequest.getContent();
            if (content != null) {
                content = content.trim();
                if (content.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }
            }

            Optional<ReviewBO> updatedReviewBO = reviewService.updateReview(
                    reviewIdLong,
                    currentUserId.get(),
                    content,
                    updateReviewRequest.getRating()
            );

            if (updatedReviewBO.isPresent()) {
                ReviewVO reviewVO = reviewAssembler.toReviewVO(updatedReviewBO.get());
                return ResponseEntity.ok(reviewVO);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
