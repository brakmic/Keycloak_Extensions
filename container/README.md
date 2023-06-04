## Containerized Keycloak

### Usage

`docker compose up -d`

**OR**

* Option 1 `docker build -t brakmic/keycloak .` (default Dockerfile)
* Option 2 `docker build -t brakmic/keycloak -f Dockerfile.providers` (to import providers)
* Option 3 `docker build -t brakmic/keycloak -f Dockerfile.import .` (to import both `test-realm.json` and providers)

Finally, run it with `docker run --rm -it -p 8080:8080 brakmic/keycloak`

### docker-compose.yml

This Docker Compose configuration defines two services: **keycloak** and **postgres**. The **keycloak** service is built using the default Dockerfile and has several volumes mounted for different directories, including providers, themes, and configuration files. It runs the `kc.sh start-dev` command with various migration-related parameters. The Keycloak server is exposed on port 8080, and environment variables are set for database configuration, Quarkus host, Keycloak URL, admin credentials, etc. It depends on the `postgres` service and will be restarted on failure. The `postgres` service uses the official Postgres image, with a volume for data storage, and environment variables for database configuration. Both services are connected to their respective networks.