# Kotlin Spring Boot Example Service

## Running the Service

Simply select the application runner and press run if you're using IntelliJ. The source and target java versions are 17. Alternatively, the service can be run from the command line with `./gradlew bootRun`.

I chose to use an embedded in-memory database for ease of running the service. The data will be persisted across runs through file storage.

## Using the Service

While the service is running, you can view the contents of the database if you would like by accessing http://localhost:8080/h2-console in your browser. The username will be `sa`. The password will be `password` (only the best security!). The url will be `jdbc:h2:file:/data/demo`.

The API documentation is available through swagger at http://localhost:8080/swagger-ui/index.html. The page can also be used to interact with the API.
