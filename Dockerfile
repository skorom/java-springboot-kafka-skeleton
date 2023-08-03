FROM gradle:7.6-jdk17 AS build  
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle --no-daemon build

FROM openjdk:17-jdk-slim
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
EXPOSE 8080  
ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]