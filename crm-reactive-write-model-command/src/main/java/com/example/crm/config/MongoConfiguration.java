package com.example.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.reactivestreams.client.MongoClient;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.example.crm.repository")
public class MongoConfiguration {
	@Bean
	ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) throws Exception {
		return new ReactiveMongoTemplate(mongoClient, "crm-write");
	}

	@Bean
	ReactiveMongoOperations mongoOperations(ReactiveMongoTemplate mongoTemplate) throws Exception {
		return mongoTemplate;
	}
}
