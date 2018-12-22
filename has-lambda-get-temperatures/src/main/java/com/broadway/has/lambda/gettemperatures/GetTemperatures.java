package com.broadway.has.lambda.gettemperatures;

import java.util.Map;

import com.amazonaws.services.apigateway.model.BadRequestException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.broadway.has.lambda.gettemperatures.HasWeatherServiceManager;
import com.broadway.has.lambda.gettemperatures.request.TemperatureRequest;
import com.broadway.has.lambda.gettemperatures.response.TemperatureResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class GetTemperatures implements RequestHandler<Map<String, Object>, TemperatureResponse> {

    @Override
    public TemperatureResponse handleRequest(Map<String, Object> input, Context context) {
        HasWeatherServiceManager manager = new HasWeatherServiceManager();
        
        
        TemperatureResponse tempResponse = null;
        try {
			tempResponse = manager.getTemperatures(new TemperatureRequest(input));
			ObjectNode headerJson = JsonUtils.MAPPER.createObjectNode();
	        headerJson.put("x-custom-header", "my custom header value");
	        headerJson.put("Content-Type", "application/json");
	        
	        ObjectNode responseJson = JsonUtils.MAPPER.createObjectNode();
	        responseJson.put("statusCode", 500);
	        responseJson.set("headers", headerJson);
	        responseJson.set("body", JsonUtils.MAPPER.valueToTree(tempResponse));
	        
	        return tempResponse;
			
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return tempResponse;
		
        
        
        
    }


}
