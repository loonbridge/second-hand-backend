package cn.edu.guet.secondhandtransactionbackend.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus配置类
 * 配置自动填充处理器
 */
@Configuration
public class MybatisPlusConfig {
    /**
     * 自动填充配置
     * 使用系统当前时间
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            /**
             * 插入时的填充策略
             */
            @Override
            public void insertFill(MetaObject metaObject) {
                // 使用系统当前时间，不再使用固定日期
                LocalDateTime now = LocalDateTime.now();
                
                // 尝试自动填充创建时间和更新时间字段
                // 只有当实体类中存在这些字段并且值为null时才会填充
                this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
                this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);
                
                // 兼容其他可能的时间字段名
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
                
                // 为状态字段设置默认值
                if (metaObject.hasSetter("status")) {
                    Object status = getFieldValByName("status", metaObject);
                    if (status == null) {
                        setFieldValByName("status", "ON_SALE", metaObject);
                    }
                }
            }

            /**
             * 更新时的填充策略
             */
            @Override
            public void updateFill(MetaObject metaObject) {
                // 使用系统当前时间，不再使用固定日期
                LocalDateTime now = LocalDateTime.now();
                
                // 更新时只填充更新时间
                this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, now);
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
            }
        };
    }
} 