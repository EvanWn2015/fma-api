package com.acer.fms.sevice;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DBconnect  {
	private static DBconnect DBCONNECT;
	private static String DBURL = "http://localhost:8000";

	public DBconnect() {
	}

	public static DBconnect getInstance() {
		if (DBCONNECT == null) {
			DBCONNECT = new DBconnect();
		}
		return DBCONNECT;
	}

	public DynamoDB getDynamoDB() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient().withEndpoint(DBURL);
		return new DynamoDB(client);
	}

}
