FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Install Gradle manually
RUN apt-get update && apt-get install -y wget unzip && rm -rf /var/lib/apt/lists/*
RUN wget https://services.gradle.org/distributions/gradle-8.1-bin.zip -P /tmp
RUN unzip -d /opt/gradle /tmp/gradle-8.1-bin.zip
ENV PATH=/opt/gradle/gradle-8.1/bin:$PATH

# Copy the Gradle wrapper and the build scripts
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Build the app
COPY src src
RUN chmod +x gradlew
RUN ./gradlew clean build

# Runtime stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*SNAPSHOT.jar app.jar

# Expose tomcat port and run the app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
