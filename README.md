# Mettle Feature Flag Service

The Feature Flag Service provides the ability to maintain a list of feature flags.

The Feature Flag Service POC has security enabled to have more fine-grained control on the exposed endpoints.

## Pre-requisites

Local environment configuration:
- Java 17
- gradle
- keystore (for generating SSL certificates)
- change the file permissions for bin/*.sh from the project root directory
  - chmod 750 bin/*.sh
- run command bin/create-jks.sh to create the jks_store.jks in the current directory; then copy it to the project src/main/resources so that it is on the classpath (required for running)

## Create JKS keystore and certificate

Create a JKS keystore by running the shell script from the project root directory:
  scripts/create-jks.sh

Follow the prompts from the keytool command for values you need to supply.

The JKS store must be placed on the classpath in order for it to be found. For development purposes it can be 
placed in folder src/main/resources or in src/test/resources for testing purposes.

Remember to copy file jks_store.jks from the project root directory to either src/main/resources to run live else
copy to src/test/resources for use in testing.

## How to build locally
- either build using your IDE or
- build from the command line using command
  - gradlew build

## How to run locally
- build from the command line using command and then run 
- ./gradlew bootRun --args='--spring.profiles.active=local'

## How to test manually on localhost
- Postman collection is under "/postman/FeatureFlagService.postman_collection.json"
  
## PORTS
HTTP: 8080

## Swagger
[Swagger UI](http://localhost:8080/swagger-ui/index.html#/)