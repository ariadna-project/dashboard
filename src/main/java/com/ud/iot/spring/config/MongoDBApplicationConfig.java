package com.ud.iot.spring.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@Profile("!dev")
@PropertySource({"classpath:mongo.properties"})
public class MongoDBApplicationConfig extends AbstractMongoClientConfiguration{
	
	private static Logger logger = LoggerFactory.getLogger(MongoDBApplicationConfig.class);
	
	@Value("${mongo.dbname}") protected String dbName;
	@Value("${mongo.url}") protected String url;
	
	@PostConstruct
	public void init() {
		logger.info("Iniciando la configuración mongo para el dashboard");
	}

	@Override
	protected String getDatabaseName() {
		return this.dbName;
	}
	
	@Override
	public MongoClient mongoClient() {
		ConnectionString connectionString = new ConnectionString(this.url);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        
        return MongoClients.create(mongoClientSettings);
	};
}