package com.example.crm.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.crm.document.CustomerDocument;
import com.example.crm.repository.CustomerDocumentReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveReadCrmService {
	private final CustomerDocumentReactiveRepository documentRepository;
	
	public ReactiveReadCrmService(CustomerDocumentReactiveRepository documentRepository) {
		this.documentRepository = documentRepository;
	}

	public Mono<CustomerDocument> findById(String customerId) {
		return documentRepository.findById(customerId);
	}

	public Flux<CustomerDocument> findAllByPage(int pageNo, int pageSize) {
		return documentRepository.findAll(PageRequest.of(pageNo, pageSize));
	}
}
