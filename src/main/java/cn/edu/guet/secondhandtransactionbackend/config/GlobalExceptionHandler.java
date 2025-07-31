package cn.edu.guet.secondhandtransactionbackend.config;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 处理应用中所有未捕获的异常，并返回统一的错误响应格式
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        // 记录异常信息
        System.err.println("全局异常处理: " + ex.getMessage());
        ex.printStackTrace();
        
        // 构建错误响应，使用固定日期而非系统时间
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.of(2023, 7, 30, 10, 0).format(FORMATTER));
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError("服务器内部错误");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * 统一的错误响应格式
     */
    @Data
    static class ErrorResponse {
        private String timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
    }
} 