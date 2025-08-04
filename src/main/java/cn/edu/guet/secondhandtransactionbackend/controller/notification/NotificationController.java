package cn.edu.guet.secondhandtransactionbackend.controller.notification;

import cn.edu.guet.secondhandtransactionbackend.assembler.NotificationAssembler;
import cn.edu.guet.secondhandtransactionbackend.controller.api.NotificationsApi;
import cn.edu.guet.secondhandtransactionbackend.dto.DeleteNotificationsRequest;
import cn.edu.guet.secondhandtransactionbackend.dto.NotificationListVO;
import cn.edu.guet.secondhandtransactionbackend.dto.notification.NotificationListBO;
import cn.edu.guet.secondhandtransactionbackend.service.NotificationService;
import cn.edu.guet.secondhandtransactionbackend.util.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class NotificationController implements NotificationsApi {

    private final NotificationService notificationService;
    private final AuthenticationHelper authenticationHelper;
    private final NotificationAssembler notificationAssembler;

    @Autowired
    public NotificationController(NotificationService notificationService,
                                  AuthenticationHelper authenticationHelper,
                                  NotificationAssembler notificationAssembler) {
        this.notificationService = notificationService;
        this.authenticationHelper = authenticationHelper;
        this.notificationAssembler = notificationAssembler;
    }

    @Override
    public ResponseEntity<Void> notificationsDeleteBatchPost(DeleteNotificationsRequest deleteNotificationsRequest) {
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        notificationService.deleteBatchNotifications(
                deleteNotificationsRequest.getNotificationIds(),
                currentUserId.get()
        );

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<NotificationListVO> notificationsGet(String type, Integer page, Integer size) {
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        NotificationListBO notifications = notificationService.getNotifications(
                type,
                page != null ? page : 1,
                size != null ? size : 10,
                currentUserId.get()
        );

        NotificationListVO notificationListVO = notificationAssembler.toNotificationListVO(notifications);

        return ResponseEntity.ok(notificationListVO);
    }

    @Override
    public ResponseEntity<Void> notificationsIdDelete(String id) {
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean deleted = notificationService.deleteNotification(id, currentUserId.get());

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> notificationsIdReadPost(String id) {
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean marked = notificationService.markAsRead(id, currentUserId.get());

        if (marked) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> notificationsReadAllPost() {
        Optional<Long> currentUserId = authenticationHelper.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        notificationService.markAllAsRead(currentUserId.get());

        return ResponseEntity.noContent().build();
    }
}
