package com.broadway.has.lambda.gethumidity;

import java.util.Map;

import com.amazonaws.services.apigateway.model.BadRequestException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.broadway.has.lambda.gethumidity.request.HumidityRequest;
import com.broadway.has.lambda.gethumidity.response.HumidityResponse;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GetHumidity implements RequestHandler<Map<String, Object>, HumidityResponse> {

	private static final Logger logger = LogManager.getLogger(GetHumidity.class);

    @Override
    public HumidityResponse handleRequest(Map<String, Object> input, Context context) {
        HasWeatherServiceManager manager = new HasWeatherServiceManager();
        


        HumidityResponse tempResponse = null;
        try {
			logger.info("Received new request: {}", JsonUtils.MAPPER.writeValueAsString(input));

			tempResponse = manager.getHumidity(new HumidityRequest(input));
			ObjectNode headerJson = JsonUtils.MAPPER.createObjectNode();
	        headerJson.put("x-custom-header", "my custom header value");
	        headerJson.put("Content-Type", "application/json");
	        
	        ObjectNode responseJson = JsonUtils.MAPPER.createObjectNode();
	        responseJson.put("statusCode", 500);
	        responseJson.set("headers", headerJson);
	        responseJson.set("body", JsonUtils.MAPPER.valueToTree(tempResponse));
	        
	        return tempResponse;
			
		} catch (BadRequestException e) {
			logger.error("Bad request exception while processing request: {}", e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Fatal exception processing request: {}", e);
		}
        
        
        return tempResponse;
		
        
        
        
    }


}
