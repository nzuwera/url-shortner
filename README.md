## url-shortner
IT-Impulse take home assigment

### GENERAL CONDITIONS
In this Take Home Assignment, a small Spring Boot REST Service with Java is to be developed. You can use Spring Initializr to create the project.
The data should be stored in any relational database. The connection of the database to the Spring Framework should be done with Spring Data JPA.
The application should make meaningful log outputs and use SLF4J as the logging facade. Either a file in the file system or the console (stdout) is sufficient as a log sink.
All interfaces of the service should be as RESTful as possible.
All sources of information (online and offline) may be used to do this Take Home Assignment. If you have any questions about the task, you can get in touch with your company's technical contact person.
The time to do this assignment is approximately 2-3 days and the submission takes place one week after the task has been announced.
### FUNCTIONAL REQUIREMENTS
A “URL shortener” service is to be implemented. With the help of a REST interface, a theoretical front end (not part of this task) should be able to generate a shortened link for any URL.
For this purpose, the backend should assign a unique alphanumeric ID that is as short as possible to each transferred URL. It should also be possible for the REST client to set its own IDs. If this ID is already in use, a corresponding REST-compliant error message should be returned.
When creating a new Short-URL, the REST client should also offer the option of specifying a time-to-live (TTL). This determines how long a short URL can be reached before it is deleted from the database. If no TTL is parameterized in the request, the short URL will remain forever.
With an HTTP-GET request on the route of the ID (e.g. http://localhost:8080/id), the service should redirect the REST client to the long URL. It should also be possible to delete short URLs based on their IDs.

### Local Deployment
Here are the requirement to run this project locally on your machine.

#### Dependencies
- Docker.
- JDK 17
- Gradle
- Kubernetes

#### How to configure the application
- Copy the `.env.local` to `.env` and update the file accordingly.
- Build the application by running `./gradlew build`

#### How to run the application on Docker
- Run `docker compose up -d` this should start the application

#### How to run the application on Kubernetes
- Run `helm install url-shortener ./helm-chart`
- If deployed on minikube, run `minikube tunnel` to expose the application

#### How to use the application
- Visit `http://localhost:8080/swagger-ui.html` to access the api documentation


