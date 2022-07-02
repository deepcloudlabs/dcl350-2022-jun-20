package com.example.crm.events;

public class CustomerAcquiredEvent extends CustomerEvent {

	public CustomerAcquiredEvent(Object eventData, String conversationId) {
		super(eventData, EventType.ACQUIRED, conversationId);
	}

}
