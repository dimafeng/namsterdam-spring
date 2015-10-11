package com.github.dimafeng.namsterdam.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.tautua.markdownpapers.Markdown;

//@Import(PropertiesConfig.class)
@Configuration
@ComponentScan(basePackages = "com.github.dimafeng.namsterdam")
@EnableMongoRepositories(basePackages = "com.github.dimafeng.namsterdam.dao")
//@EnableAspectJAutoProxy(proxyTargetClass = false)
public class AppConfig extends AbstractMongoConfiguration {

    @Value("${mongo.db.name:namsterdam}")
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
