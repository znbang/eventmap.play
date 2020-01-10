package models;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomId {
    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generate() {
        byte[] buf = new byte[20];
        random.nextBytes(buf);
        return encoder.encodeToString(buf);
    }
}
