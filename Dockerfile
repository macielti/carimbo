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

CMD ["java", "--add-opens=java.base/java.nio=ALL-UNNAMED", "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED", "-Dcom.sun.management.jmxremote=true", "-Dcom.sun.management.jmxremote.port=9010", "-Dcom.sun.management.jmxremote.local.only=false", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.rmi.port=9010", "-Djava.rmi.server.hostname=localhost", "-jar", "carimbo.jar"]