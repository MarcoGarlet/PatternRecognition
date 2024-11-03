# Usa un'immagine Gradle con Java 17 come base
FROM gradle:7.5.1-jdk17 AS app
WORKDIR /app

COPY . .

RUN gradle clean build -x test

CMD ["sh", "start.sh"]
