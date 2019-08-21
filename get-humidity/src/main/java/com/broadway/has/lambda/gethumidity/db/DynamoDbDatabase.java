package com.broadway.has.lambda.gethumidity.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.broadway.has.lambda.gethumidity.JsonUtils;
import com.broadway.has.lambda.gethumidity.response.HumidityResponse;
import org.joda.time.DateTime;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.broadway.has.lambda.gethumidity.response.SingleHumidityEntry;
import com.broadway.has.lambda.gethumidity.response.HumidityEntry;
import com.fasterxml.jackson.databind.JsonNode;

public class DynamoDbDatabase implements DatabaseManager {


    private final DynamoDB dynamoDB;

    private final String humidityTable = "humidity-avg-hourly";

    private final Table humidity;

    private final Integer MAX_RETURN_SIZE = 100000;

    public DynamoDbDatabase(){
    	System.out.println("initializing db");
        AmazonDynamoDB client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_EAST_1));
        this.dynamoDB = new DynamoDB(client);
        System.out.println("init done");
        this.humidity = dynamoDB.getTable(humidityTable);
        System.out.println("got tables");


        System.out.println("Done iterating");
    }

    @Override
    public HumidityResponse getHumidity(String nodeId, DateTime startDate, DateTime endDate, Integer limit) throws IOException{

        int returnSize = Math.min(limit, MAX_RETURN_SIZE);

        QuerySpec query = new QuerySpec()
                .withKeyConditionExpression("nodeId = :v_id and timeslot between :startDate and :endDate")
                .withValueMap(new ValueMap().withString(":v_id", nodeId)
                                            .withString(":endDate", endDate.toDateTimeISO().toString())
                                            .withString(":startDate", startDate.toDateTimeISO().toString()))
                .withMaxResultSize(returnSize);

        ItemCollection<QueryOutcome> items = humidity.query(query);

        


        List<HumidityEntry> temps = new ArrayList<>();

        Iterator<Item> iter = items.iterator();
        while(iter.hasNext()){
            Item i = iter.next();

            JsonNode tempJson = JsonUtils.MAPPER.readTree(i.getJSON("humidity"));

            try {
	            SingleHumidityEntry temp = new SingleHumidityEntry(
	                    new DateTime(DateTime.parse(i.getString("lastUpdated"))),
	                    new DateTime(DateTime.parse(i.getString("timeslot"))),
	                    tempJson.get("humidity").asLong(),
	                    "percent"
	            );
	
	            temps.add(temp);
            }catch(Exception e) {
            	System.err.println("Error processing humidity from db: " + e);
            }

        }
        

        return new HumidityResponse(nodeId, 0, "humidity", temps);
    }
}
