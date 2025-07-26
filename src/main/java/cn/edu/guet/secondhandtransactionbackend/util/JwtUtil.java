package cn.edu.guet.secondhandtransactionbackend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private final String secretKey = "your-secret-key"; // 替换为你的密钥

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    private   Algorithm algorithm;



    @PostConstruct
    public  void init() {
        // 初始化JWT算法
        algorithm = Algorithm.HMAC256(secretKey);
    }



    /*
    * 为指定用户ID生成Token令牌
    *@param userId 用户ID
    * @return JWT Token字符串
    * */


    /*
    * JWT 三部分组成
    * Header（头部）: 指定签名算法和令牌类型
    * Payload（负载）: 包含声明（claims），如用户信息、过期时间等
    * Signature（签名）: 用于验证令牌的真实性和完整性
    * */
    public   String generateToken(Long userId) {
        //有效期
        long expirationTime = 1000 * 60 * 60 * 24; // 24小时
        return JWT.create()

                .withSubject(String.valueOf(userId)) // 设置主题为用户ID
                .withClaim("userId", userId) // 将用户ID存储在自定义的claim中
                .withIssuedAt(Instant.now()) // 设置令牌的签发时间
                //设置有效期
                .withExpiresAt(new Date(System.currentTimeMillis() + this.expirationTime)) //
                .sign(this.algorithm); // 使用HMAC256算法签名


    }


    /*
    * 验证token有效性
    * @param token JWT Token字符串
    * @return 解码后的对象
    * @throws Exception 如果验证失败或解析错误

     */


    public  DecodedJWT verifyToken(String token) throws JWTVerificationException {

    return    JWT.require(this.algorithm)
                .build().verify(token);// 验证Token的有效性

    }


    /**
     * 从Token中获取用户ID。
     * 这是一个便捷方法，它会先验证Token，然后提取用户ID。
     * @param token JWT Token字符串
     * @return 用户ID (Long)
     * @throws JWTVerificationException 如果Token无效或无法解析出用户ID
     */
    public  Long getUserIdFromToken(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = verifyToken(token);
        // 从自定义claim中获取userId，更可靠
        return decodedJWT.getClaim("userId").asLong();
    }



}


