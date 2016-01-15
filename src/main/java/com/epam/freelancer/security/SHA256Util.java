package com.epam.freelancer.security;

import org.apache.log4j.Logger;

import java.security.MessageDigest;

public class SHA256Util implements Algorithm {

    public static final Logger LOG = Logger.getLogger(SHA256Util.class);

    @Override
    public String crypt(String base, String salt) {
        base += salt;
        StringBuffer hexString = new StringBuffer();

        try {
            LOG.info("encode by SHA256 algorithm");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

        } catch (Exception ex) {
            LOG.error("encode(SHA256) exception " + ex);
        }

        return hexString.toString();
    }
}
