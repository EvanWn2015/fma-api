package com.acer.fms.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.acer.fms.sevice.DBconnect;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndexDescription;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

public class Example {

	/**
	 * 以下是使用DynamoDB文檔API創建具有本地輔助索引的表的步驟。 創建實例DynamoDB類。
	 * 創建實例CreateTableRequest類，提供申請信息。
	 * 您必須提供表名稱，其主鍵和預配置的吞吐量值。對於本地輔助索引，必須提供索引名稱，索引排序鍵的名稱和數據類型，索引的鍵模式和屬性投影。
	 * 調用createTable通過提供request對象作為參數的方法。
	 */
	public void createTable(String tableName) {
		// DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(new
		// ProfileCredentialsProvider()));
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();

		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);

		// ProvisionedThroughput
		createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

		// AttributeDefinitions
		ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("Artist").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("SongTitle").withAttributeType("S"));
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("AlbumTitle").withAttributeType("S"));

		createTableRequest.setAttributeDefinitions(attributeDefinitions);

		// KeySchema
		ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
		tableKeySchema.add(new KeySchemaElement().withAttributeName("Artist").withKeyType(KeyType.HASH)); // Partition
																											// key
		tableKeySchema.add(new KeySchemaElement().withAttributeName("SongTitle").withKeyType(KeyType.RANGE)); // Sort
																												// key

		createTableRequest.setKeySchema(tableKeySchema);

		ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
		indexKeySchema.add(new KeySchemaElement().withAttributeName("Artist").withKeyType(KeyType.HASH)); // Partition
																											// key
		indexKeySchema.add(new KeySchemaElement().withAttributeName("AlbumTitle").withKeyType(KeyType.RANGE)); // Sort
																												// key
		Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);
		ArrayList<String> nonKeyAttributes = new ArrayList<String>();
		nonKeyAttributes.add("Genre");
		nonKeyAttributes.add("Year");
		projection.setNonKeyAttributes(nonKeyAttributes);

		LocalSecondaryIndex localSecondaryIndex = new LocalSecondaryIndex().withIndexName("AlbumTitleIndex")
				.withKeySchema(indexKeySchema).withProjection(projection);

		ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
		localSecondaryIndexes.add(localSecondaryIndex);
		createTableRequest.setLocalSecondaryIndexes(localSecondaryIndexes);

		Table table = dynamoDB.createTable(createTableRequest);
		System.out.println(table.getDescription());
	}

	/**
	 * 使用本地輔助索引描述表 為了獲得在表上關於本地二級索引信息，使用describeTable
	 * 方法。對於每個索引，您可以訪問其名稱，鍵架構和投影屬性。 以下是使用AWS SDK for Java Document
	 * API訪問表的輔助索引信息的步驟 創建實例DynamoDB類。 創建實例Table類。您必須提供表名稱。
	 * 調用describeTable該方法上的Table 對象。
	 */
	public void describeTable() {
		DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(new ProfileCredentialsProvider()));

		String tableName = "Music";
		Table table = dynamoDB.getTable(tableName);
		TableDescription tableDescription = table.describe();
		List<LocalSecondaryIndexDescription> localSecondaryIndexes = tableDescription.getLocalSecondaryIndexes();
		// This code snippet will work for multiple indexes, even though
		// there is only one index in this example.
		Iterator<LocalSecondaryIndexDescription> lsiIter = localSecondaryIndexes.iterator();
		while (lsiIter.hasNext()) {
			LocalSecondaryIndexDescription lsiDescription = lsiIter.next();
			System.out.println("Info for index " + lsiDescription.getIndexName() + ":");
			Iterator<KeySchemaElement> kseIter = lsiDescription.getKeySchema().iterator();
			while (kseIter.hasNext()) {
				KeySchemaElement kse = kseIter.next();
				System.out.printf("\t%s: %s\n", kse.getAttributeName(), kse.getKeyType());
			}
			Projection projection = lsiDescription.getProjection();
			System.out.println("\tThe projection type is: " + projection.getProjectionType());
			if (projection.getProjectionType().toString().equals("INCLUDE")) {
				System.out.println("\t\tThe non-key projected attributes are: " + projection.getNonKeyAttributes());
			}
		}
	}

	/**
	 * 以下是使用AWS SDK for Java Document API查詢本地輔助索引的步驟。 創建實例DynamoDB類。
	 * 創建實例Table類。您必須提供表名稱。 創建實例Index類。您必須提供索引名稱。 調用query該方法的Index類。
	 */
	public void query() {
		DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(new ProfileCredentialsProvider()));
		String tableName = "Music";
		Table table = dynamoDB.getTable(tableName);
		Index index = table.getIndex("AlbumTitleIndex");

		QuerySpec spec = new QuerySpec().withKeyConditionExpression("Artist = :v_artist and AlbumTitle = :v_title")
				.withValueMap(
						new ValueMap().withString(":v_artist", "Acme Band").withString(":v_title", "Songs About Life"));
		ItemCollection<QueryOutcome> items = index.query(spec);
		Iterator<Item> itemsIter = items.iterator();
		while (itemsIter.hasNext()) {
			Item item = itemsIter.next();
			System.out.println(item.toJSONPretty());
		}
	}
}
