package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.ReviewVO;
import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring", uses = {UserAssembler.class})
public interface ReviewAssembler {

  // 不需要显式的 @Mapping，MapStruct 会自动处理
  ReviewVO toReviewVO(ReviewBO reviewBO);
}