package dev.sepler.bytekeeper.controller;

import dev.sepler.bytekeeper.ByteKeeperApi;
import dev.sepler.bytekeeper.exception.ByteFileNotFoundException;
import dev.sepler.bytekeeper.exception.ErrorRequestException;
import dev.sepler.bytekeeper.mapper.ByteFileMapper;
import dev.sepler.bytekeeper.mapper.IdentifierMapper;
import dev.sepler.bytekeeper.model.ByteFile;
import dev.sepler.bytekeeper.model.Identifier;
import dev.sepler.bytekeeper.rest.GetByteFileRequest;
import dev.sepler.bytekeeper.rest.GetByteFileResponse;
import dev.sepler.bytekeeper.rest.GetByteFilesRequest;
import dev.sepler.bytekeeper.rest.GetByteFilesResponse;
import dev.sepler.bytekeeper.rest.PutFileRequest;
import dev.sepler.bytekeeper.rest.PutFileResponse;
import dev.sepler.bytekeeper.service.ByteKeeperService;
import java.io.FileNotFoundException;
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
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ByteKeeperController implements ByteKeeperApi {

    private final ByteFileMapper byteFileMapper = Mappers.getMapper(ByteFileMapper.class);

    private final IdentifierMapper identifierMapper = Mappers.getMapper(IdentifierMapper.class);

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private final ByteKeeperService byteKeeperService;

    @Override
    public ResponseEntity<Resource> downloadFile(String id) {
        log.info("Received downloadFile request: id={}", id);
        if (!StringUtils.hasText(id)) {
            throw createBadRequestErrorResponse("id must not be blank");
        }
        try {
            String fileName = byteKeeperService.getByteFile(Identifier.of(id)).getName();
            FileSystemResource fileSystemResource = byteKeeperService.downloadFile(id);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Disposition", "attachment; filename=" + fileName);
            return ResponseEntity.ok().headers(responseHeaders).body(fileSystemResource);
        } catch (ByteFileNotFoundException | FileNotFoundException exception) {
            throw createNotFoundErrorResponse(id);
        } catch (Exception exception) {
            log.error("Encountered exception while processing request", exception);
            throw createInternalServerErrorResponse();
        }
    }

    @Override
    public ResponseEntity<GetByteFileResponse> getByteFile(GetByteFileRequest getByteFileRequest) {
        log.info("Received getByteFile request: {}", getByteFileRequest);
        Set<ConstraintViolation<GetByteFileRequest>> violations = validator.validate(getByteFileRequest);
        if (!violations.isEmpty()) {
            throw createBadRequestErrorResponse(violations);
        }

        Identifier id = identifierMapper.map(getByteFileRequest.getId());
        try {
            ByteFile byteFile = byteKeeperService.getByteFile(id);

            GetByteFileResponse getByteFileResponse = new GetByteFileResponse()
                    .withByteFile(byteFileMapper.toSdk(byteFile));
            return ResponseEntity.ok().body(getByteFileResponse);
        } catch (ByteFileNotFoundException exception) {
            throw createNotFoundErrorResponse(exception.getId());
        } catch (Exception exception) {
            log.error("Encountered exception while processing request", exception);
            throw createInternalServerErrorResponse();
        }
    }

    @Override
    public ResponseEntity<GetByteFilesResponse> getByteFiles(final GetByteFilesRequest getFilesRequest) {
        log.info("Received getByteFiles request: {}", getFilesRequest);
        Set<ConstraintViolation<GetByteFilesRequest>> violations = validator.validate(getFilesRequest);
        if (!violations.isEmpty()) {
            throw createBadRequestErrorResponse(violations);
        }
        List<Identifier> ids = getFilesRequest.getIds().stream()
                .map(identifierMapper::map)
                .collect(Collectors.toList());
        try {
            List<ByteFile> byteFiles = byteKeeperService.getByteFiles(ids);

            GetByteFilesResponse getFilesResponse = new GetByteFilesResponse()
                    .withByteFiles(byteFiles.stream().map(byteFileMapper::toSdk).collect(Collectors.toList()));
            return ResponseEntity.ok().body(getFilesResponse);
        } catch (Exception exception) {
            log.error("Encountered exception while processing request", exception);
            throw createInternalServerErrorResponse();
        }
    }

    @Override
    public ResponseEntity<PutFileResponse> putFile(final MultipartFile file, final PutFileRequest putFileRequest) {
        log.info("Received putFiles request: {}", putFileRequest);
        Set<ConstraintViolation<PutFileRequest>> violations = validator.validate(putFileRequest);
        if (!violations.isEmpty()) {
            throw createBadRequestErrorResponse(violations);
        } else if (Objects.isNull(file)) {
            throw createBadRequestErrorResponse("file must not be null");
        }
        try {
            Identifier id = byteKeeperService.putFile(file);

            PutFileResponse putFileResponse = new PutFileResponse()
                    .withId(identifierMapper.toSdk(id));
            return ResponseEntity.ok().body(putFileResponse);
        } catch (Exception exception) {
            log.error("Encountered exception while processing request", exception);
            throw createInternalServerErrorResponse();
        }
    }

    private static <T> ErrorRequestException createBadRequestErrorResponse(final Set<ConstraintViolation<T>> violationList) {
        List<String> messages = violationList.stream()
                .map(violation -> violation.getPropertyPath().toString() + " " + violation.getMessage())
                .collect(Collectors.toList());
        return createBadRequestErrorResponse(messages);
    }

    private static ErrorRequestException createBadRequestErrorResponse(final String reason) {
        return createBadRequestErrorResponse(Collections.singletonList(reason));
    }

    private static ErrorRequestException createBadRequestErrorResponse(final List<String> reason) {
        String errorMessage = "Invalid request: " + reason;
        return createErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    private static ErrorRequestException createInternalServerErrorResponse() {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
    }

    private static ErrorRequestException createNotFoundErrorResponse(final String reason) {
        String errorMessage = "Not found: " + reason;
        return createErrorResponse(HttpStatus.NOT_FOUND, errorMessage);
    }

    private static ErrorRequestException createErrorResponse(final HttpStatus httpStatus, final String reason) {
        log.warn(reason);
        return new ErrorRequestException(httpStatus, reason);
    }

}
