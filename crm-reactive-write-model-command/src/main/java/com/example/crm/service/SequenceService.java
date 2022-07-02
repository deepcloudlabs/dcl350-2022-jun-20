package com.example.crm.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.crm.document.Sequence;

import reactor.core.publisher.Mono;

@Service
public class SequenceService {
	private final ReactiveMongoOperations mongoOperations;

	public SequenceService(ReactiveMongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public Mono<Long> generateSequence(String sequenceName) {
		System.err.println("generateSequence");
		return mongoOperations.findAndModify(query(where("_id").is(sequenceName)),
				new Update().inc("value", 1), options().returnNew(true).upsert(true), Sequence.class).map(Sequence::getValue);
	}
}
