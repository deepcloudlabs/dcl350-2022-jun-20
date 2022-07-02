package com.example.crm.events;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@Document(collection = "events")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
	@Type(value = CustomerReleasedEvent.class, name = "customer-released"), 
	@Type(value = CustomerUpdatedEvent.class, name = "customer-updated"), 
	@Type(value = CustomerAcquiredEvent.class, name = "customer-acquired") 
})
@Data
public class CustomerEvent {
	@Transient
	public static final String SEQUENCE_NAME = "crm_event_sequence";

	@Id
	private String eventId;
	private Object eventData;
	private EventType eventType;
	private long sequence;
	private long timestamp;
	private String conversationId; // transactionId, txid

	public CustomerEvent(Object eventData, EventType eventType, String conversationId,long sequence) {
		this.eventId = UUID.randomUUID().toString();
		this.eventData = eventData;
		this.eventType = eventType;
		this.conversationId = conversationId;
		this.sequence = sequence;
		this.timestamp = ZonedDateTime.now().toEpochSecond();
	}

}
