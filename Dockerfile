FROM openjdk:17-jre-alpine

ENV TZ=Asia/Shanghai
RUN apk add --no-cache tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

WORKDIR /app

COPY build/libs/CommunismBot-*.jar ./app.jar

RUN mkdir -p /app/config

EXPOSE 8080

ENTRYPOINT ["java", \
    "-Dfile.encoding=UTF-8", \
    "-Xmx1024m", \
    "-jar", \
    "./app.jar", \
    "--spring.profiles.active=prod", \
    "--spring.config.additional-location=file:/app/config/application.properties"]