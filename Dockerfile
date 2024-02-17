FROM clojure as buildStage

LABEL stage="builder"

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN apt-get -y update

RUN lein deps

RUN lein uberjar

FROM amazoncorretto:11-alpine

WORKDIR /app

COPY --from=buildStage /usr/src/app/target/carimbo-0.1.0-SNAPSHOT-standalone.jar  /app/carimbo.jar

RUN apk add lmdb

CMD ["java", "-jar", "carimbo.jar"]