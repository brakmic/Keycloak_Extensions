package com.brakmic.auth.local;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.keycloak.authentication.ClientAuthenticationFlowContext;
import org.keycloak.authentication.ClientAuthenticator;
import org.keycloak.models.ClientModel;
import org.keycloak.models.ClientProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.protocol.oidc.OIDCClientSecretConfigWrapper;

import com.brakmic.auth.SimpleClientAuthenticatorFactory;


public class RunLocal {

    public static void main(String[] args) {
    	ClientAuthenticationFlowContext context = mock(ClientAuthenticationFlowContext.class);
    	KeycloakSession session = mock(KeycloakSession.class);
    	ClientProvider provider = mock(ClientProvider.class);
    	RealmModel realm = mock(RealmModel.class);
    	ClientModel client = mock(ClientModel.class);
    	OIDCClientSecretConfigWrapper wrapper = mock(OIDCClientSecretConfigWrapper.class);
    	
    	when(wrapper.isClientSecretExpired()).thenReturn(false);
    	when(client.isEnabled()).thenReturn(true);
    	when(client.getName()).thenReturn("test-client");
    	when(client.getRealm()).thenReturn(realm);
    	when(client.getSecret()).thenReturn("password");
    	when(client.validateSecret("password")).thenReturn(true);
    	when(client.getBaseUrl()).thenReturn("http://localhost:8080");
    	when(realm.getName()).thenReturn("test-realm");
    	when(session.getAttribute("client_id", String.class)).thenReturn("test-client");
    	when(session.getAttribute("client_secret", String.class)).thenReturn("password");
    	when(context.getRealm()).thenReturn(realm);
    	when(context.getSession()).thenReturn(session);
    	when(context.getSession().clients()).thenReturn(provider);
    	when(provider.getClientByClientId(realm, "test-client")).thenReturn(client);
    	
    	
        SimpleClientAuthenticatorFactory factory = new SimpleClientAuthenticatorFactory();
        ClientAuthenticator authenticator = factory.create(session);
        
        
        authenticator.authenticateClient(context);
        System.out.println(authenticator);
    }
}
