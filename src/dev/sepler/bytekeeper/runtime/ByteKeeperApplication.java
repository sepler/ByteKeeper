package dev.sepler.bytekeeper.runtime;

import dev.sepler.bytekeeper.dao.ByteFileRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableMongoRepositories(basePackageClasses = ByteFileRepository.class)
@SpringBootApplication(scanBasePackages = "dev.sepler.bytekeeper")
public class ByteKeeperApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ByteKeeperApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*");
            }
        };
    }
}
