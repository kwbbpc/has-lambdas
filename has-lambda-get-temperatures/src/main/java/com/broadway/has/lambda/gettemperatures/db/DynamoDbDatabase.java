package com.broadway.has.lambda.gettemperatures.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.broadway.has.lambda.gettemperatures.JsonUtils;
import com.broadway.has.lambda.gettemperatures.response.SingleTemperatureEntry;
import com.broadway.has.lambda.gettemperatures.response.TemperatureEntry;
import com.broadway.has.lambda.gettemperatures.response.TemperatureResponse;
import com.fasterxml.jackson.databind.JsonNode;

public class DynamoDbDatabase implements DatabaseManager {


    private final DynamoDB dynamoDB;

    private final String temperatureTable = "temperatures-avg-hourly";

    private final Table temperatures;

    private final Integer MAX_RETURN_SIZE = 100000;

    public DynamoDbDatabase(){
    	System.out.println("initializing db");
        AmazonDynamoDB client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.US_EAST_1));
        this.dynamoDB = new DynamoDB(client);
        System.out.println("init done");
        this.temperatures = dynamoDB.getTable(temperatureTable);
        System.out.println("got tables");

        TableCollection<ListTablesResult> r = dynamoDB.listTables();
        Iterator<Table> iter = r.iterator();
        while(iter.hasNext()){
            System.out.println(iter.next().getTableName());
        }

        System.out.println("Done iterating");
    }

    @Override
    public TemperatureResponse getTemperatures(String nodeId, DateTime startDate, DateTime endDate, Integer limit) throws IOException{

        int returnSize = Math.min(limit, MAX_RETURN_SIZE);

        QuerySpec query = new QuerySpec()
                .withKeyConditionExpression("nodeId = :v_id and timeslot between :startDate and :endDate")
                .withValueMap(new ValueMap().withString(":v_id", nodeId)
                                            .withString(":endDate", endDate.toDateTimeISO().toString())
                                            .withString(":startDate", startDate.toDateTimeISO().toString()))
                .withMaxResultSize(returnSize);

        ItemCollection<QueryOutcome> items = temperatures.query(query);

        


        List<TemperatureEntry> temps = new ArrayList<>();

        Iterator<Item> iter = items.iterator();
        while(iter.hasNext()){
            Item i = iter.next();

            JsonNode tempJson = JsonUtils.MAPPER.readTree(i.getJSON("temperature"));

            try {
	            SingleTemperatureEntry temp = new SingleTemperatureEntry(
	                    new DateTime(DateTime.parse(i.getString("lastUpdated"))),
	                    new DateTime(DateTime.parse(i.getString("timeslot"))),
	                    tempJson.get("temperature").asLong(),
	                    "F"
	            );
	
	            temps.add(temp);
            }catch(Exception e) {
            	System.err.println("Error processing temp from db: " + e);
            }

        }
        

        return new TemperatureResponse(nodeId, 0, "temperature", temps);
    }
}
