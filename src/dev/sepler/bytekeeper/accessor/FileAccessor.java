package dev.sepler.bytekeeper.accessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
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

    public FileSystemResource retrieve(final String name) throws FileNotFoundException {
        Path path = buildPath(name);
        if (!exists(path)) {
            throw new FileNotFoundException(path.toAbsolutePath().toString());
        }
        return new FileSystemResource(path);
    }

    public void delete(final String name) {
        Path path = buildPath(name);
        deleteIfExists(path);
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

    protected void deleteIfExists(final Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            log.error("Exception while deleting file", exception);
            throw new RuntimeException(exception.getMessage());
        }
    }

    protected boolean exists(final Path path) {
        return Files.exists(path);
    }
}
