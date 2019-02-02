package com.broadway.has.lambda.weather.averager;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.broadway.has.lambda.weather.averager.humidity.HourlyHumidityAverager;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LambdaFunctionHandler implements RequestHandler<DynamodbEvent, String> {

	
	private static final Logger logger = LogManager.getLogger(LambdaFunctionHandler.class); 
	
    @Override
    public String handleRequest(DynamodbEvent event, Context context) {

    	try {

    		logger.debug("New dynamoDB event: {}", event.toString());
    		Averager averager = new HourlyHumidityAverager();
	
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
