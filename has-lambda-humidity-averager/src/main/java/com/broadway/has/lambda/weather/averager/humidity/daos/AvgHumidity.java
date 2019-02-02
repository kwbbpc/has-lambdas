package com.broadway.has.lambda.weather.averager.humidity.daos;

public class AvgHumidity {
	
	private int numberOfPoints;
	private float humidity;
	private String units;
	
	
	
	public AvgHumidity() {
		super();
		this.numberOfPoints = 0;
		this.humidity = 0f;
		this.units = "0";
	}
	public AvgHumidity(int numberOfPoints, float humidity, String units) {
		super();
		this.numberOfPoints = numberOfPoints;
		this.humidity = humidity;
		this.units = units;
	}
	public int getNumberOfPoints() {
		return numberOfPoints;
	}
	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	
	
	
}
