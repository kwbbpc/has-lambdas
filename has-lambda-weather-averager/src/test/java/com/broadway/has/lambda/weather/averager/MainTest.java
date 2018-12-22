package com.broadway.has.lambda.weather.averager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.tz.FixedDateTimeZone;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Record;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MainTest {
	

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectMapper snsEventMapper = new ObjectMapper();
    public static final ObjectMapper dynamodbEventMapper = new ObjectMapper();

    static {
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.setPropertyNamingStrategy(new UpperCaseRecordsPropertyNamingStrategy());
        mapper.registerModule(new TestJacksonMapperModule());

        snsEventMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        //snsEventMapper.setPropertyNamingStrategy(PropertyNamingStrategy.PASCAL_CASE_TO_CAMEL_CASE);
        snsEventMapper.registerModule(new TestJacksonMapperModule());

        dynamodbEventMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        dynamodbEventMapper.setPropertyNamingStrategy(new UpperCaseRecordsPropertyNamingStrategy());
        dynamodbEventMapper.registerModule(new TestJacksonMapperModule());
        dynamodbEventMapper.addMixIn(Record.class, DynamodbEventMixin.RecordMixin.class);
        dynamodbEventMapper.addMixIn(StreamRecord.class, DynamodbEventMixin.StreamRecordMixin.class);
        dynamodbEventMapper.addMixIn(AttributeValue.class, DynamodbEventMixin.AttributeValueMixIn.class);
    }

    private static final DateTimeFormatter dateTimeFormatter =
            ISODateTimeFormat.dateTime()
                        .withZone(new FixedDateTimeZone("GMT", "GMT", 0, 0));



    private static class TestJacksonMapperModule extends SimpleModule {

        private static final long serialVersionUID = 1L;

        public TestJacksonMapperModule() {
            super("TestJacksonMapperModule");

            super.addSerializer(DateTime.class, new DateTimeSerializer());
            super.addDeserializer(DateTime.class, new DateTimeDeserializer());
        }
    }

    private static class DateTimeSerializer extends JsonSerializer<DateTime> {

        @Override
        public void serialize(
                DateTime value,
                JsonGenerator gen,
                SerializerProvider provider) throws IOException {

            gen.writeString(dateTimeFormatter.print(value));
        }
    }

    private static class DateTimeDeserializer
            extends JsonDeserializer<DateTime> {

        @Override
        public DateTime deserialize(
                JsonParser parser,
                DeserializationContext context) throws IOException {

            return dateTimeFormatter.parseDateTime(parser.getText());
        }
    }

    private static class UpperCaseRecordsPropertyNamingStrategy
            extends PropertyNamingStrategy.PropertyNamingStrategyBase {

        private static final long serialVersionUID = 1L;

        @Override
        public String translate(String propertyName) {
            if (propertyName.equals("records")) {
                return "Records";
            }
            return propertyName;
        }
    }

    private static interface DynamodbEventMixin {
        public static final String L = "L";
        public static final String M = "M";
        public static final String BS = "BS";
        public static final String NS = "NS";
        public static final String SS = "SS";
        public static final String BOOL = "BOOL";
        public static final String NULL = "NULL";
        public static final String B = "B";
        public static final String N = "N";
        public static final String S = "S";
        public static final String OLD_IMAGE = "OldImage";
        public static final String NEW_IMAGE = "NewImage";
        public static final String STREAM_VIEW_TYPE = "StreamViewType";
        public static final String SEQUENCE_NUMBER = "SequenceNumber";
        public static final String SIZE_BYTES = "SizeBytes";
        public static final String KEYS = "Keys";
        public static final String AWS_REGION = "awsRegion";
        public static final String DYNAMODB = "dynamodb";
        public static final String EVENT_ID = "eventID";
        public static final String EVENT_NAME = "eventName";
        public static final String EVENT_SOURCE = "eventSource";
        public static final String EVENT_VERSION = "eventVersion";
        public static final String EVENT_SOURCE_ARN = "eventSourceARN";
        public static final String APPROXIMATE_CREATION_DATE_TIME = "ApproximateCreationDateTime";

        @JsonProperty(value = "Records")
        public List<?> getRecords();

        static interface RecordMixin {
            @JsonProperty(AWS_REGION) public String getAwsRegion();
            @JsonProperty(AWS_REGION) public void setAwsRegion(String awsRegion);
            @JsonProperty(DYNAMODB) public Object getDynamodb();
            @JsonProperty(DYNAMODB) public void setDynamodb(Object dynamodb);
            @JsonProperty(EVENT_ID) public String getEventID();
            @JsonProperty(EVENT_ID) public void setEventID(String eventID);
            @JsonProperty(EVENT_NAME) public String getEventName();
            @JsonProperty(EVENT_NAME) public void setEventName(String eventName);
            @JsonProperty(EVENT_SOURCE) public String getEventSource();
            @JsonProperty(EVENT_SOURCE) public void setEventSource(String eventSource);
            @JsonProperty(EVENT_VERSION) public String getEventVersion();
            @JsonProperty(EVENT_VERSION) public void setEventVersion(String eventVersion);
            @JsonProperty(EVENT_SOURCE_ARN) public String getEventSourceArn();
            @JsonProperty(EVENT_SOURCE_ARN) public void setEventSourceArn(String eventSourceArn);
        }

        static interface StreamRecordMixin {

            @JsonProperty(KEYS) public Map<String, Object> getKeys();
            @JsonProperty(KEYS) public void setKeys(Map<String, Object> keys);
            @JsonProperty(SIZE_BYTES) public Long getSizeBytes();
            @JsonProperty(SIZE_BYTES) public void setSizeBytes(Long sizeBytes);
            @JsonProperty(SEQUENCE_NUMBER) public String getSequenceNumber();
            @JsonProperty(SEQUENCE_NUMBER) public void setSequenceNumber(String sequenceNumber);
            @JsonProperty(STREAM_VIEW_TYPE) public String getStreamViewType();
            @JsonProperty(STREAM_VIEW_TYPE) public void setStreamViewType(String streamViewType);
            @JsonProperty(NEW_IMAGE) public Map<String, Object> getNewImage();
            @JsonProperty(NEW_IMAGE) public void setNewImage(Map<String, Object> newImage);
            @JsonProperty(OLD_IMAGE) public Map<String, Object> getOldImage();
            @JsonProperty(OLD_IMAGE) public void setOldImage(Map<String, Object> oldImage);
            @JsonProperty(APPROXIMATE_CREATION_DATE_TIME) public Date getApproximateCreationDateTime();
            @JsonProperty(APPROXIMATE_CREATION_DATE_TIME) public void setApproximateCreationDateTime(Date approximateCreationDateTime);
        }

        static interface AttributeValueMixIn {
            @JsonProperty(S) public String getS();
            @JsonProperty(S) public void setS(String s);
            @JsonProperty(N) public String getN();
            @JsonProperty(N) public void setN(String n);
            @JsonProperty(B) public ByteBuffer getB();
            @JsonProperty(B) public void setB(ByteBuffer b);
            @JsonProperty(NULL) public Boolean isNULL();
            @JsonProperty(NULL) public void setNULL(Boolean nU);
            @JsonProperty(BOOL) public Boolean getBOOL();
            @JsonProperty(BOOL) public void setBOOL(Boolean bO);
            @JsonProperty(SS) public List<String> getSS();
            @JsonProperty(SS) public void setSS(List<String> sS);
            @JsonProperty(NS) public List<String> getNS();
            @JsonProperty(NS) public void setNS(List<String> nS);
            @JsonProperty(BS) public List<String> getBS();
            @JsonProperty(BS) public void setBS(List<String> bS);
            @JsonProperty(M) public Map<String, Object> getM();
            @JsonProperty(M) public void setM(Map<String, Object> val);
            @JsonProperty(L) public List<Object> getL();
            @JsonProperty(L) public void setL(List<Object> val);
        }
    }

    public static void main(String args[]) throws Exception{


        DynamodbEvent event = dynamodbEventMapper.readValue(getDynamoDBEvent(), DynamodbEvent.class);

        LambdaFunctionHandler h = new LambdaFunctionHandler();

        h.handleRequest(event, null);

    }

    private static String getDynamoDBEvent(){
        return
        "{\n" +
                "  \"Records\": [\n" +
                "    {\n" +
                "      \"eventID\": \"c4ca4238a0b923820dcc509a6f75849b\",\n" +
                "      \"eventName\": \"INSERT\",\n" +
                "      \"eventVersion\": \"1.1\",\n" +
                "      \"eventSource\": \"aws:dynamodb\",\n" +
                "      \"awsRegion\": \"us-east-1\",\n" +
                "      \"dynamodb\": {\n" +
                "        \"Keys\": {\"createdDate\":{\"S\": \"2018-12-04T01:57:50.258Z\"}, \"nodeId\":{\"S\": \"0013A200406B8D09\"}\n" +
                "        },\n" +
                "        \"NewImage\": {\n" +


                        "\"createdDate\": \"2018-12-04T01:37:36.864Z\", " +
                        "\"indexDate\": \"2018-12-03T20:37:36.945-05:00\", " +
                        "\"temperature\": {\"M\": { " +
                            "\"temperature\": {\"N\": 73.399994}" +
                        "}}, " +
                        "\"nodeInfo\": { \"M\": { " +
                        "\"hardwareVersionId\": \"0\",  " +
                        "\"xbeeProtocol\": \"ZigBee\", \"address16bit\": \"3BD7\", \"address64bit\": \"0013A200406B8D09\" " +
                        "}}, " +
                        "\"nodeId\":\"0013A200406B8D09\"" +



                "        },\n" +
                "        \"ApproximateCreationDateTime\": 1428537600,\n" +
                "        \"SequenceNumber\": \"4421584500000000017450439091\",\n" +
                "        \"SizeBytes\": 26,\n" +
                "        \"StreamViewType\": \"NEW_AND_OLD_IMAGES\"\n" +
                "      },\n" +
                "      \"eventSourceARN\": \"arn:aws:dynamodb:us-east-1:123456789012:table/ExampleTableWithStream/stream/2015-06-27T00:48:05.899\"\n" +
                "    }]}";
        /*

        return "{\"Records\":[{\"dynamodb\":{\"ApproximateCreationDateTime\": \"2018-12-04T01:37:36.864Z\",\"Keys\": {\"createdDate\": \"2018-12-04T01:37:36.864Z\", " +
                "\"nodeId\": \"0013A200406B8D09\"},\"NewImage\": {\"createdDate\": \"2018-12-04T01:37:36.864Z\", " +
                "\"indexDate\": \"2018-12-03T20:37:36.945-05:00\", \"temperature\": { " +
                "\"temperature\": \"73.399994\"}}, \"nodeInfo\": {\"hardwareVersionId\": \"0\",  " +
                "\"xbeeProtocol\": \"ZigBee\", \"address16bit\": \"3BD7\", \"address64bit\": \"0013A200406B8D09\" " +
                "}, " +
                "\"nodeId\":0013A200406B8D09\"},\"SequenceNumber\": \"429273900000000004713091230\",\"SizeBytes\": \"343\"," +
                "\"StreamViewType\": \"NEW_IMAGE\"}]}";*/
    }
	
}
