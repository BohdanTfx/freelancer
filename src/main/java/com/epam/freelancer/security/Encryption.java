package com.epam.freelancer.security;

import org.apache.log4j.Logger;

public class Encryption {

    public static final Logger LOG = Logger.getLogger(Encryption.class);
    private Algorithm algorithm;

    public Encryption(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String crypt(String base, String salt) {
        LOG.info("encode");
        return algorithm.crypt(base, salt);
    }
}
