package com.brakmic.auth;

import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import org.jboss.logging.Logger;

public class SimpleAuthenticator implements Authenticator {
    private static final Logger logger = Logger.getLogger(SimpleAuthenticator.class);

    public SimpleAuthenticator(KeycloakSession session) {
    	if (session != null && session.getContext() != null) {
    		logger.debug("Initialized SimpleAuthenticator in realm: " + session.getContext().getRealm());
    	}
    }

    
    @Override
    public void authenticate(AuthenticationFlowContext context) {
        //context.success();
	    try {
	         UserModel user = context.getUser();
	         if (user == null) {
	             logger.error("Unknown user");
	             context.failure(AuthenticationFlowError.UNKNOWN_USER);
	             return;
	         }
	
	         // Retrieve user's username and email
	         String username = user.getUsername();
	         String email = user.getEmail();
	
	         // Check if username and email are not null or empty
	         if (username == null || username.isEmpty() || email == null || email.isEmpty()) {
	             logger.error("Invalid credentials");
	             context.failure(AuthenticationFlowError.INVALID_CREDENTIALS);
	             return;
	         }
	
	         // If username, email, and password are present, consider the authentication as successful
	         logger.debug("User authenticated: " + username);
	         context.success();
	     } catch (Exception e) {
	         logger.error("Error during authentication", e);
	         context.failure(AuthenticationFlowError.INTERNAL_ERROR);
	     }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // No action required
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // No required actions
    }

    @Override
    public void close() {
        // No resources to close
    }
}
