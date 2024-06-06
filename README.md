# NewsSummarizerBot

add .env file in this format:
```
TELEGRAM_API_TOKEN=<your telegram token>
TELEGRAM_API_ADDRESS=<your telegram bot address (is not currently used)>
SENTRY_DSN=<your sentry dsn>
YANDEX_FOLDER=<your yandex cloud folder>
YANDEX_API_KEY=<your yandex foundation models api key>
```

And then run with docker:
```
docker build . --file Dockerfile --tag news-summarizer-bot:latest

docker run --rm news-summarizer-bot:latest ./gradlew run

# to run tests use
docker run --rm news-summarizer-bot:latest ./gradlew unit-test
docker run --rm news-summarizer-bot:latest ./gradlew integration-test

# or to run all tests:
docker run --rm news-summarizer-bot:latest ./gradlew tests
```