package cn.edu.guet.secondhandtransactionbackend.controller.notification;

import cn.edu.guet.secondhandtransactionbackend.controller.api.NotificationsApi;
import cn.edu.guet.secondhandtransactionbackend.dto.NotificationVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class NotificationController   implements NotificationsApi {
    @Override
    public ResponseEntity<List<NotificationVO>> notificationsGet(String type) {
        return null;
    }
}
