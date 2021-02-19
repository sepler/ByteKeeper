package dev.sepler.bytekeeper.controller;

import dev.sepler.bytekeeper.rest.GetFileRequest;
import dev.sepler.bytekeeper.rest.GetFileResponse;
import dev.sepler.bytekeeper.rest.GetFilesRequest;
import dev.sepler.bytekeeper.rest.GetFilesResponse;
import dev.sepler.bytekeeper.rest.PutFileRequest;
import dev.sepler.bytekeeper.rest.PutFileResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class ByteKeeperControllerTest {

    private final ByteKeeperController byteKeeperController;

    public ByteKeeperControllerTest() {
        byteKeeperController = new ByteKeeperController();
    }

    @Test
    public void getFile_worksOk() {
        GetFileRequest getFileRequest = new GetFileRequest();
        ResponseEntity<GetFileResponse> responseEntity = byteKeeperController.getFile(getFileRequest);
        Assertions.assertEquals(204, responseEntity.getStatusCode().value());
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
