## Nodejs / Expressjs backend server that communicates with Keycloak

### Installation

Install nodejs and then run `npm install`

### Usage

`npm start`

### Backend-Keycloak communication

The backend server communicates with the Keycloak server to handle user authentication and authorization. It obtains an access token from Keycloak on behalf of the client (backend server itself) and uses it to make authorized requests. The Keycloak server verifies the request, issues the access token, and provides user identity and permissions. The backend server can then use the access token to secure its resources and customize behavior based on user roles. This communication ensures secure and controlled access to the backend server's functionalities.

### Client ID and Secret

Client ID and client secret authentication is a method used to verify the identity of a client application when communicating with an authorization server, such as Keycloak. The client ID is a public identifier that uniquely identifies the client application, while the client secret is a confidential credential known only to the client and the authorization server. By providing both the client ID and client secret during the authentication process, the client application can prove its identity and obtain the necessary access tokens to access protected resources. This mechanism helps ensure secure and authorized interactions between the client application and the authorization server.