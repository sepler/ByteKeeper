package dev.sepler.bytekeeper.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import dev.sepler.bytekeeper.accessor.FileAccessor;
import dev.sepler.bytekeeper.dao.ByteFileRepository;
import dev.sepler.bytekeeper.exception.ByteFileNotFoundException;
import dev.sepler.bytekeeper.model.ByteFile;
import dev.sepler.bytekeeper.model.Identifier;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ByteKeeperServiceTest {

    @Mock
    private ByteFileRepository byteFileRepository;

    @Mock
    private FileAccessor fileAccessor;

    @InjectMocks
    private ByteKeeperService byteKeeperService;

    @Captor
    private ArgumentCaptor<ByteFile> byteFileArgumentCaptor;

    @Mock
    private FileSystemResource fileSystemResource;

    @Test
    public void downloadFile_worksOk() throws FileNotFoundException {
        doReturn(fileSystemResource).when(fileAccessor).retrieve("id");

        FileSystemResource fileSystemResource = byteKeeperService.downloadFile("id");

        Assertions.assertNotNull(fileSystemResource);
    }

    @Test
    public void downloadFile_notFound_throwException() throws FileNotFoundException {
        doThrow(new FileNotFoundException()).when(fileAccessor).retrieve("id");

        assertThrows(FileNotFoundException.class, () -> {
            byteKeeperService.downloadFile("id");
        });
    }

    @Test
    public void getByteFile_worksOk() {
        Identifier id = new Identifier().withValue("id");

        doReturn(Optional.of(new ByteFile())).when(byteFileRepository).findById(any());

        ByteFile byteFile = byteKeeperService.getByteFile(id);

        verify(byteFileRepository, times(1)).findById(id);
        Assertions.assertNotNull(byteFile);
    }

    @Test
    public void getByteFile_notFound_throwException() {
        Identifier id = new Identifier().withValue("id");

        doReturn(Optional.empty()).when(byteFileRepository).findById(id);

        assertThrows(ByteFileNotFoundException.class, () -> {
            byteKeeperService.getByteFile(id);
        });
    }

    @Test
    public void getByteFiles_worksOk() {
        List<Identifier> ids = Arrays.asList(new Identifier(), new Identifier());

        doReturn(Arrays.asList(new ByteFile(), new ByteFile())).when(byteFileRepository).findAllById(eq(ids));

        List<ByteFile> byteFiles = byteKeeperService.getByteFiles(ids);

        verify(byteFileRepository, times(1)).findAllById(ids);
        Assertions.assertEquals(2, byteFiles.size());
    }

    @Test
    public void putFile_worksOk() {
        MultipartFile multipartFile = new MockMultipartFile("file", "origName", null, "content".getBytes());

        doReturn(new ByteFile()).when(byteFileRepository).save(byteFileArgumentCaptor.capture());
        doNothing().when(fileAccessor).save(anyString(), eq(multipartFile));

        Identifier id = byteKeeperService.putFile(multipartFile);

        Assertions.assertEquals("origName", byteFileArgumentCaptor.getValue().getName());
        Assertions.assertEquals("content".getBytes().length, byteFileArgumentCaptor.getValue().getSizeInBytes());
        verify(byteFileRepository, times(1)).save(any());
        Assertions.assertTrue(id.getValue().length() > 0);
    }
}
