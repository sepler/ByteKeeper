package dev.sepler.bytekeeper.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ByteFile {

    Identifier id;

    long version;

    String name;

    String createdBy;

    long sizeInBytes;

    long createdAtInUtcEpochMilliseconds;

    boolean deleted;

    public ByteFile() {
        this.id = Identifier.of("");
        this.version = 1;
        this.name = null;
        this.createdBy = null;
        this.sizeInBytes = 0;
        this.createdAtInUtcEpochMilliseconds = 0;
        this.deleted = false;
    }

}
