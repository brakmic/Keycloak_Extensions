package com.brakmic.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class SimpleAuthenticatorFactoryTest {

    @Test
    public void testCreateSimpleAuthenticatorFactory() {
        SimpleAuthenticatorFactory simpleAuthenticatorFactory = new SimpleAuthenticatorFactory();
        Assertions.assertNotNull(simpleAuthenticatorFactory, "SimpleAuthenticatorFactory instance should not be null");
    }
}
