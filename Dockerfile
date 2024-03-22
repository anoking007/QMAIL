FROM openjdk:17-jdk-slim

EXPOSE 8080

MAINTAINER Gautham

COPY target/kyberJCE-3.0.0.jar kyberJCE-3.0.0.jar

ENTRYPOINT ["java","-jar","kyberJCE-3.0.0.jar"]