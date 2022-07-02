package com.example.crm.events;

public class CustomerAcquiredEvent extends CustomerEvent {

	public CustomerAcquiredEvent(Object eventData, String conversationId,long sequence) {
		super(eventData, EventType.ACQUIRED, conversationId,sequence);
	}

}
