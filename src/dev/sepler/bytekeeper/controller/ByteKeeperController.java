package dev.sepler.bytekeeper.controller;

import dev.sepler.bytekeeper.ByteKeeperApi;
import dev.sepler.bytekeeper.rest.GetFileRequest;
import dev.sepler.bytekeeper.rest.GetFileResponse;
import dev.sepler.bytekeeper.rest.GetFilesRequest;
import dev.sepler.bytekeeper.rest.GetFilesResponse;
import dev.sepler.bytekeeper.rest.PutFileRequest;
import dev.sepler.bytekeeper.rest.PutFileResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ByteKeeperController implements ByteKeeperApi {

    @Override
    public ResponseEntity<GetFileResponse> getFile(final @Valid GetFileRequest getFileRequest) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GetFilesResponse> getFiles(final @Valid GetFilesRequest getFilesRequest) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PutFileResponse> putFile(final @Valid PutFileRequest putFileRequest) {
        return ResponseEntity.noContent().build();
    }
}
