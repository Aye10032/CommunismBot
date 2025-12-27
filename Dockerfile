FROM eclipse-temurin:17-jre-alpine

ENV TZ=Asia/Shanghai
RUN apk add --no-cache tzdata && \
    apk add --no-cache ca-certificates && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

WORKDIR /app

COPY app.jar ./app.jar

RUN mkdir -p /app/config

EXPOSE 8080

ENTRYPOINT ["java", \
    "-Dfile.encoding=UTF-8", \
    "-Xmx1024m", \
    "-jar", \
    "./app.jar", \
    "--spring.profiles.active=prod", \
    "--spring.config.additional-location=file:/app/config/application.properties"]