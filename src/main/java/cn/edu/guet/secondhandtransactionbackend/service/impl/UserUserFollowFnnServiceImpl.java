package cn.edu.guet.secondhandtransactionbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.guet.secondhandtransactionbackend.entity.UserUserFollowFnn;
import cn.edu.guet.secondhandtransactionbackend.service.UserUserFollowFnnService;
import cn.edu.guet.secondhandtransactionbackend.mapper.UserUserFollowFnnMapper;
import org.springframework.stereotype.Service;

/**
* @author Sammy
* @description 针对表【user_user_follow_fnn([行为]用户关注用户(n-n)关系表)】的数据库操作Service实现
* @createDate 2025-07-25 18:08:42
*/
@Service
public class UserUserFollowFnnServiceImpl extends ServiceImpl<UserUserFollowFnnMapper, UserUserFollowFnn>
    implements UserUserFollowFnnService{

}




