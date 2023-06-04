package com.brakmic.auth;

import java.util.stream.Stream;

import org.jboss.logging.Logger;
import org.keycloak.authentication.ClientAuthenticationFlowContext;
import org.keycloak.authentication.ClientAuthenticator;
import org.keycloak.models.ClientModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;

public class SimpleClientAuthenticator implements ClientAuthenticator {

	private static final Logger logger = Logger.getLogger(SimpleClientAuthenticator.class);
	private static final String EXPECTED_PROTOCOL = "openid-connect";

	@Override
    public void authenticateClient(ClientAuthenticationFlowContext context) {
		RealmModel realm = context.getRealm();
		
		Stream<ClientModel> clientsStream = context.getSession().clients().getClientsStream(realm);
		ClientModel client = clientsStream.filter(c -> c.getClientId().equals("test-client")).findFirst().get();

		if(client != null) {
			
			// print out info from associated roles
			client.getRolesStream().forEach(this::logRole);
            
            // check client availability
            if (!client.isEnabled()) {
                logger.debug("client login failed due to client being disabled");
                context.failure(null);
                return;
            }

            // check if client protocol is the correct one
            if (!EXPECTED_PROTOCOL.equals(client.getProtocol())) {
                logger.debug("client login failed due to incorrect protocol");
                context.failure(null);
                return;
            }
            
            String clientSecret = client.getSecret();
            // client must provide a secret
            if (clientSecret != null && !clientSecret.isEmpty()) {
                logger.debug("client id: " + client.getClientId());
                logger.debug("client secret: " + client.getSecret());
                context.success();
            } else {
                logger.debug("client login failed due to missing secret");
                context.failure(null);
            }
        } else {
            logger.debug("client login failed due to missing client");
            context.failure(null);
        }
    }

    @Override
    public void close() {
        logger.info("Closing SimpleClientAuthenticator");
    }

	private void logRole(RoleModel role) {
		logger.debug("Role: " + role.getName() + ", Desc: " + role.getDescription());
	}

}
