package dev.sepler.bytekeeper.runtime;

import dev.sepler.bytekeeper.dao.ByteFileRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = ByteFileRepository.class)
@SpringBootApplication(scanBasePackages = "dev.sepler.bytekeeper")
public class ByteKeeperApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ByteKeeperApplication.class, args);
    }

}
