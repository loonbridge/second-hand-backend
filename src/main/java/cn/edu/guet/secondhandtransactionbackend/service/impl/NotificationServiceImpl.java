package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.Notification;
import cn.edu.guet.secondhandtransactionbackend.service.NotificationService;
import cn.edu.guet.secondhandtransactionbackend.mapper.NotificationMapper;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【notification(消息实体表)】的数据库操作Service实现
* @createDate 2025-07-25 18:13:45
*/
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
    implements NotificationService{

}




