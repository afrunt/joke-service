#!/usr/bin/env sh
set -e

APM_PARAMETERS=""
if [ "${APM_ENABLED}" = "true" ]
then
    APM_PARAMETERS="-Delastic.apm.server_urls=${APM_SERVER_URLS} -Delastic.apm.service_name=${APM_SERVICE_NAME} -Delastic.apm.application_packages=${APM_APPLICATION_PACKAGES} -Delastic.apm.enable_log_correlation=true  -javaagent:/deployment/agents/elastic-apm-agent.jar"
fi

exec java -Xmx${MEM_MAX} -Dspring.profiles.active="${PROFILES}" ${APM_PARAMETERS} -Dserver.port=${SERVER_PORT} \
    -Dspring.datasource.url=${DB_URL} -Dspring.datasource.username=${DB_USERNAME} -Dspring.datasource.password=${DB_PASSWORD} \
    -Djokeservice.url=${JOKE_SERVICE_URL} \
    -Dcrawler.jobs.randomjokecrawler.batchSize=${RANDOMJOKECRAWLER_BATCH_SIZE} -Dcrawler.jobs.randomjokecrawler.bufferSize=${RANDOMJOKECRAWLER_BUFFER_SIZE} \
    ${JAVA_OPTS} \
    $@ \
    -jar /deployment/application.jar

