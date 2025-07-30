package cn.edu.guet.secondhandtransactionbackend.service.impl;

import cn.edu.guet.secondhandtransactionbackend.dto.auth.LoginResponseBO;
import cn.edu.guet.secondhandtransactionbackend.dto.user.UserProfileBO;
import cn.edu.guet.secondhandtransactionbackend.entity.User;
import cn.edu.guet.secondhandtransactionbackend.mapper.UserMapper;
import cn.edu.guet.secondhandtransactionbackend.service.UserService;
import cn.edu.guet.secondhandtransactionbackend.service.WxUserService;
import cn.edu.guet.secondhandtransactionbackend.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WxUserServiceImpl implements WxUserService {

    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${app.miniapp.appId}")
    private String appId;

    @Value("${app.miniapp.secret}")
    private  String appSecret;

  private final UserService userService;

    private final JwtUtil jwtUtil;

    @Autowired
   public WxUserServiceImpl(UserService userService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @Override
    public LoginResponseBO login(String code) {

        //发起请求到微信服务器

        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appId, appSecret, code);


        String wechatResponse = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        String openid;

        String sessionKey;


        try {
            JsonNode jsonNode = objectMapper.readTree(wechatResponse);


            if (jsonNode.has("errcode")&& jsonNode.get("errcode").asInt() !=0) {
                // 如果有错误码，说明请求失败
                String errorMessage = jsonNode.get("errmsg").asText();
                throw new RuntimeException("微信登录失败: " + errorMessage);
            }

            //获取openid和session_key

            openid = jsonNode.get("openid").asText();
            sessionKey = jsonNode.get("session_key").asText();



        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //如果openid是空

        if (openid == null || openid.trim().isEmpty()) {
            throw new RuntimeException("微信登录失败: openid 为空");
        }


        //根据openid数据库用户

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("openid", openid);


        LoginResponseBO loginResponseBO = new LoginResponseBO();


        userService.getOneOpt(queryWrapper)
                .ifPresentOrElse(user -> {
                    //如果用户存在，构造jwt，返回bo对象
                    Long userId = user.getUserId();

                    String    jwt  = jwtUtil.generateToken(userId);


                    UserProfileBO userProfileBO = new UserProfileBO();

                    BeanUtils.copyProperties(user, userProfileBO);


                    loginResponseBO.setJwt(jwt)
                            .setUserProfile(userProfileBO);



                }, () -> {
                    //如果用户不存在，创建新用户

                    User newUser = new User();
                    newUser.setOpenid(openid);

                    //保存在数据库中
                    /*TODO：保存时没有只保存了openid，其他字段需要前端传递，然后再填充，或者修改表，不需要这些只能有前端获取的数据比如nickname，avator等
                    */
                    userService.save(newUser);

                    //获取新用户的ID
                    User savedUser = userService.getOne(queryWrapper);

                    String jwt = jwtUtil.generateToken(savedUser.getUserId());



                    //返回BO对象


                    UserProfileBO userProfileBO = new UserProfileBO();

                    BeanUtils.copyProperties(savedUser, userProfileBO);


                    loginResponseBO.setJwt(jwt)
                            .setUserProfile(userProfileBO);



                });





        return loginResponseBO  ;
    }
}
