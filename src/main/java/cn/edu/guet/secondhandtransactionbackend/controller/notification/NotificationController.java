package cn.edu.guet.secondhandtransactionbackend.controller.notification;

import cn.edu.guet.secondhandtransactionbackend.controller.api.NotificationsApi;
import cn.edu.guet.secondhandtransactionbackend.dto.NotificationListVO;
import org.springframework.http.ResponseEntity;

public class NotificationController   implements NotificationsApi {

    @Override
    public ResponseEntity<NotificationListVO> notificationsGet(String type, Integer page, Integer size) {
        return null;
    }
}
