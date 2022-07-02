package com.example.crm.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crm.document.CustomerDocument;
import com.example.crm.service.ReactiveReadCrmService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@CrossOrigin
@Validated
public class CrmRestController {

	private final ReactiveReadCrmService crmService;
	
	public CrmRestController(ReactiveReadCrmService crmService) {
		this.crmService = crmService;
	}

	@GetMapping("{customerId}")
	public Mono<CustomerDocument> getCustomerById(@PathVariable String customerId) {
		return crmService.findById(customerId);
	}

	@GetMapping(params = {"pageNo","pageSize"})
	public Flux<CustomerDocument> getCustomerByPage(@RequestParam int pageNo, @RequestParam int pageSize) {
		return crmService.findAllByPage(pageNo,pageSize);
	}
	
}
