package com.broadway.has.lambda.gethumidity.request;

import com.broadway.has.lambda.gethumidity.JsonUtils;
import com.broadway.has.lambda.gethumidity.exceptions.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;

import org.joda.time.DateTime;

public class HumidityRequest {


    private final Integer resolution;
    private final DateTime startTime;
    private final DateTime endTime;
    private final String nodeId;


    public HumidityRequest(Object params) throws BadRequestException{

    	
    	JsonNode object = JsonUtils.MAPPER.valueToTree(params);
    	
        try {
            Integer resolution = (Integer) object.get("resolution").asInt();
            String nodeId = object.get("nodeId").asText();
            String startTime = object.get("startTime").asText();
            String endTime = object.get("endTime").asText();


            this.resolution = resolution;

            this.nodeId = nodeId;

            if(startTime != null){
                this.startTime = DateTime.parse(startTime);
            }else{
                this.startTime = DateTime.now().minusHours(48);
            }

            if(endTime != null){
                this.endTime = DateTime.parse(endTime);
            }else{
                this.endTime = DateTime.now();
            }

        }catch(Exception e){

            //throw bad request exception
            throw new BadRequestException(params.toString(), e);

        }

    }

    public Integer getResolution() {
        return resolution;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public String getNodeId() {
        return nodeId;
    }
}
