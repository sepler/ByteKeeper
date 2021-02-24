package dev.sepler.bytekeeper.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;

import dev.sepler.bytekeeper.exception.ErrorRequestException;
import dev.sepler.bytekeeper.rest.GetFileRequest;
import dev.sepler.bytekeeper.rest.GetFilesRequest;
import dev.sepler.bytekeeper.rest.GetFilesResponse;
import dev.sepler.bytekeeper.rest.Identifier;
import dev.sepler.bytekeeper.rest.PutFileRequest;
import dev.sepler.bytekeeper.rest.PutFileResponse;
import dev.sepler.bytekeeper.service.ByteKeeperService;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ByteKeeperControllerTest {

    @Mock
    private ByteKeeperService byteKeeperService;

    @InjectMocks
    private ByteKeeperController byteKeeperController;

    @Mock
    private FileSystemResource fileSystemResource;

    @Test
    public void getFile_withValidRequest_thenOk() {
        GetFileRequest getFileRequest = new GetFileRequest().withId(new Identifier().withValue("id"));

        doReturn(fileSystemResource).when(byteKeeperService).getFile(any());

        ResponseEntity<Resource> responseEntity = byteKeeperController.getFile(getFileRequest);
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getFile_withInvalidRequest_thenThrowBadRequestException() {
        GetFileRequest getFileRequest = new GetFileRequest();

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.getFile(getFileRequest);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void getFiles_withValidRequest_thenOk() {
        GetFilesRequest getFilesRequest = new GetFilesRequest()
                .withIds(Arrays.asList(new Identifier().withValue("id1"), new Identifier().withValue("id2")));

        doReturn(Arrays.asList(new dev.sepler.bytekeeper.model.ByteFile(),
                new dev.sepler.bytekeeper.model.ByteFile())).when(byteKeeperService).getFiles(anyList());

        ResponseEntity<GetFilesResponse> responseEntity = byteKeeperController.getFiles(getFilesRequest);
        Assertions.assertFalse(responseEntity.getBody().getByteFiles().isEmpty());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getFiles_withInvalidRequest_thenThrowBadRequestException() {
        GetFilesRequest getFilesRequest = new GetFilesRequest();

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.getFiles(getFilesRequest);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void putFile_withValidRequest_thenOk() {
        PutFileRequest putFileRequest = new PutFileRequest();
        MultipartFile multipartFile = new MockMultipartFile("file", "content".getBytes());

        doReturn(new dev.sepler.bytekeeper.model.Identifier()).when(byteKeeperService).putFile(any());

        ResponseEntity<PutFileResponse> responseEntity = byteKeeperController.putFile(multipartFile, putFileRequest);
        Assertions.assertNotNull(responseEntity.getBody().getId());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void putFile_withInvalidRequest_thenThrowBadRequestException() {
        PutFileRequest putFileRequest = new PutFileRequest();

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.putFile(null, putFileRequest);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

}
