FROM clojure as buildStage

LABEL stage="builder"

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN apt-get -y update

RUN lein deps

RUN lein uberjar

FROM openjdk:11

WORKDIR /app

COPY --from=buildStage /usr/src/app/target/carimbo-0.1.0-SNAPSHOT-standalone.jar  /app/carimbo.jar

RUN apt update; apt-get -y install liblmdb-dev

CMD ["java", "-jar", "carimbo.jar"]