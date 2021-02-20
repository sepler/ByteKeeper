package dev.sepler.bytekeeper.service;

import dev.sepler.bytekeeper.rest.ByteFile;
import dev.sepler.bytekeeper.rest.Identifier;
import java.util.Arrays;
import java.util.List;
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
        Identifier id = new Identifier().withValue("id");

        ByteFile byteFile = byteKeeperService.getFile(id);

        Assertions.assertEquals("id", byteFile.getId().getValue());
    }

    @Test
    public void getFiles_worksOk() {
        List<Identifier> ids = Arrays.asList(new Identifier(), new Identifier());

        List<ByteFile> byteFiles = byteKeeperService.getFiles(ids);

        Assertions.assertEquals(2, byteFiles.size());
    }

    @Test
    public void putFile_worksOk() {
        ByteFile byteFile = new ByteFile().withId(new Identifier().withValue("id"));

        Identifier id = byteKeeperService.putFile(byteFile);

        Assertions.assertEquals("id", id.getValue());
    }
}
