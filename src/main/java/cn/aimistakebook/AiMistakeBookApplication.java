package cn.aimistakebook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类 —— 整个 Spring Boot 项目的入口
 *
 * @MapperScan("cn.aimistakebook.mapper") 告诉 MyBatis-Plus：
 * "去这个包下面找所有的 Mapper 接口，自动帮它们创建实例"
 */
@SpringBootApplication
@MapperScan("cn.aimistakebook.mapper")
public class AiMistakeBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiMistakeBookApplication.class, args);
    }

}
