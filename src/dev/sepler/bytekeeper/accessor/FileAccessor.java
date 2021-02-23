package dev.sepler.bytekeeper.accessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class FileAccessor {

    @Value("${bytekeeper.files.dir}")
    private final String directory;

    public void save(final String name, final MultipartFile file) {
        Path path = buildPath(name);
        copy(file, path);
    }

    public FileSystemResource retrieve(final String name) {
        Path path = buildPath(name);
        return new FileSystemResource(path);
    }

    private Path buildPath(final String name) {
        return Path.of(directory + File.separator + StringUtils.cleanPath(name)).toAbsolutePath();
    }

    protected void copy(final MultipartFile multipartFile, final Path path) {
        try {
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            log.error("Exception while saving file", exception);
            throw new RuntimeException(exception.getMessage());
        }
    }
}
