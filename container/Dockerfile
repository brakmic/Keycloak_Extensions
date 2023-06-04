FROM alpine AS downloader

ARG KEYCLOAK_VERSION=21.1.1
ENV KEYCLOAK_VERSION=${KEYCLOAK_VERSION}

RUN apk add --no-cache curl

# Download Java Runtime
RUN curl -LO https://github.com/adoptium/temurin20-binaries/releases/download/jdk-20.0.1%2B9/OpenJDK20U-jre_x64_alpine-linux_hotspot_20.0.1_9.tar.gz
RUN tar -zxvf OpenJDK20U-jre_x64_alpine-linux_hotspot_20.0.1_9.tar.gz
RUN mv jdk-20.0.1+9-jre /opt/java

# Download Keycloak
RUN curl -LO https://github.com/keycloak/keycloak/releases/download/${KEYCLOAK_VERSION}/keycloak-${KEYCLOAK_VERSION}.tar.gz
RUN tar -zxvf keycloak-${KEYCLOAK_VERSION}.tar.gz -C / && mv /keycloak-${KEYCLOAK_VERSION} /opt/keycloak

FROM alpine AS runtime
RUN apk add --no-cache bash

# Copy Keycloak and Java to runtime
COPY --from=downloader /opt/java /opt/java
COPY --from=downloader /opt/keycloak /opt/keycloak

# Runtime environment setup
ENV JAVA_HOME=/opt/java
ENV PATH="${JAVA_HOME}/bin:${PATH}"
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=password

# Expose the ports and start Keycloak
EXPOSE 8080
WORKDIR /opt/keycloak
ENTRYPOINT [ "bash" ]
CMD ["-c", "./bin/kc.sh start-dev"]
