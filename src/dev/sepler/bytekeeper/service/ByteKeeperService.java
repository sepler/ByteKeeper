package dev.sepler.bytekeeper.service;

import dev.sepler.bytekeeper.accessor.FileAccessor;
import dev.sepler.bytekeeper.model.ByteFile;
import dev.sepler.bytekeeper.model.Identifier;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ByteKeeperService {

    @Autowired
    private final FileAccessor fileAccessor;

    public FileSystemResource downloadFile(final String id) {
        return fileAccessor.retrieve(id);
    }

    public ByteFile getByteFile(final Identifier id) {
        return new ByteFile().withId(id);
    }

    public List<ByteFile> getByteFiles(final List<Identifier> ids) {
        return ids.stream().map(id -> new ByteFile().withId(id)).collect(Collectors.toList());
    }

    public Identifier putFile(final MultipartFile multipartFile) {
        Identifier id = Identifier.of(UUID.randomUUID().toString());
        fileAccessor.save(id.getValue(), multipartFile);
        return id;
    }

}
