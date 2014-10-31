package com.rippmn.halloween.service;

import java.util.Date;

import com.rippmn.halloween.domain.TTEvent;

public interface TrickOrTreatEventService {

	public TTEvent[] getAllTTEvents();
	
	public TTEvent[] getEventsAfterDate(Date d);
	
	public void trickOrTreatEvent(int count);
}
