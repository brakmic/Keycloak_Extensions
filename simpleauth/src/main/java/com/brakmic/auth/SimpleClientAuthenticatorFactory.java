package com.brakmic.auth;

import java.util.Collections;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.ClientAuthenticator;
import org.keycloak.authentication.ClientAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.idm.CredentialRepresentation;

public class SimpleClientAuthenticatorFactory implements ClientAuthenticatorFactory {

	public static final String PROVIDER_ID = "simple-client-authenticator";

    @Override
    public void init(Scope config) {
        // Initialize the factory here if necessary.
    }
	
	@Override
	public ClientAuthenticator create() {
		return new SimpleClientAuthenticator();
	}
	

	@Override
	public ClientAuthenticator create(KeycloakSession session) {
		return new SimpleClientAuthenticator();
	}

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // Perform any post-initialization tasks here if necessary.
    }

    @Override
    public void close() {
        // Clean up the factory here if necessary.
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "Simple Client Authenticator";
    }

    @Override
    public String getReferenceCategory() {
        return "simple-client-auth-reference";
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[]{
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.ALTERNATIVE
        };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return null;
    }

    @Override
    public String getHelpText() {
        return "This is a simple client authenticator";
    }

    @Override
    public List<ProviderConfigProperty> getConfigPropertiesPerClient() {
        return null;
    }

    @Override
    public Set<String> getProtocolAuthenticatorMethods(String loginProtocol) {
        if (loginProtocol.equals(OIDCLoginProtocol.LOGIN_PROTOCOL)) {
            Set<String> results = new LinkedHashSet<>();
            results.add(OIDCLoginProtocol.CLIENT_SECRET_BASIC);
            results.add(OIDCLoginProtocol.CLIENT_SECRET_POST);
            return results;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public Map<String, Object> getAdapterConfiguration(ClientModel client) {
        Map<String, Object> result = new HashMap<>();
        result.put(CredentialRepresentation.SECRET, client.getSecret());
        return result;
    }

}

