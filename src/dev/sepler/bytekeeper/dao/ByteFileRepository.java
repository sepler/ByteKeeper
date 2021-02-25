package dev.sepler.bytekeeper.dao;

import dev.sepler.bytekeeper.model.ByteFile;
import dev.sepler.bytekeeper.model.Identifier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ByteFileRepository extends MongoRepository<ByteFile, Identifier> {
}
