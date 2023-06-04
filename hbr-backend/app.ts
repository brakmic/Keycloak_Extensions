import express from 'express';
import { Client, Issuer } from 'openid-client';
import dotenv from 'dotenv';
import path from 'path';
import favicon from 'serve-favicon';
import jwt from 'jsonwebtoken';

dotenv.config();

interface IEnvironmentVariables {
  CLIENT_ID: string;
  CLIENT_SECRET: string;
}

const { CLIENT_ID, CLIENT_SECRET } = process.env as unknown as IEnvironmentVariables;

if (!CLIENT_ID || !CLIENT_SECRET) {
  throw new Error('Environment Variables are missing!');
}

const app = express();
const port = 3000;

let client: Client | null = null;

Issuer.discover('http://localhost:8080/realms/test-realm')
.then((keycloakIssuer) => {
    console.log('Discovered issuer %s', keycloakIssuer.issuer);

    client = new keycloakIssuer.Client({
        client_id: CLIENT_ID,
        client_secret: CLIENT_SECRET,
    });
});

// Set the view engine to ejs
app.set('view engine', 'ejs');

app.use(express.static('public'));

// Middleware to serve the favicon
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));

// Serve the main page
app.get('/', (req, res) => {
    res.render('index', { title: 'Simple Backend', message: 'Welcome to Simple Backend!' });
});

app.get('/protected', async (req, res) => {
  if (!client) {
      return res.status(401).send('Keycloak client not initialized');
  }
  
  let tokenSet;
  try {
      tokenSet = await client.grant({
          grant_type: 'client_credentials'
      });
  } catch (err) {
      console.error('Failed to get token', err);
      return res.status(401).send('Failed to get token');
  }

  if (tokenSet) {
    const { access_token, token_type, expires_in, scope } = tokenSet;
    const decodedToken = jwt.decode(access_token as string);
    return res.render('protected', {
      token: access_token,
      decodedToken,
      tokenType: token_type,
      expiresIn: expires_in,
      scope: scope,
    });
  } else {
      return res.status(401).send('Unauthorized');
  }
});


app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});
