package com.epam.freelancer.security;

import org.apache.log4j.Logger;

import java.security.SecureRandom;

public class SaltUtil {

    public static final Logger LOG = Logger.getLogger(SaltUtil.class);

    public static String createSalt() {
        StringBuilder buf = new StringBuilder();
        LOG.info("get random number (salt)");
        SecureRandom sr = new SecureRandom();
        for (int i = 0; i < 50; i++) {
            boolean upper = sr.nextBoolean();
            char ch = (char) (sr.nextInt(26) + 'a');
            if (upper)
                ch = Character.toUpperCase(ch);

            buf.append(ch);
        }

        return buf.toString();
    }
}
