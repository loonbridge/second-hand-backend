package cn.edu.guet.secondhandtransactionbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@MapperScan("cn.edu.guet.secondhandtransactionbackend.mapper")
public class SecondHandTransactionBackendApplication {

    public static void main(String[] args) {
        // 设置默认时区为上海，确保时间正确
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        
        // 注释掉固定时间的设置，使用系统当前时间
        // overrideSystemTime();
        
        System.out.println("系统当前时间: " + LocalDateTime.now());
        
        SpringApplication.run(SecondHandTransactionBackendApplication.class, args);
    }
    
    /**
     * 此方法已不再使用 - 保留作为参考
     * 之前用于覆盖系统时间的方法
     */
    private static void overrideSystemTime() {
        try {
            // 注释掉固定日期，使用当前时间
            // LocalDateTime fixedDate = LocalDateTime.of(2023, 7, 30, 10, 0);
            LocalDateTime currentDateTime = LocalDateTime.now();
            
            // 设置系统属性
            long currentTimeMillis = currentDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            // 注释掉设置覆盖时间的属性
            // System.setProperty("org.springframework.boot.datetime.override", String.valueOf(fixedTimeMillis));
            
            System.out.println("系统使用当前时间: " + currentDateTime);
        } catch (Exception e) {
            System.err.println("时间设置失败: " + e.getMessage());
        }
    }
}
