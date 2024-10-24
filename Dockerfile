FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
WORKDIR /home/app

COPY ./pom.xml /home/app/pom.xml
COPY ./src/main/java/com/sadi/hackathon_authservice/HackathonAuthserviceApplication.java /home/app/src/main/java/com/sadi/hackathon_authservice/HackathonAuthserviceApplication.java

RUN mvn -f /home/app/pom.xml clean package

COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM maven:3.9.7-eclipse-temurin-21-alpine
EXPOSE 8100
COPY --from=build /home/app/target/*.jar authservice.jar
ARG profile=dev
RUN echo "profile: ${profile}"
ENV spring_profiles_active=${profile}
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar .
ENV JAVA_TOOL_OPTIONS "-javaagent:./opentelemetry-javaagent.jar"
ENTRYPOINT ["sh", "-c", "java -jar /authservice.jar"]