package dev.sepler.bytekeeper.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import dev.sepler.bytekeeper.accessor.FileAccessor;
import dev.sepler.bytekeeper.model.ByteFile;
import dev.sepler.bytekeeper.model.Identifier;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ByteKeeperServiceTest {

    @Mock
    private FileAccessor fileAccessor;

    @InjectMocks
    private ByteKeeperService byteKeeperService;

    @Mock
    private FileSystemResource fileSystemResource;

    @Test
    public void downloadFile_worksOk() {
        doReturn(fileSystemResource).when(fileAccessor).retrieve("id");

        FileSystemResource fileSystemResource = byteKeeperService.downloadFile("id");

        Assertions.assertNotNull(fileSystemResource);
    }

    @Test
    public void getByteFile_worksOk() {
        Identifier id = new Identifier().withValue("id");

        ByteFile byteFile = byteKeeperService.getByteFile(id);

        Assertions.assertNotNull(byteFile);
    }

    @Test
    public void getByteFiles_worksOk() {
        List<Identifier> ids = Arrays.asList(new Identifier(), new Identifier());

        List<ByteFile> byteFiles = byteKeeperService.getByteFiles(ids);

        Assertions.assertEquals(2, byteFiles.size());
    }

    @Test
    public void putFile_worksOk() {
        MultipartFile multipartFile = new MockMultipartFile("file", "content".getBytes());

        doNothing().when(fileAccessor).save(anyString(), eq(multipartFile));

        Identifier id = byteKeeperService.putFile(multipartFile);

        Assertions.assertTrue(id.getValue().length() > 0);
    }
}
