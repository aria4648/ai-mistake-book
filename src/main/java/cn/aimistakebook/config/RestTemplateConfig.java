package cn.aimistakebook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 配置类
 *
 * 为什么要写这个？
 *   Spring Boot 虽然自带 RestTemplate，但不会自动帮你 new，
 *   需要用 @Bean 告诉 Spring："给我造一个 RestTemplate 放在容器里"
 *
 *   之后就可以 @Autowired RestTemplate 来用了
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
