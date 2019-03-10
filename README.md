## Description

Server for chekak-messenger

## Howto

### build artifacts:
 ./gradlew :messenger-console-client:jar
 ./gradlew :messenger-server:jar

### run server

java -jar messenger-server/build/libs/messenger-server-0.0.1-SNAPSHOT.jar

### run clients

java -jar messenger-console-client/build/libs/messenger-console-client-0.0.1-SNAPSHOT.jar
