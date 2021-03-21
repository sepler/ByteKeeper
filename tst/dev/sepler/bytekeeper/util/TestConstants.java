package dev.sepler.bytekeeper.util;

import dev.sepler.bytekeeper.model.ByteFile;
import dev.sepler.bytekeeper.model.Identifier;

public final class TestConstants {

    public static final String TEST_ID = "testId";

    public static final String TEST_NAME = "testName";

    public static final String TEST_DELETE_TOKEN = "testDeleteToken";

    public static final ByteFile TEST_BYTE_FILE = new ByteFile()
            .withId(Identifier.of(TEST_ID))
            .withName(TEST_NAME)
            .withDeleteToken(TEST_DELETE_TOKEN);

    private TestConstants() {
    }

}
