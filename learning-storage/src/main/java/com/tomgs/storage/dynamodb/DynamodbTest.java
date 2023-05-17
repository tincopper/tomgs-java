package com.tomgs.storage.dynamodb;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import org.junit.Before;
import org.junit.Test;

/**
 * DynamodbTest
 *
 * @author tomgs
 * @since 1.0
 */
public class DynamodbTest {

    private DynamoDB dynamoDB;

    @Before
    public void init() {
        // This client will default to US West (Oregon)
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.CN_NORTHWEST_1)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();
        dynamoDB = new DynamoDB(client);
    }

    @Test
    public void testCreateItem() {
        final Table table = dynamoDB.getTable("FeedbackRecords");
        try {
            Item item = new Item().withPrimaryKey("SearchInputs", "{\"datetime\": 1676514195.7583299, \"Inputs\": \"单瓣钻石SI2圆形\"}")
                    .withString("_id", "123")
                    .withString("Feedback", "1");
            table.putItem(item);
        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void testGetItems() {
        final Table table = dynamoDB.getTable("FeedbackRecords");
        System.out.println(table.getTableName());

        final Item item1 = table.getItem("SearchInputs", "{\"datetime\": 1676514195.7583299, \"Inputs\": \"单瓣钻石SI2圆形\"}",
                "_id", "46");
        System.out.println(item1);

        final ItemCollection<QueryOutcome> items = table
                .query("SearchInputs", "{\"datetime\": 1676514195.7583299, \"Inputs\": \"单瓣钻石SI2圆形\"}");
        for (Item item : items) {
            System.out.println(item);
        }

//        QuerySpec querySpec = new QuerySpec()
//                .withConditionalOperator()
//        final ItemCollection<QueryOutcome> query = table.query(querySpec);
//        for (Item item : query) {
//            System.out.println(item.toJSONPretty());
//        }
    }

    @Test
    public void testScanItems() {

    }

}
