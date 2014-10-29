package com.rippmn.halloween;

import java.util.List;

public interface TrickOrTreaterReportService {

	public List<TrickOrTreatReportingEvent> getAllEvents();

	public void updateReport();
}
