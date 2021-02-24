package dev.sepler.bytekeeper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = { IdentifierMapper.class })
public interface ByteFileMapper {

    @Mapping(source = "createdAt", target = "createdAtInUtcEpochMilliseconds")
    @Mapping(source = "size", target = "sizeInBytes")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    dev.sepler.bytekeeper.model.ByteFile map(dev.sepler.bytekeeper.rest.ByteFile byteFile);

    @Mapping(source = "createdAtInUtcEpochMilliseconds", target = "createdAt")
    @Mapping(source = "sizeInBytes", target = "size")
    dev.sepler.bytekeeper.rest.ByteFile toSdk(dev.sepler.bytekeeper.model.ByteFile byteFile);

}
