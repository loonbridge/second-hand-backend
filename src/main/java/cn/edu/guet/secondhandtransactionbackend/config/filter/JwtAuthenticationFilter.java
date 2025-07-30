package cn.edu.guet.secondhandtransactionbackend.config.filter;


import cn.edu.guet.secondhandtransactionbackend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;


    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");


        if (authHeader ==null   ||!authHeader.startsWith("Bearer ")) {
            // 没有 token，放行给下一个过滤器
            filterChain.doFilter(request, response);
            return;
        }

        //有token

        String jwt = authHeader.substring(7);// 去掉 "Bearer " 前缀


        String userId =  jwtUtil.getUserIdFromToken(jwt);


        // 如果成功从Token解析出userId，并且当前用户尚未被认证

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);


            //创建登录凭证，保证下次不会再进行认证
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null
                    ,
                    userDetails.getAuthorities()
            );


            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //设置到SecurityContextHolder中

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 继续过滤链
        filterChain.doFilter(request, response);
    }
}
