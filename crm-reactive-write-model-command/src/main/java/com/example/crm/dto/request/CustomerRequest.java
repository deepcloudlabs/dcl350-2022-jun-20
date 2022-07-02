package com.example.crm.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequest {
	private String customerId;
	private String fullName;
	private String email;
	private String phone;
	private int birthYear;
}
