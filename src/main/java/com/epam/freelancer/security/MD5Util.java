package com.epam.freelancer.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

public class MD5Util implements Algorithm {

    public static final Logger LOG = Logger.getLogger(MD5Util.class);

    @Override
    public String crypt(String base, String salt) {
        LOG.info("encode by MD5");
        base += salt;
        String md5Hex = DigestUtils.md5Hex(base);

        return md5Hex;
    }
}
