FROM adoptopenjdk/openjdk12:alpine-slim

RUN mkdir /deployment

COPY entrypoint.sh /deployment/

ADD https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/1.6.1/elastic-apm-agent-1.6.1.jar /deployment/agents/elastic-apm-agent.jar

COPY target/rest.jar /deployment/application.jar

RUN chmod +x /deployment/entrypoint.sh

ENV PROFILES="release"\
    MEM_MAX="256m"\
    SERVER_PORT=8080\
    MANAGEMENT_SERVER_PORT=9090\ 
    APM_ENABLED=false\
    APM_SERVER_URLS=""\
    APM_SERVICE_NAME="joke-service"\
    APM_APPLICATION_PACKAGES="com.afrunt"\
    DB_URL="jdbc:postgresql://postgres:5432/postgres"\
    DB_USERNAME="postgres"\
    DB_PASSWORD="postgres"

ENTRYPOINT ["/deployment/entrypoint.sh"]
CMD[]

WORKDIR /deployment

ENTRYPOINT ["/deployment/entrypoint.sh"]
