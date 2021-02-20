package dev.sepler.bytekeeper.service;

import dev.sepler.bytekeeper.rest.ByteFile;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class ByteKeeperService {

    public ByteFile getFile(final String id) {
        return new ByteFile();
    }

}
