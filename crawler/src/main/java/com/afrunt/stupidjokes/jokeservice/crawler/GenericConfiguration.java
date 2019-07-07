package com.afrunt.stupidjokes.jokeservice.crawler;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * @author Andrii Frunt
 */
@Configuration
public class GenericConfiguration {
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemProcessor<String, CrawlerJokeEntity> stringToJokeEntityMapper() {
        return joke -> {
            if (joke.length() > 50000) {
                joke = joke.substring(0, 50000);
            }

            return new CrawlerJokeEntity()
                    .setBody(joke);
        };
    }

    @Bean("afterJokesSavedToLocalDBStep")
    public Step afterJokesSavedToLocalDBStep(RepositoryItemReader<CrawlerJokeEntity> reader, CrawlerJokeEntityRESTWriter writer, ThreadPoolTaskExecutor taskExecutor) {
        return stepBuilderFactory
                .get("afterJokesSavedToLocalDBStep")
                .<CrawlerJokeEntity, CrawlerJokeEntity>chunk(20000)
                .reader(reader)
                .writer(writer)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public RepositoryItemWriter<CrawlerJokeEntity> writer(CrawlerJokeEntityRepository repository) {
        RepositoryItemWriter<CrawlerJokeEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");

        return writer;
    }

    @Bean
    public RepositoryItemReader<CrawlerJokeEntity> crawlerJokeEntityRepositoryItemReader(CrawlerJokeEntityRepository repository) {
        RepositoryItemReader<CrawlerJokeEntity> reader = new RepositoryItemReader<>();
        reader.setRepository(repository);
        reader.setSort(Map.of("id", Sort.Direction.ASC));
        reader.setMethodName("findAll");
        reader.setPageSize(10000);
        return reader;
    }
}
