package cn.edu.guet.secondhandtransactionbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.edu.guet.secondhandtransactionbackend.mapper")
public class SecondHandTransactionBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondHandTransactionBackendApplication.class, args);
    }

}
