package dev.sepler.bytekeeper.controller;

import static dev.sepler.bytekeeper.util.TestConstants.TEST_BYTE_FILE;
import static dev.sepler.bytekeeper.util.TestConstants.TEST_DELETE_TOKEN;
import static dev.sepler.bytekeeper.util.TestConstants.TEST_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import dev.sepler.bytekeeper.exception.ByteFileNotFoundException;
import dev.sepler.bytekeeper.exception.ErrorRequestException;
import dev.sepler.bytekeeper.rest.DeleteByteFileRequest;
import dev.sepler.bytekeeper.rest.DeleteByteFileResponse;
import dev.sepler.bytekeeper.rest.GetByteFileRequest;
import dev.sepler.bytekeeper.rest.GetByteFileResponse;
import dev.sepler.bytekeeper.rest.GetByteFilesRequest;
import dev.sepler.bytekeeper.rest.GetByteFilesResponse;
import dev.sepler.bytekeeper.rest.Identifier;
import dev.sepler.bytekeeper.rest.PutFileRequest;
import dev.sepler.bytekeeper.rest.PutFileResponse;
import dev.sepler.bytekeeper.service.ByteKeeperService;
import java.io.FileNotFoundException;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
    public void deleteByteFile_withValidRequest_thenOk() {
        DeleteByteFileRequest request = new DeleteByteFileRequest()
                .withId(new Identifier().withValue(TEST_ID))
                .withToken(TEST_DELETE_TOKEN);

        doNothing().when(byteKeeperService).deleteByteFile(any(), anyString());

        ResponseEntity<DeleteByteFileResponse> responseEntity = byteKeeperController.deleteByteFile(request);

        assertEquals(TEST_ID, responseEntity.getBody().getId().getValue());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteByteFile_withInvalidRequest_thenThrowBadRequestException() {
        DeleteByteFileRequest request = new DeleteByteFileRequest();

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.deleteByteFile(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void deleteByteFile_withEmptyToken_thenThrowBadRequestException() {
        DeleteByteFileRequest request = new DeleteByteFileRequest()
                .withId(new Identifier().withValue(TEST_ID))
                .withToken("");

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.deleteByteFile(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void deleteByteFile_withNotFound_thenThrowNotFoundException() {
        DeleteByteFileRequest request = new DeleteByteFileRequest()
                .withId(new Identifier().withValue(TEST_ID))
                .withToken(TEST_DELETE_TOKEN);

        doThrow(new ByteFileNotFoundException(TEST_ID)).when(byteKeeperService).deleteByteFile(any(), anyString());

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.deleteByteFile(request);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void deleteByteFile_withUnsupportedOp_thenThrowBadRequestException() {
        DeleteByteFileRequest request = new DeleteByteFileRequest()
                .withId(new Identifier().withValue(TEST_ID))
                .withToken(TEST_DELETE_TOKEN);

        doThrow(new UnsupportedOperationException()).when(byteKeeperService).deleteByteFile(any(), anyString());

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.deleteByteFile(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void deleteByteFile_withException_thenThrowInternalErrorException() {
        DeleteByteFileRequest request = new DeleteByteFileRequest()
                .withId(new Identifier().withValue(TEST_ID))
                .withToken(TEST_DELETE_TOKEN);

        doThrow(new RuntimeException()).when(byteKeeperService).deleteByteFile(any(), anyString());

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.deleteByteFile(request);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

    @Test
    public void downloadFile_withValidRequest_thenOk() throws FileNotFoundException {
        doReturn(TEST_BYTE_FILE).when(byteKeeperService).getByteFile(any());
        doReturn(fileSystemResource).when(byteKeeperService).downloadFile(any());

        ResponseEntity<Resource> responseEntity = byteKeeperController.downloadFile(TEST_ID);
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        Assertions.assertTrue(responseHeaders.get("Content-Disposition")
                .contains("attachment; filename=" + TEST_BYTE_FILE.getName()));
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void downloadFile_withInvalidRequest_thenThrowBadRequestException() {
        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.downloadFile("");
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void downloadFile_withNotFound_thenThrowNotFoundException() {
        doThrow(new ByteFileNotFoundException(TEST_ID)).when(byteKeeperService).getByteFile(any());

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.downloadFile("file");
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void downloadFile_withRuntimeException_thenThrowInternalErrorException() {
        doThrow(new RuntimeException()).when(byteKeeperService).getByteFile(any());

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.downloadFile("file");
        });
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

    @Test
    public void getByteFile_withValidRequest_thenOk() {
        GetByteFileRequest getByteFileRequest = new GetByteFileRequest()
                .withId(new Identifier().withValue(TEST_ID));

        doReturn(TEST_BYTE_FILE).when(byteKeeperService).getByteFile(any());

        ResponseEntity<GetByteFileResponse> responseEntity = byteKeeperController.getByteFile(getByteFileRequest);
        Assertions.assertNotNull(responseEntity.getBody().getByteFile());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getByteFile_withInvalidRequest_thenThrowBadRequestException() {
        GetByteFileRequest getByteFileRequest = new GetByteFileRequest();

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.getByteFile(getByteFileRequest);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void getByteFile_withNotFound_thenThrowNotFoundException() {
        GetByteFileRequest getByteFileRequest = new GetByteFileRequest()
                .withId(new Identifier().withValue(TEST_ID));
        doThrow(new ByteFileNotFoundException(TEST_ID)).when(byteKeeperService).getByteFile(any());

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.getByteFile(getByteFileRequest);
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    public void getByteFile_withRuntimeException_thenThrowInternalErrorException() {
        GetByteFileRequest getByteFileRequest = new GetByteFileRequest()
                .withId(new Identifier().withValue(TEST_ID));
        doThrow(new RuntimeException()).when(byteKeeperService).getByteFile(any());

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.getByteFile(getByteFileRequest);
        });
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

    @Test
    public void getByteFiles_withValidRequest_thenOk() {
        GetByteFilesRequest getFilesRequest = new GetByteFilesRequest()
                .withIds(Arrays.asList(new Identifier().withValue("id1"), new Identifier().withValue("id2")));

        doReturn(Arrays.asList(new dev.sepler.bytekeeper.model.ByteFile(),
                new dev.sepler.bytekeeper.model.ByteFile())).when(byteKeeperService).getByteFiles(anyList());

        ResponseEntity<GetByteFilesResponse> responseEntity = byteKeeperController.getByteFiles(getFilesRequest);
        Assertions.assertFalse(responseEntity.getBody().getByteFiles().isEmpty());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getByteFiles_withInvalidRequest_thenThrowBadRequestException() {
        GetByteFilesRequest getFilesRequest = new GetByteFilesRequest();

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.getByteFiles(getFilesRequest);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void getByteFiles_withRuntimeException_thenThrowInternalErrorException() {
        GetByteFilesRequest getFilesRequest = new GetByteFilesRequest()
                .withIds(Arrays.asList(new Identifier().withValue("id1"), new Identifier().withValue("id2")));

        doThrow(new RuntimeException()).when(byteKeeperService).getByteFiles(any());
        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.getByteFiles(getFilesRequest);
        });
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
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

    @Test
    public void putFile_withRuntimeException_thenThrowInternalErrorException() {
        PutFileRequest putFileRequest = new PutFileRequest();
        MultipartFile multipartFile = new MockMultipartFile("file", "content".getBytes());
        doThrow(new RuntimeException()).when(byteKeeperService).putFile(any());

        ErrorRequestException exception = assertThrows(ErrorRequestException.class, () -> {
            byteKeeperController.putFile(multipartFile, putFileRequest);
        });
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

}
