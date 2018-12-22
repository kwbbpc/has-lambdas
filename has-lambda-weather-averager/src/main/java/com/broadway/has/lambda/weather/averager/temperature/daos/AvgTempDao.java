package com.broadway.has.lambda.weather.averager.temperature.daos;

public class AvgTempDao {
	
	private int numberOfPoints;
	private float temperature;
	private String units;
	
	
	
	public AvgTempDao() {
		super();
		this.numberOfPoints = 0;
		this.temperature = 0f;
		this.units = "0";
	}
	public AvgTempDao(int numberOfPoints, float temperature, String units) {
		super();
		this.numberOfPoints = numberOfPoints;
		this.temperature = temperature;
		this.units = units;
	}
	public int getNumberOfPoints() {
		return numberOfPoints;
	}
	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	
	
	
}
