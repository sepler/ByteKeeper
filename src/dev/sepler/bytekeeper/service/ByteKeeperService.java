package dev.sepler.bytekeeper.service;

import dev.sepler.bytekeeper.rest.ByteFile;
import dev.sepler.bytekeeper.rest.Identifier;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class ByteKeeperService {

    public ByteFile getFile(final Identifier id) {
        return new ByteFile().withId(id);
    }

    public List<ByteFile> getFiles(final List<Identifier> ids) {
        return ids.stream().map(id -> new ByteFile().withId(id)).collect(Collectors.toList());
    }

    public Identifier putFile(final ByteFile byteFile) {
        return new Identifier().withValue(byteFile.getId().getValue());
    }

}
