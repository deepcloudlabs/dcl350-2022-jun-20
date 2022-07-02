package com.example.crm.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.crm.document.CustomerLookup;
import com.example.crm.dto.request.CustomerRequest;
import com.example.crm.events.CustomerAcquiredEvent;
import com.example.crm.events.CustomerEvent;
import com.example.crm.events.CustomerReleasedEvent;
import com.example.crm.events.CustomerUpdatedEvent;
import com.example.crm.repository.CustomerEventRepository;
import com.example.crm.repository.CustomerLookupRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class ReactiveWriteCrmService {
	private final CustomerEventRepository customerEventRepository;
	private final CustomerLookupRepository customerLookupRepository;
	private final SequenceService sequenceService;
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	@Value("${crm.event.rabbitmq.exchange}")
	private String crmEventExchange;
	
	public ReactiveWriteCrmService(CustomerEventRepository customerEventRepository,
			CustomerLookupRepository customerLookupRepository, SequenceService sequenceService,
			RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
		this.customerEventRepository = customerEventRepository;
		this.customerLookupRepository = customerLookupRepository;
		this.sequenceService = sequenceService;
		this.rabbitTemplate = rabbitTemplate;
		this.objectMapper = objectMapper;
	}

	public Mono<CustomerAcquiredEvent> acquireCustomer(CustomerRequest customer,String conversationId) {
		var customerId= customer.getCustomerId();
		System.err.println(customerId);
		return Mono.just(customerId)
				   .filterWhen( custId -> customerLookupRepository.existsById(custId).map(b -> !b))
				   .flatMap( custId -> customerLookupRepository.save(new CustomerLookup(custId)))
				   .flatMap( custId -> sequenceService.generateSequence(CustomerEvent.SEQUENCE_NAME))				   
				   .flatMap( sequence -> Mono.just(new CustomerAcquiredEvent(customer,conversationId,sequence)))
				   .doOnNext(customerEventRepository::insert)
				   .doOnNext( event -> {
				   	   try {
				          var json = objectMapper.writeValueAsString(event);
				       	  rabbitTemplate.convertAndSend(crmEventExchange, null, json);
				       } catch (JsonProcessingException e) {
				          System.err.println(e.getMessage());
				       	  Mono.error(e);
				       }
				    });
				                       
	}

	public Mono<CustomerUpdatedEvent> updateCustomer(CustomerRequest customer,String conversationId) {
		return Mono.just(customer.getCustomerId())
				   .filterWhen( custId -> customerLookupRepository.existsById(custId))
				   .flatMap( custId -> sequenceService.generateSequence(CustomerEvent.SEQUENCE_NAME))
				   .flatMap( sequence -> Mono.just(new CustomerUpdatedEvent(customer,conversationId,sequence)))
                   .doOnNext(customerEventRepository::insert)
                   .doOnNext( event -> {
						try {
							var json = objectMapper.writeValueAsString(event);
							rabbitTemplate.convertAndSend(crmEventExchange, null, json);
						} catch (JsonProcessingException e) {
       						System.err.println(e.getMessage());
       						Mono.error(e);						
       					}
		           });	
	}

	public Mono<CustomerReleasedEvent> releaseCustomerById(String customerId,String conversationId) {
		return Mono.just(customerId)
				   .filterWhen( custId -> customerLookupRepository.existsById(custId))
				   .flatMap( custId -> { 
					   customerLookupRepository.delete(new CustomerLookup(custId)).subscribe();
					   return Mono.just(custId);
				   })
				   .flatMap( custId -> sequenceService.generateSequence(CustomerEvent.SEQUENCE_NAME))
				   .flatMap( sequence -> Mono.just(new CustomerReleasedEvent(customerId,conversationId,sequence)))
                   .doOnNext(customerEventRepository::insert)
                   .doOnNext( event -> {
						try {
							var json = objectMapper.writeValueAsString(event);
							rabbitTemplate.convertAndSend(crmEventExchange, null, json);
						} catch (JsonProcessingException e) {
       						System.err.println(e.getMessage());
       						Mono.error(e);						
       					}
		           });	
	}
}
