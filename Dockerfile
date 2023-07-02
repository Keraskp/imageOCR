FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install -y libtesseract-dev libleptonica-dev tesseract-ocr-eng
RUN apt-get install openjdk-17-jdk -y
COPY . .
RUN chmod +x mvnw
RUN chmod +x mvnw.cmd
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /build/libs/imageOCR-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]