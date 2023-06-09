package com.brakmic.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SimpleClientAuthenticatorFactoryTest {

    @Mock
    private KeycloakSession session;

    @Mock
    private KeycloakSessionFactory factory;

    @Mock
    private ClientModel client;

    private SimpleClientAuthenticatorFactory simpleClientAuthenticatorFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        simpleClientAuthenticatorFactory = new SimpleClientAuthenticatorFactory();
    }

    @Test
    public void testCreate() {
        SimpleClientAuthenticator clientAuthenticator = (SimpleClientAuthenticator) simpleClientAuthenticatorFactory.create(session);
        assertTrue(clientAuthenticator instanceof SimpleClientAuthenticator);
    }

    @Test
    public void testGetId() {
        assertEquals(SimpleClientAuthenticatorFactory.PROVIDER_ID, simpleClientAuthenticatorFactory.getId());
    }

    @Test
    public void testGetDisplayType() {
        assertEquals("Simple Client Authenticator", simpleClientAuthenticatorFactory.getDisplayType());
    }

    @Test
    public void testGetReferenceCategory() {
        assertEquals("simple-client-auth-reference", simpleClientAuthenticatorFactory.getReferenceCategory());
    }

    @Test
    public void testIsConfigurable() {
        assertFalse(simpleClientAuthenticatorFactory.isConfigurable());
    }

    @Test
    public void testGetRequirementChoices() {
        AuthenticationExecutionModel.Requirement[] requirements = simpleClientAuthenticatorFactory.getRequirementChoices();
        assertTrue(requirements.length > 0);
    }

    @Test
    public void testIsUserSetupAllowed() {
        assertFalse(simpleClientAuthenticatorFactory.isUserSetupAllowed());
    }

    @Test
    public void testGetConfigProperties() {
        assertNull(simpleClientAuthenticatorFactory.getConfigProperties());
    }

    @Test
    public void testGetHelpText() {
        assertEquals("This is a simple client authenticator", simpleClientAuthenticatorFactory.getHelpText());
    }

    @Test
    public void testGetConfigPropertiesPerClient() {
        assertNull(simpleClientAuthenticatorFactory.getConfigPropertiesPerClient());
    }

    @Test
    public void testGetProtocolAuthenticatorMethods() {
        assertTrue(simpleClientAuthenticatorFactory.getProtocolAuthenticatorMethods(OIDCLoginProtocol.LOGIN_PROTOCOL).size() > 0);
        assertTrue(simpleClientAuthenticatorFactory.getProtocolAuthenticatorMethods("other-protocol").isEmpty());
    }

    @Test
    public void testGetAdapterConfiguration() {
        when(client.getSecret()).thenReturn("secret");
        assertEquals("secret", simpleClientAuthenticatorFactory.getAdapterConfiguration(client).get("secret"));
    }

}
