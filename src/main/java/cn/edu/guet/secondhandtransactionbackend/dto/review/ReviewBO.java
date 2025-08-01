package cn.edu.guet.secondhandtransactionbackend.dto.review;

import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link cn.edu.guet.secondhandtransactionbackend.entity.Review}
 */
@Data
@Accessors(chain = true)
public class ReviewBO implements Serializable {
    private Long reviewId;
    private String content;
    private LocalDateTime createdAt;

    private Integer rating;
    private UserProfileBO author;
}