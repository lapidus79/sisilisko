package common.misc;

import org.apache.commons.lang3.RandomStringUtils;

public class Helpers {

    public static String generateToken() {
        return RandomStringUtils.randomAlphanumeric(32);
    }
}
