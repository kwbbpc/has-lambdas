package com.broadway.has.lambda.weather.averager;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.broadway.has.lambda.weather.averager.temperature.HourlyTemperatureAverager;
import com.fasterxml.jackson.databind.JsonNode;




public class LambdaFunctionHandler implements RequestHandler<DynamodbEvent, String> {

	
	private static final Logger logger = LogManager.getLogger(LambdaFunctionHandler.class); 
	
    @Override
    public String handleRequest(DynamodbEvent event, Context context) {

    	try {

    		logger.debug("New dynamoDB event: {}", event.toString());
    		WeatherAverager averager = new HourlyTemperatureAverager();
	
	        for(DynamodbEvent.DynamodbStreamRecord record : event.getRecords()) {
	
	
	            try {
	
	                String l = JsonUtils.MAPPER.writeValueAsString(JsonUtils.MAPPER.valueToTree(record.getDynamodb()));
	                logger.info("Got new record: " + l);
	
	                Item coreItem = ItemUtils.toItem(record.getDynamodb().getNewImage());
	                JsonNode coreRecord = JsonUtils.MAPPER.valueToTree(coreItem.asMap());
	
	
	                //handle the temperature average
	                averager.average(coreRecord);
	
	
	                //handle the humidity average
	
	            }catch (Exception e){
	                logger.error("Error getting record: {}", e);
	            }
        	
	        }
    	}catch(Exception e) {
    		logger.error("There was a fatal exception processing this request: {}",  e);
    	}
        return null;
    }
}
