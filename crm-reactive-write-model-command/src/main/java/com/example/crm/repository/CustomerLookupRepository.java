package com.example.crm.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.crm.document.CustomerLookup;

public interface CustomerLookupRepository extends ReactiveMongoRepository<CustomerLookup, String>{

}
