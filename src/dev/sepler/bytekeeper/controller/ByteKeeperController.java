package dev.sepler.bytekeeper.controller;

import dev.sepler.bytekeeper.rest.ByteFile;
import dev.sepler.bytekeeper.rest.GetFileRequest;
import dev.sepler.bytekeeper.rest.GetFileResponse;
import dev.sepler.bytekeeper.rest.GetFilesRequest;
import dev.sepler.bytekeeper.rest.GetFilesResponse;
import dev.sepler.bytekeeper.rest.PutFileRequest;
import dev.sepler.bytekeeper.rest.PutFileResponse;
import dev.sepler.bytekeeper.service.ByteKeeperService;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ByteKeeperController {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private final ByteKeeperService byteKeeperService;

    public ResponseEntity<GetFileResponse> getFile(final GetFileRequest getFileRequest) {
        Set<ConstraintViolation<GetFileRequest>> violations = VALIDATOR.validate(getFileRequest);
        if (!violations.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String id = getFileRequest.getId().getValue();
        ByteFile byteFile = byteKeeperService.getFile(id);

        GetFileResponse getFileResponse = new GetFileResponse().withByteFile(byteFile);
        return ResponseEntity.ok().body(getFileResponse);
    }

    public ResponseEntity<GetFilesResponse> getFiles(final GetFilesRequest getFilesRequest) {
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<PutFileResponse> putFile(final PutFileRequest putFileRequest) {
        return ResponseEntity.noContent().build();
    }
}
