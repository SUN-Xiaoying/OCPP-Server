# OCPP Server

## Todo

-[ ] build postgres:ocpp in docker `docker compose up`
-[ ] Start service and dependencies `./gradlew startDependencies`
-[ ] Junit tests  `./gradlew test`
-[ ] Build docker image `./gradlew dockerBuild`

## Dependency

| Role                                     | Software                  |
|------------------------------------------|---------------------------|
| Back-end Development Framework           | Spring Boot               |
| Programming Language                     | Java 11                   |
| Web Engine                               | Thymeleaf                 |
| Integrated Development Environment (IDE) | Intellij IDEA (Optional)  |
| Web Deployment                           | Amazon Elastic Beanstalk  |
| Open-source Database                     | Amazon RDS MySQL ／ MySQL |


## Running the service

### Running local profile

- Create local database `ocpp` with MySQL
- Attach the URL, username, and password in `/src/main/resources/application.properties`
- Run `CentralSystemApplication`

### Running towards production

- Create cloud database with Amazon RDS MySQL or Aurora
- Attach the URL, username, and password in `/src/main/resources/application.properties`
- Build jar file with Maven deploy,`target/csms-0.0.1-SNAPSHOT.jar`
- Upload the jar on Amazon Elastic Beanstalk
- Open provided website