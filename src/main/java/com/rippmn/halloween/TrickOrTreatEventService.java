package com.rippmn.halloween;

import java.util.Date;

public interface TrickOrTreatEventService {

	public TTEvent[] getAllTTEvents();
	
	public TTEvent[] getEventsAfterDate(Date d);
}
