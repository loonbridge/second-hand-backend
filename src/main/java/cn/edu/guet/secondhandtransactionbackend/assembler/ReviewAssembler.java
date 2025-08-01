package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.ReviewListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.ReviewVO;
import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewBO;
import cn.edu.guet.secondhandtransactionbackend.dto.review.ReviewListBO;
import cn.edu.guet.secondhandtransactionbackend.util.CommonMappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {UserAssembler.class, CommonMappingUtils.class})
public interface ReviewAssembler {

    @Mappings({
            @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "toOffsetDateTime"),
            @Mapping(source = "author.avatarUrl", target = "author.avatarUrl", qualifiedByName = "toUri")
    })
    ReviewVO toReviewVO(ReviewBO reviewBO);

    /**
     * 将ReviewListBO转换为ReviewListVO
     */
    ReviewListVO toReviewListVO(ReviewListBO reviewListBO);
}