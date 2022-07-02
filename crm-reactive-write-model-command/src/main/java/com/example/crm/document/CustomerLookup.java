package com.example.crm.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="customer_lookup")
@Data
@NoArgsConstructor
public class CustomerLookup {
	@Id
	private String customerId;

	
	public CustomerLookup(String customerId) {
		this.customerId = customerId;
	}
	
}
