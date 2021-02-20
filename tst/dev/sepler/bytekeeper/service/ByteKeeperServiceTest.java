package dev.sepler.bytekeeper.service;

import dev.sepler.bytekeeper.rest.ByteFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ByteKeeperServiceTest {

    @InjectMocks
    private ByteKeeperService byteKeeperService;

    @Test
    public void getFile_worksOk() {
        String id = "id";
        ByteFile byteFile = byteKeeperService.getFile(id);
        Assertions.assertNotNull(byteFile);
    }
}
