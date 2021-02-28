package dev.sepler.bytekeeper.accessor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class FileAccessorTest {

    private static final String TEST_DIRECTORY = "directory/test";

    private static final String TEST_NAME = "name";

    private static FileAccessor fileAccessor;

    @BeforeAll
    public static void before() {
        fileAccessor = spy(new FileAccessor(TEST_DIRECTORY));
    }

    @Test
    public void save_workOk() {
        doNothing().when(fileAccessor).copy(any(MultipartFile.class), any(Path.class));

        fileAccessor.save(TEST_NAME, new MockMultipartFile("file", "content".getBytes()));

        Path expectedPath = Path.of(TEST_DIRECTORY + "/" + TEST_NAME).toAbsolutePath();
        verify(fileAccessor, times(1)).copy(any(MultipartFile.class), eq(expectedPath));
    }

    @Test
    public void retrieve_worksOk() throws FileNotFoundException {
        doReturn(true).when(fileAccessor).exists(any());
        FileSystemResource fileSystemResource = fileAccessor.retrieve("name");
        Assertions.assertNotNull(fileSystemResource);
    }


    @Test
    public void retrieve_notFound_throwException() throws FileNotFoundException {
        doReturn(false).when(fileAccessor).exists(any());
        assertThrows(FileNotFoundException.class, () -> {
            FileSystemResource fileSystemResource = fileAccessor.retrieve("name");
        });
    }

}
