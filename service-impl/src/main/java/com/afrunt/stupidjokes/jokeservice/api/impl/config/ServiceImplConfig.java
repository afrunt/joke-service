package com.afrunt.stupidjokes.jokeservice.api.impl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Andrii Frunt
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.afrunt.stupidjokes.jokeservice.api.impl.repository")
public class ServiceImplConfig {

}
