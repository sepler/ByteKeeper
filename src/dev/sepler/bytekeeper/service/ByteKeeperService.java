package dev.sepler.bytekeeper.service;

import dev.sepler.bytekeeper.accessor.FileAccessor;
import dev.sepler.bytekeeper.rest.ByteFile;
import dev.sepler.bytekeeper.rest.Identifier;
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

    public FileSystemResource getFile(final Identifier id) {
        return fileAccessor.retrieve(id.getValue());
    }

    public List<ByteFile> getFiles(final List<Identifier> ids) {
        return ids.stream().map(id -> new ByteFile().withId(id)).collect(Collectors.toList());
    }

    public Identifier putFile(final MultipartFile multipartFile) {
        Identifier id = new Identifier().withValue(UUID.randomUUID().toString());
        fileAccessor.save(id.getValue(), multipartFile);
        return id;
    }

}
