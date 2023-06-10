import express from 'express';
import path from 'path';
import dotenv from 'dotenv';
import favicon from 'serve-favicon';
import expressOasGenerator from 'express-oas-generator';
import kcAdminClient, { keycloakConnectionCheck } from './middlewares/kc.middleware';

dotenv.config();

const app = express();
const port = 3000;

expressOasGenerator.init(app, {
  apiDocPath: '/api-docs',
  openApiPath: '/api-spec',
  appRoutes: ['/realms'],
  tags: ['keycloak', 'admin-api']
});

// Set the view engine to ejs
app.set('view engine', 'ejs');

app.use(express.static('public'));

// Middleware to serve the favicon
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));

// Use the keycloakConnectionCheck middleware
app.use(keycloakConnectionCheck);

// Serve the main page
app.get('/', (req, res) => {
  res.redirect('/api-docs');
});

// List all realms
app.get('/realms', async (req, res) => {
  try {
    const realms = await kcAdminClient.realms.find();
    res.json(realms);
  } catch (err: any) {
    res.status(500).send({ error: err.toString() });
  }
});

// Get details of a specific realm
app.get('/realms/:realmName', async (req, res) => {
  try {
    const realmName = req.params.realmName;
    const realm = await kcAdminClient.realms.findOne({ realm: realmName });
    res.json(realm);
  } catch (err: any) {
    res.status(500).send({ error: err.toString() });
  }
});

// List all users within a particular realm
app.get('/realms/:realmName/users', async (req, res) => {
  try {
    const realmName = req.params.realmName;
    const users = await kcAdminClient.users.find({ realm: realmName });
    res.json(users);
  } catch (err: any) {
    res.status(500).send({ error: err.toString() });
  }
});

// Get details of a specific user within a particular realm
app.get('/realms/:realmName/users/:userId', async (req, res) => {
  try {
    const realmName = req.params.realmName;
    const userId = req.params.userId;
    const user = await kcAdminClient.users.findOne({ realm: realmName, id: userId });
    res.json(user);
  } catch (err: any) {
    res.status(500).send({ error: err.toString() });
  }
});


app.listen(port, () => {
  console.log(`Server is running at http://localhost:${port}`);
});
