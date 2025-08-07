// 文件路径: cn.edu.guet.secondhandtransactionbackend.config.SecurityConfig.java
package cn.edu.guet.secondhandtransactionbackend.config;

import cn.edu.guet.secondhandtransactionbackend.config.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//启用方法注解
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Value("${file.access.url}")
    private String fileAccessUrl;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护，因为我们使用的是无状态的JWT
                .csrf(AbstractHttpConfigurer::disable)

                // 配置URL授权规则
                .authorizeHttpRequests(auth -> auth
                        // 明确放行登录接口（根据您的OpenAPI定义，路径是 /auth/login）
                        .requestMatchers("/auth/login").permitAll()
                        // 放行文件访问路径
//                        .requestMatchers(fileAccessUrl + "/**").permitAll()
                        // 为了方便调试，也可以放行swagger文档
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        // 除了上面放行的，其他所有请求都必须经过认证
                                .anyRequest().authenticated()
//                        .anyRequest().permitAll()
                )

                // 设置Session管理为无状态（STATELESS），不使用Session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 将我们自定义的JWT过滤器添加到过滤器链中，
                // 它会在Spring Security的用户名密码认证过滤器之前执行
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                
        return http.build();
    }
}