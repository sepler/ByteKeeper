package dev.sepler.bytekeeper.controller;

import dev.sepler.bytekeeper.ByteKeeperApi;
import dev.sepler.bytekeeper.rest.GetFileRequest;
import dev.sepler.bytekeeper.rest.GetFileResponse;
import dev.sepler.bytekeeper.rest.GetFilesRequest;
import dev.sepler.bytekeeper.rest.GetFilesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ByteKeeperController implements ByteKeeperApi {

    @Override
    public ResponseEntity<GetFileResponse> getFile(@Valid GetFileRequest getFileRequest) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GetFilesResponse> getFiles(@Valid GetFilesRequest getFilesRequest) {
        return ResponseEntity.noContent().build();
    }
}
