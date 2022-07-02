package com.example.crm.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crm.dto.request.CustomerRequest;
import com.example.crm.events.CustomerAcquiredEvent;
import com.example.crm.events.CustomerReleasedEvent;
import com.example.crm.events.CustomerUpdatedEvent;
import com.example.crm.service.ReactiveWriteCrmService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@CrossOrigin
@Validated
public class CrmRestController {

	private final ReactiveWriteCrmService crmService;
	
	public CrmRestController(ReactiveWriteCrmService crmService) {
		this.crmService = crmService;
	}
	
	@PostMapping(params="conversationId")
	public Mono<CustomerAcquiredEvent> acquireCustomer(@RequestBody CustomerRequest customer,@RequestParam String conversationId){
		return crmService.acquireCustomer(customer,conversationId);
	}
	
	@PutMapping(params="conversationId")
	public Mono<CustomerUpdatedEvent> updateCustomer(@RequestBody CustomerRequest customer,@RequestParam String conversationId){
		return crmService.updateCustomer(customer,conversationId);
	}
	
	@DeleteMapping(value="{customerId}",params="conversationId")
	public Mono<CustomerReleasedEvent> releaseCustomerById(@PathVariable String customerId,@RequestParam String conversationId) {
		return crmService.releaseCustomerById(customerId,conversationId);
	}
	
}
