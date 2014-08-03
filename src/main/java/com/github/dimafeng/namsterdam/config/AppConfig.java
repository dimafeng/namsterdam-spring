package com.github.dimafeng.namsterdam.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.tautua.markdownpapers.Markdown;

@Configuration
@ComponentScan(basePackages = "com.github.dimafeng.namsterdam")
@EnableMongoRepositories(basePackages = "com.github.dimafeng.namsterdam.dao")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig extends AbstractMongoConfiguration {
    @Override
    public String getDatabaseName() {
        return "nams";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("127.0.0.1");
    }

    @Bean
    public Markdown markdownProcessor() {
        return new Markdown();
    }


}
