package dev.sepler.bytekeeper.runtime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "spring.data.mongodb.uri=mongodb://localhost:27017/" })
class ByteKeeperApplicationTest {

    @Test
    void contextLoads() {
    }

}
