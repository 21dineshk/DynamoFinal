package com.ensemble;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

public class HandlerPOST implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent message, Context context) {
        System.out.println("Input message: " + message);

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDB = new DynamoDB(client);
        Gson gson = new Gson();

        String tableName = System.getenv("Employee");
        Table table = dynamoDB.getTable(tableName);
        String body = message.getBody();
        Employee inputData = gson.fromJson(body, Employee.class);

        Item item = new Item()
                .withPrimaryKey("id", inputData.getId())
                .withString("name", inputData.getName())
                .withString("location", inputData.getLocation());

        PutItemOutcome outcome = table.putItem(item);

        return new APIGatewayProxyResponseEvent().withStatusCode(204);
    }
}