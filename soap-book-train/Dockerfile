FROM maven:3.8.2-adoptopenjdk-11
COPY . .
RUN mvn clean package
ENTRYPOINT ["java", "-jar", "target/soap-book-train-0.0.1.jar"]
