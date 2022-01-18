FROM clojure:openjdk-16-tools-deps-slim-buster as builder

WORKDIR /app
COPY deps.edn .
COPY src ./src
COPY resources ./resources
COPY uberdeps ./uberdeps

WORKDIR uberdeps
RUN clojure -M -m uberdeps.uberjar --deps-file ../deps.edn --target ../target/app.jar

FROM openjdk:16-slim-buster

COPY --from=builder /app/target/app.jar /app.jar
CMD ["java", "-cp", "/app.jar", "clojure.main", "-m", "app-server.main"]
