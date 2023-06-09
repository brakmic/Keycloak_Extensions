package com.brakmic.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.authentication.ClientAuthenticationFlowContext;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.ClientModel;
import org.keycloak.models.ClientProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SimpleClientAuthenticatorTest {

    @Mock
    private ClientAuthenticationFlowContext context;

    @Mock
    private RealmModel realm;

    @Mock
    private ClientModel client;

    @Mock
    private EventBuilder eventBuilder;

    private SimpleClientAuthenticator authenticator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        authenticator = new SimpleClientAuthenticator();
        KeycloakSession session = getKeycloakSessionMock();
        ClientProvider clientProvider = getClientProviderMock(session);
        when(session.getProvider(ClientProvider.class)).thenReturn(clientProvider);
        when(context.getSession()).thenReturn(session);
        when(context.getEvent()).thenReturn(eventBuilder);
        when(session.clients()).thenReturn(clientProvider);
    }

    @Test
    public void testAuthenticateClient_Success() {
        String expectedClientId = "test-client";
        String expectedProtocol = "openid-connect";
        String expectedSecret = "test-secret";

        // Mock client
        ClientModel client = mock(ClientModel.class);
        when(client.getClientId()).thenReturn(expectedClientId);
        when(client.getProtocol()).thenReturn(expectedProtocol);
        when(client.getSecret()).thenReturn(expectedSecret);
        when(client.isEnabled()).thenReturn(true);

        // Mock RealmModel and ClientProvider
        RealmModel realm = mock(RealmModel.class);
        ClientProvider clientProvider = mock(ClientProvider.class);
        when(context.getRealm()).thenReturn(realm);
        when(context.getSession().clients()).thenReturn(clientProvider);
        when(clientProvider.getClientsStream(realm)).thenReturn(Stream.of(client));

        // Run authentication
        authenticator.authenticateClient(context);

        // Verify behavior
        verify(context).success();
        verify(context, never()).failure(any());
    }


    @Test
    public void testAuthenticateClient_Failure() {
        String expectedClientId = "test-client";
        String incorrectProtocol = "invalid-protocol";

        // Mock clients and context
        when(client.getClientId()).thenReturn(expectedClientId);
        when(client.getProtocol()).thenReturn(incorrectProtocol);
        when(context.getRealm()).thenReturn(realm);

        // Run authentication
        authenticator.authenticateClient(context);

        // Verify behavior
        verify(context).failure(any());
        verify(context, never()).success();
        verify(context, times(1)).getRealm();
        verify(context, times(1)).getSession();
        verify(context, times(1)).getEvent();
    }


    private KeycloakSession getKeycloakSessionMock() {
        KeycloakSession session = mock(KeycloakSession.class);
        return session;
    }

    private ClientProvider getClientProviderMock(KeycloakSession session) {
        ClientProvider clientProvider = mock(ClientProvider.class);
        when(clientProvider.getClientsStream(any(RealmModel.class))).thenReturn(Stream.of(client));
        return clientProvider;
    }
}
