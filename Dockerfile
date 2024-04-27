FROM gradle:8.7.0-jdk21
WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew assemble

CMD ["./gradlew", "run"]
CMD ["./gradlew", "test"]