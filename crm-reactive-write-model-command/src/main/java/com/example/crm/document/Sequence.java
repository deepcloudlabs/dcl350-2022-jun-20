package com.example.crm.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "sequences")
@Data
public class Sequence {
	@Id
	private String id;
	private long value;
}
