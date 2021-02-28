package dev.sepler.bytekeeper.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ByteFileNotFoundException extends RuntimeException {

    String id;

}
