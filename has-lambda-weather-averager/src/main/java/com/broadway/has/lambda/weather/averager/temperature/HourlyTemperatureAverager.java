package com.broadway.has.lambda.weather.averager.temperature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.broadway.has.lambda.weather.averager.LambdaFunctionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.broadway.has.lambda.weather.averager.JsonUtils;
import com.broadway.has.lambda.weather.averager.WeatherAverager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class HourlyTemperatureAverager implements WeatherAverager{
	
	private final String tableName = "temperatures-avg-hourly";
	private final DynamoDB dynamoDB;
	private final AmazonDynamoDB dbClient;
	private final Table avgTemps;


	private static final Logger logger = LogManager.getLogger(HourlyTemperatureAverager.class);

	public HourlyTemperatureAverager() {

		logger.info("Initializing temperature averager: {}, {}", tableName, Regions.US_EAST_1);

		this.dbClient = new AmazonDynamoDBClient();
		this.dbClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        this.dynamoDB = new DynamoDB(this.dbClient);
        this.avgTemps = dynamoDB.getTable(tableName);

	}
	
	private DateTime getTimeslot(DateTime exactTime) {

		DateTime timeslot = exactTime.toDateTimeISO();
		timeslot = timeslot.withMinuteOfHour(0);
		timeslot = timeslot.withSecondOfMinute(0);
		timeslot = timeslot.withMillisOfSecond(0);
		return timeslot;
	}
	

	@Override
	public void average(JsonNode coreRecord) throws JsonProcessingException, IOException{
		
		String nodeId = coreRecord.get("nodeId").asText();
		DateTime timestamp = DateTime.parse(coreRecord.get("createdDate").asText()).toDateTimeISO();

		float newTemp = coreRecord.get("temperature").get("temperature").asLong();
		

		//get the timeslot for this time
		DateTime timeslot = getTimeslot(timestamp);
		
		logger.debug("Getting timeslot " + timeslot.toDateTimeISO().toString());
		
		//get the average temp for this timeslot
		QuerySpec query = new QuerySpec()
                .withKeyConditionExpression("nodeId = :v_id and timeslot = :timeslot")
                .withValueMap(new ValueMap().withString(":v_id", nodeId)
                                            .withString(":timeslot", timeslot.toDateTimeISO().toString()))
                .withMaxResultSize(1);

		logger.debug("Issuing find query for timeslot: {}", query.toString());
		
		ItemCollection<QueryOutcome> items = avgTemps.query(query);
		
		Iterator<Item> timeslotIter = items.iterator();

		if(!timeslotIter.hasNext()){
			logger.info("No timeslots were found for {}.  Creating a new entry.", timeslot);

			//create it new.
			JsonNode newAverageEntry = createNewAverageEntry(timeslot, timestamp, coreRecord);

			String a = newAverageEntry.path("temperature").toString();
			//insert the new average
			Item item = new Item().withPrimaryKey("nodeId", nodeId, "timeslot", timeslot.toString());
			item.withJSON("temperature", newAverageEntry.path("temperature").toString());
			item.withJSON("recordTimestamps", newAverageEntry.path("recordTimestamps").toString());
			item.withJSON("lastUpdatedTimestamps", newAverageEntry.path("lastUpdatedTimestamps").toString());
			item.withString("lastUpdated", newAverageEntry.path("lastUpdated").toString());

			avgTemps.putItem(item);
		}
		else {

			logger.info("Adding point to existing timeslot for {}", timeslot);

			//if the timeslot already exists, add this to the running sum
			Item runningAverage = timeslotIter.next();
			
			ObjectNode runningAverageObj = (ObjectNode)JsonUtils.MAPPER.readTree(runningAverage.toJSON());
			JsonNode runningAverageJson = runningAverageObj.get("temperature");
			
			int totalPts = runningAverageJson.get("numberOfPoints").asInt();
			double avgTemp = Float.parseFloat(runningAverageJson.get("temperature").asText());
			
			//add the new temp in and recompute
			double newAvg = avgTemp + newTemp;
			newAvg = newAvg / 2.0;
			++totalPts;
			
			//update the item
			String lastUpdatedTimestampStr = DateTime.now().toDateTimeISO().toString();
			List<String> lastUpdatedTimestamp = new ArrayList<String>(){{add(DateTime.now().toDateTimeISO().toString());}};
			List<String> createdDate = new ArrayList<String>(){{add(coreRecord.get("createdDate").textValue());}};
			ArrayNode records = (ArrayNode)runningAverageObj.get("recordTimestamps");


			//update the item in dynamo
			String updateExpression =
					"SET temperature.temperature = :newAvg, " +
							"temperature.numberOfPoints = :totalPts, " +
							"lastUpdated = :lastUpdatedTimestampStr, " +
							"recordTimestamps = list_append(recordTimestamps, :createdDate), " +
							"lastUpdatedTimestamps = list_append(lastUpdatedTimestamps, :lastUpdatedTimestamp)";

			UpdateItemSpec update = new UpdateItemSpec()
					.withPrimaryKey("nodeId", nodeId, "timeslot", timeslot.toDateTimeISO().toString())
					.withUpdateExpression(updateExpression)
					.withValueMap(new ValueMap()
						.withNumber(":newAvg", newAvg)
						.withInt(":totalPts", totalPts)
						.withString(":lastUpdatedTimestampStr", lastUpdatedTimestampStr)
						.withList(":lastUpdatedTimestamp", lastUpdatedTimestamp)
						.withList(":createdDate", createdDate))
					.withReturnValues(ReturnValue.ALL_NEW);

			logger.info("Updating item {} from temp {} to new avg temp {}", nodeId, avgTemp, newAvg);

			UpdateItemOutcome result = avgTemps.updateItem(update);

			logger.debug("Result from update: " + result.getItem().toJSONPretty());
			
		}
		
		
	}
	
	
	private JsonNode createNewAverageEntry(DateTime timeslot, DateTime timestamp, JsonNode sqsEvent) {

		String lastUpdatedTimestamp = DateTime.now().toDateTimeISO().toString();

		ObjectNode newAverageEntry = JsonUtils.MAPPER.createObjectNode();
		newAverageEntry.put("nodeId", sqsEvent.get("nodeId"));
		newAverageEntry.put("timeslot", timeslot.toDateTimeISO().toString());
		newAverageEntry.put("temperature", sqsEvent.get("temperature"));
		((ObjectNode)newAverageEntry.get("temperature")).put("numberOfPoints", 1);
		newAverageEntry.put("lastUpdated", lastUpdatedTimestamp);
		newAverageEntry.put("recordTimestamps",  JsonUtils.MAPPER.createArrayNode().add(timestamp.toDateTimeISO().toString()));
		newAverageEntry.put("lastUpdatedTimestamps",  JsonUtils.MAPPER.createArrayNode().add(lastUpdatedTimestamp));
		
		return newAverageEntry;
	}

}
