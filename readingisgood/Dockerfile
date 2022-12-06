FROM maven:3.8.3-openjdk-17 as builder

COPY . /app
WORKDIR /app
RUN mvn clean package

FROM amazoncorretto:17

COPY --from=builder /app/target/readingisgood-0.0.1-SNAPSHOT.jar /readingisgood-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/readingisgood-0.0.1-SNAPSHOT.jar"]