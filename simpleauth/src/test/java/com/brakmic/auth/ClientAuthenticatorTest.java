package com.brakmic.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.keycloak.models.KeycloakSession;
import org.mockito.Mockito;

public class SimpleAuthenticatorTest {
    
    @Test
    public void testCreateSimpleAuthenticator() {
        KeycloakSession session = Mockito.mock(KeycloakSession.class);
        SimpleAuthenticator simpleAuthenticator = new SimpleAuthenticator(session);
        Assertions.assertNotNull(simpleAuthenticator, "SimpleAuthenticator instance should not be null");
    }
}
