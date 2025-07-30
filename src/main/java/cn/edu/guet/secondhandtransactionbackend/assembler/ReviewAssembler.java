package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.ReviewVO;
import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


@Mapper(componentModel = "spring", uses = {UserAssembler.class})
public interface ReviewAssembler {



  default LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
    return offsetDateTime == null ? null : offsetDateTime.toLocalDateTime();
  }

  default OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
    return localDateTime == null ? null : localDateTime.atOffset(ZoneOffset.ofHours(8)); // 按需调整时区
  }

  // 不需要显式的 @Mapping，MapStruct 会自动处理
  ReviewVO toReviewVO(ReviewBO reviewBO);
}