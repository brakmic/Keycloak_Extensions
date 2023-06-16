#!/usr/bin/env bash

# Project name
artifactid="simpleauth"
groupid="org.brakmic.auth"
class="SimpleAuthenticator"

# Check if the project directory already exists
if [ -d "$artifactid" ]; then
  # Ask the user if they want to delete the existing project
  read -p "The project '$artifactid' already exists. Do you want to delete it and create a new one? (y/N) " answer
  case ${answer:0:1} in
    y|Y )
        echo "Deleting the existing project..."
        rm -rf "$artifactid"
    ;;
    * )
        echo "Aborting the script..."
        exit 1
    ;;
  esac
fi

# Use mvnw to create the basic structure of the project
./mvnw io.quarkus.platform:quarkus-maven-plugin:create \
    -DprojectGroupId="$groupid" \
    -DprojectArtifactId="$artifactid" \
    -DclassName="$groupid.$class" \
    -Dpath="/$project"

# Navigate into the project directory
cd "$artifactid"

# Remove the auto-generated pom.xml
rm pom.xml

# Create pom.xml containing the needed Keycloak libraries
cat << 'EOF' | sed "s|\$groupid|$groupid|g; s|\$artifactid|$artifactid|g" > pom.xml
<?xml version="1.0"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>$groupid</groupId>
  <artifactId>$artifactid</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <keycloak.version>21.1.1</keycloak.version> <!-- Adjust this to your Keycloak version -->
  </properties>
  <dependencies>
	<dependency>
       <groupId>ch.qos.logback</groupId>
       <artifactId>logback-classic</artifactId>
       <version>1.4.7</version>
       <scope>provided</scope>
	</dependency>
    <dependency>
       <groupId>org.slf4j</groupId>
       <artifactId>slf4j-api</artifactId>
       <version>2.0.7</version>
       <scope>provided</scope>
    </dependency>
	 <dependency>
      <groupId>jakarta.ws.rs</groupId>
      <artifactId>jakarta.ws.rs-api</artifactId>
      <version>3.1.0</version>
      <scope>provided</scope>
    </dependency>
     <dependency>
      <groupId>org.jboss.logging</groupId>
      <artifactId>jboss-logging</artifactId>
      <version>3.5.0.Final</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.9.3</version>
      <scope>test</scope>
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
      <scope>test</scope>
    </dependency>
  <dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.3.1</version>
    <scope>test</scope>
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
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.1.2</version>
      </plugin>
    </plugins>
  </build>
</project>
EOF

echo "$artifactid setup completed"

