FROM java:8

RUN apt-get update
RUN apt-get install -y git
RUN git clone https://github.com/dimafeng/namsterdam-spring.git

WORKDIR namsterdam-spring

RUN chmod +x ./gradlew
RUN ./gradlew compileJava

RUN echo $MONGO_PORT_27017_TCP_ADDR:$MONGO_PORT_27017_TCP_PORT

EXPOSE 8080
CMD ["./gradlew", "-Dmongo.db.server=$MONGO_PORT_27017_TCP_ADDR:$MONGO_PORT_27017_TCP_PORT", "run"]