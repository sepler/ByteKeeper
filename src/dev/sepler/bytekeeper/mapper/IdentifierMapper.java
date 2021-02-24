package dev.sepler.bytekeeper.mapper;

import org.mapstruct.Mapper;

@Mapper
public interface IdentifierMapper {

    dev.sepler.bytekeeper.model.Identifier map(dev.sepler.bytekeeper.rest.Identifier identifier);

    dev.sepler.bytekeeper.rest.Identifier toSdk(dev.sepler.bytekeeper.model.Identifier identifier);

}
