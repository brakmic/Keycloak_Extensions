import dotenv from 'dotenv';
import { KeycloakAdminClient } from 'kc-admin-client';
import { NextFunction, Request, Response } from 'express';
import path from 'path';

dotenv.config({ path: path.resolve(__dirname, '../.env') });

interface IEnvironmentVariables {
  KC_URL: string;
  KC_REALM: string;
  CLIENT_ID: string;
  CLIENT_SECRET: string;
}

const {
  KC_URL,
  KC_REALM,
  CLIENT_ID,
  CLIENT_SECRET } = process.env as unknown as IEnvironmentVariables;

const kcAdminClient = new KeycloakAdminClient({
  baseUrl: KC_URL,
  realmName: KC_REALM
});

const connectKeycloak = async () => {
  await kcAdminClient.auth({
    clientId: CLIENT_ID,
    clientSecret: CLIENT_SECRET,
    grantType: 'client_credentials',
  });
};

export const keycloakConnectionCheck = async (req: Request, res: Response, next: NextFunction) => {
  try {
    await kcAdminClient.serverInfo.find();
    next();
  } catch (error) {
    try {
      await connectKeycloak();
      console.log('Connected to Keycloak');
      next();
    } catch (error: any) {
      res.status(500).send({ error: error.toString() });
    }
  }
};

export default kcAdminClient;
