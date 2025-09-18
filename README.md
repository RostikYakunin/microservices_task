Docker 

1) ./gradlew clean build
2) docker compose down
3) docker compose up -d --build --scale song-service=2
4) docker ps
5) docker compose logs -f resource-service song-service
6) http://localhost:8761
7) docker compose down

Local

1) docker compose up -d resource-db song-db
2) docker ps
3) servers star
./gradlew :eureka-server:bootRun
./gradlew :resource-service:bootRun
./gradlew :song-service:bootRun

4) http://localhost:8761