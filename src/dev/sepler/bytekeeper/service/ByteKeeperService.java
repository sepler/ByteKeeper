package dev.sepler.bytekeeper.service;

import dev.sepler.bytekeeper.accessor.FileAccessor;
import dev.sepler.bytekeeper.dao.ByteFileRepository;
import dev.sepler.bytekeeper.exception.ByteFileNotFoundException;
import dev.sepler.bytekeeper.model.ByteFile;
import dev.sepler.bytekeeper.model.Identifier;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class ByteKeeperService {

    @Autowired
    private final FileAccessor fileAccessor;

    @Autowired
    private final ByteFileRepository byteFileRepository;

    public void deleteByteFile(final Identifier id, final String token) {
        ByteFile byteFile = getByteFile(id);
        if (byteFile.isDeleted()) {
            throw new UnsupportedOperationException("ByteFile is already deleted");
        } else if (!byteFile.getDeleteToken().equals(token)) {
            throw new UnsupportedOperationException("Invalid delete token");
        }
        fileAccessor.delete(byteFile.getId().getValue());
    }

    public FileSystemResource downloadFile(final String id) throws FileNotFoundException {
        return fileAccessor.retrieve(id);
    }

    public ByteFile getByteFile(final Identifier id) {
        return byteFileRepository.findById(id).orElseThrow(() -> new ByteFileNotFoundException(id.getValue()));
    }

    public List<ByteFile> getByteFiles(final List<Identifier> ids) {
        List<ByteFile> byteFiles = new ArrayList<>();
        byteFileRepository.findAllById(ids).forEach(byteFiles::add);
        return byteFiles;
    }

    public Identifier putFile(final MultipartFile multipartFile) {
        Identifier id = Identifier.of(UUID.randomUUID().toString());
        ByteFile byteFile = new ByteFile()
                .withId(id)
                .withSizeInBytes(multipartFile.getSize())
                .withName(multipartFile.getOriginalFilename())
                .withCreatedAtInUtcEpochMilliseconds(Instant.now().toEpochMilli());
        byteFileRepository.save(byteFile);
        fileAccessor.save(id.getValue(), multipartFile);
        return id;
    }

}
