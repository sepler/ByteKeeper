package dev.sepler.bytekeeper.activity;

import dev.sepler.bytekeeper.ByteServerApi;
import dev.sepler.bytekeeper.rest.GetFileRequest;
import dev.sepler.bytekeeper.rest.GetFileResponse;
import dev.sepler.bytekeeper.rest.GetFilesRequest;
import dev.sepler.bytekeeper.rest.GetFilesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ByteKeeperActivity implements ByteServerApi {

    @Override
    public ResponseEntity<GetFileResponse> getFile(@Valid GetFileRequest getFileRequest) {
        return null;
    }

    @Override
    public ResponseEntity<GetFilesResponse> getFiles(@Valid GetFilesRequest getFilesRequest) {
        return null;
    }
}
