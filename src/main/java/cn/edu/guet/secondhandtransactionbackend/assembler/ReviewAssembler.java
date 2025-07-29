package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.ReviewVO;
import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewAssembler {



  ReviewVO toReviewVO(ReviewBO reviewBO)
      ;


}
