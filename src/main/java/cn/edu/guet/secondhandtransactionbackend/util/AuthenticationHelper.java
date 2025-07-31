package cn.edu.guet.secondhandtransactionbackend.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.Principal;
import java.util.Optional;

@Component
public class AuthenticationHelper {

    private final HttpServletRequest request;

    // 通过构造函数注入请求作用域的HttpServletRequest代理
    @Autowired
    public AuthenticationHelper(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 获取当前已认证用户的ID。
     * 这种方式比直接使用SecurityContextHolder更适合在Web层组件中，因为它更易于测试。
     * @return 如果用户已认证，返回包含用户ID的Optional；否则返回空的Optional。
     */
    public Optional<Long> getCurrentUserId() {
        // 从注入的 request 代理中获取 Principal
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return Optional.empty();
        }

        String userIdStr = principal.getName();
        try {
            return Optional.of(Long.parseLong(userIdStr));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}