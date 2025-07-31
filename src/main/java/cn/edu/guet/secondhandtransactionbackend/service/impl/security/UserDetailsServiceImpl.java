package cn.edu.guet.secondhandtransactionbackend.service.impl.security;

import cn.edu.guet.secondhandtransactionbackend.entity.User;
import cn.edu.guet.secondhandtransactionbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;


    @Autowired
    public  UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    /**
     * Spring Security 调用此方法来获取用户详情
     * @param userId 在我们的场景中，这里的 "username" 参数实际上是 user ID
     * @return UserDetails 对象
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        User user = userMapper.selectById(Long.valueOf(userId));

        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }


        return new  org.springframework.security.core.userdetails.User(
                user.getUserId().toString(), // principal, 通常是用户名或ID
                "",             // credentials, 在JWT模式下为空字符串
                Collections.emptyList() // authorities, 权限列表
        );


    }
}
