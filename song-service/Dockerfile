# build stage
FROM gradle:8.6-jdk17-alpine AS build
WORKDIR /app

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew assemble --no-daemon -x test

# runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8072

CMD ["java", "-jar", "/app/app.jar"]
