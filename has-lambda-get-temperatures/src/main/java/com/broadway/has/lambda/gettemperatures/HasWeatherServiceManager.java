package com.broadway.has.lambda.gettemperatures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.broadway.has.lambda.gettemperatures.db.DatabaseManager;
import com.broadway.has.lambda.gettemperatures.db.DynamoDbDatabase;
import com.broadway.has.lambda.gettemperatures.request.TemperatureRequest;
import com.broadway.has.lambda.gettemperatures.response.TemperatureResponse;

public class HasWeatherServiceManager {

    private static Logger logger = LoggerFactory.getLogger(HasWeatherServiceManager.class);

    private DatabaseManager db;

    private final int MAX_RETURN_LIMIT = 10000;

    public HasWeatherServiceManager(){
    	this.db = new DynamoDbDatabase();
    }


    public String pong(){
        return "pong";
    }

    public TemperatureResponse getTemperatures(TemperatureRequest request) throws Exception{


        //grab temps from the database
        TemperatureResponse r = this.db.getTemperatures(request.getNodeId(), request.getStartTime(), request.getEndTime(), MAX_RETURN_LIMIT);

        
        System.out.println("returning response.");
        return r;
    }

}
