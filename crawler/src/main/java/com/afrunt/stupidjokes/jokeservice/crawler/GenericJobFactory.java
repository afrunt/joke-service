package com.afrunt.stupidjokes.jokeservice.crawler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Andrii Frunt
 */
@Component
public class GenericJobFactory {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ItemProcessor<String, CrawlerJokeEntity> stringToJokeEntityMapper;
    
    @Autowired
    private RepositoryItemWriter<CrawlerJokeEntity> writer;
    
    @Autowired
    private Step afterJokesSavedToLocalDBStep;
    
    @Autowired
    private JobExecutionListener listener;

    public Job create(String name, ItemReader<String> reader) {
        return jobBuilderFactory.get(name + "Job")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(createFirstStep(name + "FirstStep", reader))
                .next(afterJokesSavedToLocalDBStep)
                .end()
                .build();
    }

    private Step createFirstStep(String name, ItemReader<String> reader) {
        return stepBuilderFactory
                .get(name)
                .<String, CrawlerJokeEntity>chunk(1000)
                .reader(reader)
                .processor(stringToJokeEntityMapper)
                .writer(writer)
                .build();
    }
}
