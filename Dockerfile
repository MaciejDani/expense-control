FROM openjdk:21
WORKDIR /expense-control-app
COPY target/expense-control-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]