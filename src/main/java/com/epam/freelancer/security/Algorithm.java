package com.epam.freelancer.security;

public interface Algorithm {

    String crypt(String base, String salt);
}
