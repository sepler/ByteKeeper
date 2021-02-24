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
public class Identifier {

    String value;

    public Identifier() {
        this.value = null;
    }

    public static Identifier of(String value) {
        return new Identifier(value);
    }

}
