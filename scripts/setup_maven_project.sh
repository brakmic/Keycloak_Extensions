#!/usr/bin/env bash

# Project name
project="simpleauth"

# Check if the project directory already exists
if [ -d "$project" ]; then
  # Ask the user if they want to delete the existing project
  read -p "The project '$project' already exists. Do you want to delete it and create a new one? (y/N) " answer
  case ${answer:0:1} in
    y|Y )
        echo "Deleting the existing project..."
        rm -rf "$project"
    ;;
    * )
        echo "Aborting the script..."
        exit 1
    ;;
  esac
fi

# Use mvnw to create the basic structure of the project
./mvnw io.quarkus.platform:quarkus-maven-plugin:create \
    -DprojectGroupId=com.brakmic \
    -DprojectArtifactId=simpleauth \
    -DclassName="com.brakmic.auth.SimpleAuthenticator" \
    -Dpath="/simpleauth"

# Navigate into the project directory
cd simpleauth

# Remove the auto-generated pom.xml
rm pom.xml

# Create pom.xml containing the needed Keycloak libraries
cat << EOF > pom.xml
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.brakmic</groupId>
  <artifactId>simpleauth</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <properties>
    <maven.compiler.source>20</maven.compiler.source>
    <maven.compiler.target>20</maven.compiler.target>
    <keycloak.version>21.1.1</keycloak.version>
  </properties>
  
  <dependencies>
     <dependency>
      <groupId>jakarta.ws.rs</groupId>
      <artifactId>jakarta.ws.rs-api</artifactId>
      <version>3.1.0</version>
    </dependency>
     <dependency>
      <groupId>org.jboss.logging</groupId>
      <artifactId>jboss-logging</artifactId>
      <version>3.5.0.Final</version>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.9.3</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.9.3</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.keycloak</groupId>
      <artifactId>keycloak-core</artifactId>
      <version>${keycloak.version}</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.keycloak</groupId>
      <artifactId>keycloak-server-spi</artifactId>
      <version>${keycloak.version}</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.keycloak</groupId>
      <artifactId>keycloak-server-spi-private</artifactId>
      <version>${keycloak.version}</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.keycloak</groupId>
      <artifactId>keycloak-services</artifactId>
      <version>${keycloak.version}</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.keycloak</groupId>
      <artifactId>keycloak-common</artifactId>
      <version>${keycloak.version}</version>
      <scope>provided</scope>
  </dependency>
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-core</artifactId>
      <version>4.1.0</version>
      <scope>provided</scope>
    </dependency>
  </dependencies>
  
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.1</version>
        <configuration>
          <source>${maven.compiler.source}</source>
          <target>${maven.compiler.target}</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
EOF
