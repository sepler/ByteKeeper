package dev.sepler.bytekeeper.controller;

import dev.sepler.bytekeeper.ByteKeeperApi;
import dev.sepler.bytekeeper.exception.ErrorRequestException;
import dev.sepler.bytekeeper.rest.ByteFile;
import dev.sepler.bytekeeper.rest.GetFileRequest;
import dev.sepler.bytekeeper.rest.GetFilesRequest;
import dev.sepler.bytekeeper.rest.GetFilesResponse;
import dev.sepler.bytekeeper.rest.Identifier;
import dev.sepler.bytekeeper.rest.PutFileRequest;
import dev.sepler.bytekeeper.rest.PutFileResponse;
import dev.sepler.bytekeeper.service.ByteKeeperService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ByteKeeperController implements ByteKeeperApi {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private final ByteKeeperService byteKeeperService;

    @Override
    public ResponseEntity<Resource> getFile(final GetFileRequest getFileRequest) {
        log.info("Received getFile request: {}", getFileRequest);
        Set<ConstraintViolation<GetFileRequest>> violations = VALIDATOR.validate(getFileRequest);
        if (!violations.isEmpty()) {
            throwErrorResponse(violations);
        }

        Identifier id = getFileRequest.getId();
        FileSystemResource fileSystemResource = byteKeeperService.getFile(id);

        return ResponseEntity.ok().body(fileSystemResource);
    }

    @Override
    public ResponseEntity<GetFilesResponse> getFiles(final GetFilesRequest getFilesRequest) {
        log.info("Received getFiles request: {}", getFilesRequest);
        Set<ConstraintViolation<GetFilesRequest>> violations = VALIDATOR.validate(getFilesRequest);
        if (!violations.isEmpty()) {
            throwErrorResponse(violations);
        }
        List<Identifier> ids = getFilesRequest.getIds();
        List<ByteFile> byteFiles = byteKeeperService.getFiles(ids);

        GetFilesResponse getFilesResponse = new GetFilesResponse().withByteFiles(byteFiles);
        return ResponseEntity.ok().body(getFilesResponse);
    }

    @Override
    public ResponseEntity<PutFileResponse> putFile(final MultipartFile file, final PutFileRequest putFileRequest) {
        log.info("Received putFiles request: {}", putFileRequest);
        Set<ConstraintViolation<PutFileRequest>> violations = VALIDATOR.validate(putFileRequest);
        if (!violations.isEmpty()) {
            throwErrorResponse(violations);
        } else if (Objects.isNull(file)) {
            throwErrorResponse("file must not be null");
        }
        Identifier id = byteKeeperService.putFile(file);

        PutFileResponse putFileResponse = new PutFileResponse().withId(id);
        return ResponseEntity.ok().body(putFileResponse);
    }

    private static <T> void throwErrorResponse(final Set<ConstraintViolation<T>> violationList) {
        List<String> messages = violationList.stream()
                .map(violation -> violation.getPropertyPath().toString() + " " + violation.getMessage())
                .collect(Collectors.toList());
        throwErrorResponse(messages);
    }

    private static void throwErrorResponse(final String reason) {
        throwErrorResponse(Collections.singletonList(reason));
    }

    private static void throwErrorResponse(final List<String> reason) {
        String errorMessage = "Invalid request: " + reason;
        log.warn(errorMessage);
        throw new ErrorRequestException(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
