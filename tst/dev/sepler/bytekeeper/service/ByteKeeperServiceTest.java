package dev.sepler.bytekeeper.service;

import static dev.sepler.bytekeeper.util.TestConstants.TEST_BYTE_FILE;
import static dev.sepler.bytekeeper.util.TestConstants.TEST_ID;
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
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class ByteKeeperServiceTest {

    @Mock
    private ByteFileRepository byteFileRepository;

    @Mock
    private FileAccessor fileAccessor;

    @InjectMocks
    private ByteKeeperService byteKeeperService;

    @Mock
    private FileSystemResource fileSystemResource;

    @Test
    public void deleteByteFile_worksOk() {
        doNothing().when(fileAccessor).delete(TEST_ID);
        doReturn(Optional.of(TEST_BYTE_FILE)).when(byteFileRepository).findById(Identifier.of(TEST_ID));

        byteKeeperService.deleteByteFile(Identifier.of(TEST_ID), TEST_BYTE_FILE.getDeleteToken());
    }

    @Test
    public void deleteByteFile_isDeleted_throwException() {
        doReturn(Optional.of(TEST_BYTE_FILE.withDeleted(true))).when(byteFileRepository).findById(Identifier.of(TEST_ID));

        assertThrows(UnsupportedOperationException.class, () -> {
            byteKeeperService.deleteByteFile(Identifier.of(TEST_ID), TEST_BYTE_FILE.getDeleteToken());
        });
    }

    @Test
    public void deleteByteFile_badToken_throwException() {
        doReturn(Optional.of(TEST_BYTE_FILE.withDeleteToken("foo"))).when(byteFileRepository).findById(Identifier.of(TEST_ID));

        assertThrows(UnsupportedOperationException.class, () -> {
            byteKeeperService.deleteByteFile(Identifier.of(TEST_ID), TEST_BYTE_FILE.getDeleteToken());
        });
    }

    @Test
    public void downloadFile_worksOk() throws FileNotFoundException {
        doReturn(fileSystemResource).when(fileAccessor).retrieve(TEST_ID);

        FileSystemResource fileSystemResource = byteKeeperService.downloadFile(TEST_ID);

        Assertions.assertNotNull(fileSystemResource);
    }

    @Test
    public void downloadFile_notFound_throwException() throws FileNotFoundException {
        doThrow(new FileNotFoundException()).when(fileAccessor).retrieve(TEST_ID);

        assertThrows(FileNotFoundException.class, () -> {
            byteKeeperService.downloadFile(TEST_ID);
        });
    }

    @Test
    public void getByteFile_worksOk() {
        Identifier id = new Identifier().withValue(TEST_ID);

        doReturn(Optional.of(new ByteFile())).when(byteFileRepository).findById(any());

        ByteFile byteFile = byteKeeperService.getByteFile(id);

        verify(byteFileRepository, times(1)).findById(id);
        Assertions.assertNotNull(byteFile);
    }

    @Test
    public void getByteFile_notFound_throwException() {
        Identifier id = new Identifier().withValue(TEST_ID);

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

        doReturn(new ByteFile()).when(byteFileRepository).save(any(ByteFile.class));
        doNothing().when(fileAccessor).save(anyString(), eq(multipartFile));

        ByteFile byteFile = byteKeeperService.putFile(multipartFile);

        verify(byteFileRepository, times(1)).save(any());
        Assertions.assertEquals("origName", byteFile.getName());
        Assertions.assertEquals("content".getBytes().length, byteFile.getSizeInBytes());
        Assertions.assertTrue(byteFile.getId().getValue().length() > 0);
        Assertions.assertTrue(byteFile.getDeleteToken().length() > 0);
    }

}
