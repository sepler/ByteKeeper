package dev.sepler.bytekeeper.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import dev.sepler.bytekeeper.rest.ByteFile;
import dev.sepler.bytekeeper.rest.GetFileRequest;
import dev.sepler.bytekeeper.rest.GetFileResponse;
import dev.sepler.bytekeeper.rest.GetFilesRequest;
import dev.sepler.bytekeeper.rest.GetFilesResponse;
import dev.sepler.bytekeeper.rest.Identifier;
import dev.sepler.bytekeeper.rest.PutFileRequest;
import dev.sepler.bytekeeper.rest.PutFileResponse;
import dev.sepler.bytekeeper.service.ByteKeeperService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ByteKeeperControllerTest {

    @Mock
    private ByteKeeperService byteKeeperService;

    @InjectMocks
    private ByteKeeperController byteKeeperController;

    @Test
    public void getFile_withValidRequest_thenReturn200() {
        GetFileRequest getFileRequest = new GetFileRequest().withId(new Identifier().withValue("id"));

        doReturn(new ByteFile()).when(byteKeeperService).getFile(anyString());

        ResponseEntity<GetFileResponse> responseEntity = byteKeeperController.getFile(getFileRequest);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody().getByteFile());
    }

    @Test
    public void getFile_withInvalidRequest_thenReturn400() {
        GetFileRequest getFileRequest = new GetFileRequest();

        ResponseEntity<GetFileResponse> responseEntity = byteKeeperController.getFile(getFileRequest);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getFiles_worksOk() {
        GetFilesRequest getFilesRequest = new GetFilesRequest();
        ResponseEntity<GetFilesResponse> responseEntity = byteKeeperController.getFiles(getFilesRequest);
        Assertions.assertEquals(204, responseEntity.getStatusCode().value());
    }

    @Test
    public void putFile_worksOk() {
        PutFileRequest putFileRequest = new PutFileRequest();
        ResponseEntity<PutFileResponse> responseEntity = byteKeeperController.putFile(putFileRequest);
        Assertions.assertEquals(204, responseEntity.getStatusCode().value());
    }

}
