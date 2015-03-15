package com.github.dimafeng.namsterdam.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.im4java.core.ConvertCmd;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.tautua.markdownpapers.Markdown;

@Import(PropertiesConfig.class)
@Configuration
@ComponentScan(basePackages = "com.github.dimafeng.namsterdam")
@EnableMongoRepositories(basePackages = "com.github.dimafeng.namsterdam.dao")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig extends AbstractMongoConfiguration {

    @Value("${mongo.db.name}")
    private String databaseName;

    @Value("${mongo.db.server}")
    private String databaseServer;

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(databaseServer);
    }

//    @Bean
//    public ConvertCmd convertCmd()
//    {
//        return new ConvertCmd();
//    }
    
    @Bean
    public Markdown markdownProcessor() {
        return new Markdown();
    }
}
