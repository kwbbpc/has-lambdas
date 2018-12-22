package com.broadway.has.lambda.weather.averager.temperature.daos;

import org.joda.time.DateTime;

public class UpdateRecord {
	
	private DateTime originalCreatedDate;
	private DateTime averagedDate;
	
	public UpdateRecord() {
		this.originalCreatedDate = DateTime.now();
		this.averagedDate = DateTime.now();
	}
	
	
	public UpdateRecord(DateTime originalCreatedDate, DateTime averagedDate) {
		super();
		this.originalCreatedDate = originalCreatedDate;
		this.averagedDate = averagedDate;
	}
	public DateTime getOriginalCreatedDate() {
		return originalCreatedDate;
	}
	public void setOriginalCreatedDate(DateTime originalCreatedDate) {
		this.originalCreatedDate = originalCreatedDate;
	}
	public DateTime getAveragedDate() {
		return averagedDate;
	}
	public void setAveragedDate(DateTime averagedDate) {
		this.averagedDate = averagedDate;
	}
	
	
	

}
