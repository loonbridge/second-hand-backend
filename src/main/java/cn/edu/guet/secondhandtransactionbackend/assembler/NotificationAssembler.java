package cn.edu.guet.secondhandtransactionbackend.assembler;

import cn.edu.guet.secondhandtransactionbackend.dto.NotificationListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.NotificationVO;
import cn.edu.guet.secondhandtransactionbackend.dto.notification.NotificationBO;
import cn.edu.guet.secondhandtransactionbackend.dto.notification.NotificationListBO;
import cn.edu.guet.secondhandtransactionbackend.util.CommonMappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {CommonMappingUtils.class})
public interface NotificationAssembler {

    /**
     * 将NotificationListBO转换为NotificationListVO
     */
    NotificationListVO toNotificationListVO(NotificationListBO notificationListBO);

    /**
     * 将NotificationBO转换为NotificationVO
     */
    @Mappings({
            @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "toOffsetDateTime"),
            @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "toOffsetDateTime")
    })
    NotificationVO toNotificationVO(NotificationBO notificationBO);
}
