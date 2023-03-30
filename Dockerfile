FROM openjdk:17
EXPOSE 8080
ARG JAR_FILE=target/car-lease-platform-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} car-lease-platform.jar
ENTRYPOINT ["java","-jar","/car-lease-platform.jar"]