# Az-Covid19-Analysis-API

> Tools and Technologies:
* Java 1.8
* Spring Boot 2.5.6
* Spring Security 2.5.6
* Spring Data JPA 2.5.6
* JsonWebToken 0.9.1
* H2 (In-Memory DB)
* SpringDoc-Openapi-UI 1.5.12
  * Swagger-UI 3.52.5
* Maven
  * Maven plugin JaCoCo 0.8.7
  * Maven plugin Sonar 3.9.1.2184
* Intellij IDEA

> Application main flows
##### 1. CsvFileScheduler:
* `CsvFileScheduler` will be started just after application up and running.
* It will download CSV file and store locally.
* Every 6Hr it will repeat the same process to keep updated CSV file.
##### 2. UserController:
* For accessing all the endpoints we need JWT.
* For JWT, we can pass any username/password.
> Resources
##### UserController 

##### How To Run:
```
./mvnw install spring-boot:run
```