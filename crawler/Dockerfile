FROM adoptopenjdk/openjdk12:alpine-slim

RUN mkdir /deployment && apk --no-cache add curl

ADD https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/1.6.1/elastic-apm-agent-1.6.1.jar /deployment/agents/elastic-apm-agent.jar

COPY entrypoint.sh /deployment/

COPY target/crawler.jar /deployment/application.jar

RUN chmod +x /deployment/entrypoint.sh

ENV PROFILES="release,full"\
    MEM_MAX="386m"\
    JAVA_OPTS=""\
    APM_ENABLED=false\
    APM_SERVER_URLS=""\
    APM_SERVICE_NAME="joke-service-crawler"\
    APM_APPLICATION_PACKAGES="com.afrunt"\
    JOKE_SERVICE_URL="http://localhost:8080"\
    RANDOMJOKECRAWLER_BATCH_SIZE=100000\
    RANDOMJOKECRAWLER_BUFFER_SIZE=1000\
    DB_URL="jdbc:h2:/db/jokedb;DB_CLOSE_ON_EXIT=FALSE"\
    DB_USERNAME="sa"\
    DB_PASSWORD="password"
    

WORKDIR /deployment/

ENTRYPOINT ["/deployment/entrypoint.sh"]
CMD []
