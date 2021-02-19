package dev.sepler.bytekeeper.runtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("dev.sepler.ByteKeeper")
@SpringBootApplication
public class ByteKeeperApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ByteKeeperApplication.class, args);
    }

}
