package com.broadway.has.lambda.gethumidity;

import com.broadway.has.lambda.gethumidity.db.DatabaseManager;
import com.broadway.has.lambda.gethumidity.db.DynamoDbDatabase;
import com.broadway.has.lambda.gethumidity.request.HumidityRequest;
import com.broadway.has.lambda.gethumidity.response.HumidityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public HumidityResponse getHumidity(HumidityRequest request) throws Exception{


        //grab temps from the database
        HumidityResponse r = this.db.getHumidity(request.getNodeId(), request.getStartTime(), request.getEndTime(), MAX_RETURN_LIMIT);

        
        System.out.println("returning response.");
        return r;
    }

}
