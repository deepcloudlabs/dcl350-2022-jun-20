package com.example.crm.listener;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.crm.document.CustomerDocument;
import com.example.crm.events.CustomerEvent;
import com.example.crm.repository.CustomerDocumentReactiveRepository;
import com.example.crm.repository.CustomerEventReactiveRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("rawtypes")
@Service
public class CrmEventRabbitListener {
	private final CustomerDocumentReactiveRepository customerDocumentReactiveRepository;
	private final CustomerEventReactiveRepository customerEventReactiveRepository;
	private final ObjectMapper objectMapper;

	public CrmEventRabbitListener(CustomerDocumentReactiveRepository customerDocumentReactiveRepository,
			CustomerEventReactiveRepository customerEventReactiveRepository, ObjectMapper objectMapper) {
		this.customerDocumentReactiveRepository = customerDocumentReactiveRepository;
		this.customerEventReactiveRepository = customerEventReactiveRepository;
		this.objectMapper = objectMapper;
	}


	@RabbitListener(queues = "${crm.event.rabbit.queue}")
	public void listen(String event) throws JsonMappingException, JsonProcessingException {
		var customerEvent = objectMapper.readValue(event, CustomerEvent.class);
		System.out.println(event);
		System.out.println(customerEvent.getClass());
		System.out.println(customerEvent.getEventData().getClass());
		customerEventReactiveRepository.insert(customerEvent).subscribe();
		switch (customerEvent.getEventType()) {
		case ACQUIRED -> {
			if (customerEvent.getEventData() instanceof Map map) {
				var customerDocument = new CustomerDocument();
				customerDocument.setCustomerId(map.get("customerId").toString());
				customerDocument.setEmail(map.get("email").toString());
				customerDocument.setFullName(map.get("fullName").toString());
				customerDocument.setPhone(map.get("phone").toString());
				customerDocument.setBirthYear(Integer.parseInt(map.get("birthYear").toString()));
				customerDocumentReactiveRepository.insert(customerDocument).subscribe(System.err::println);
			}
		}
		case RELEASED -> {
			if (customerEvent.getEventData() instanceof String customerId) {
				customerDocumentReactiveRepository.deleteById(customerId).subscribe(System.err::println);				
			}
		}
		case UPDATED -> {
			if (customerEvent.getEventData() instanceof Map map) {
				var customerDocument = new CustomerDocument();
				customerDocument.setCustomerId(map.get("customerId").toString());
				customerDocument.setEmail(map.get("email").toString());
				customerDocument.setFullName(map.get("fullName").toString());
				customerDocument.setPhone(map.get("phone").toString());
				customerDocument.setBirthYear(Integer.parseInt(map.get("birthYear").toString()));
				customerDocumentReactiveRepository.save(customerDocument).subscribe(System.err::println);
			}
		}
		}
	}
}
