# Manual execution
The following commands detail how to use the the client and server programs

## Build artifacts
Launch the following command to build the jar files for server and client

```
./gradlew clean createServer createClient
```
Or, if you are on windows
```
./gradlew.bat createServer createClient
```


## Launch Server
```
java -jar build/libs/java-file-server.jar YOURDIRECTORYPATH
```

## Launch Client
```
java -jar build/libs/java-file-client.jar
```

# Run tests
Run the unit tests for the application

```
./gradlew clean test jacocoTestReport printTestCoverage
```
