package com.acer.fms.sevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.acer.fms.constants.DNDBConstants;
import com.acer.fms.dto.PayloadDto;
import com.acer.fms.dto.TableInfoDto;
import com.acer.fms.vo.PayloadVo;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

public class DNDBService {
	private static DNDBService DNDBSERVICE;

	public static DNDBService getInstance() {
		if (DNDBSERVICE == null) {
			DNDBSERVICE = new DNDBService();
		}
		return DNDBSERVICE;
	}

	public String createTable2(String tableName) throws InterruptedException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		// ScalarAttributeType.N 代表數字
		// ScalarAttributeType.S 代表字符串
		Table table = dynamoDB.createTable(tableName,
				Arrays.asList(new KeySchemaElement(DNDBConstants.PACKID, KeyType.HASH),
						new KeySchemaElement(DNDBConstants.TIMESTAMP, KeyType.RANGE)),
				Arrays.asList(new AttributeDefinition(DNDBConstants.PACKID, ScalarAttributeType.S),
						new AttributeDefinition(DNDBConstants.TIMESTAMP, ScalarAttributeType.N),
						new AttributeDefinition(DNDBConstants.VOLTAGE, ScalarAttributeType.N),
						new AttributeDefinition(DNDBConstants.CURRENT, ScalarAttributeType.N),
						new AttributeDefinition(DNDBConstants.SOC, ScalarAttributeType.N),
						new AttributeDefinition(DNDBConstants.SOH, ScalarAttributeType.N),
						new AttributeDefinition(DNDBConstants.TEMPERATURE, ScalarAttributeType.N),
						new AttributeDefinition(DNDBConstants.ALERT, ScalarAttributeType.S),
						new AttributeDefinition(DNDBConstants.LATITUDE, ScalarAttributeType.N),
						new AttributeDefinition(DNDBConstants.LONGITUDE, ScalarAttributeType.N),
						new AttributeDefinition(DNDBConstants.SPEED, ScalarAttributeType.N)),
				new ProvisionedThroughput(10L, 10L));
		table.waitForActive();
		System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

		TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
		return tableDescription.getTableStatus();
		// 可以獲取有關表的詳細信息
		// TableDescription tableDescription =
		// dynamoDB.getTable(TABLENAME).describe();
		// System.out.printf("%s: %s \t ReadCapacityUnits: %d \t
		// WriteCapacityUnits: %d",
		// tableDescription.getTableStatus(),
		// tableDescription.getTableName(),
		// tableDescription.getProvisionedThroughput().getReadCapacityUnits(),
		// tableDescription.getProvisionedThroughput().getWriteCapacityUnits());
	}

	public String createTable(String tableName) throws InterruptedException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.createTable(tableName,
				Arrays.asList(
						new KeySchemaElement(DNDBConstants.PACKID, KeyType.HASH),
						new KeySchemaElement(DNDBConstants.TIMESTAMP, KeyType.RANGE)),
				Arrays.asList(
						new AttributeDefinition(DNDBConstants.PACKID, ScalarAttributeType.N),
						new AttributeDefinition(DNDBConstants.TIMESTAMP, ScalarAttributeType.N)),
				new ProvisionedThroughput(10L, 10L));
		table.waitForActive();
		System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

		TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
		return tableDescription.getTableStatus();
	}

	public static Integer getTablesCount() {

		TableCollection<ListTablesResult> tables = DBconnect.getInstance().getDynamoDB().listTables();
		Iterator<Table> iterator = tables.iterator();

		if (iterator instanceof Collection) {
			return ((Collection<?>) iterator).size();
		} else {
			int count = 0;
			while (iterator.hasNext()) {
				Table table = iterator.next();
				System.out.println(table.getTableName());
				count++;
			}
			return count;
		}
	}

	public TableInfoDto getTableInfo(String tableName) throws ResourceNotFoundException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		TableDescription td = dynamoDB.getTable(tableName).describe();

		TableInfoDto dto = new TableInfoDto();
		dto.setTableName(td.getTableName());
		dto.setTableStatus(td.getTableStatus());
		dto.setReadCapacityUnits(td.getProvisionedThroughput().getReadCapacityUnits());
		dto.setWiteCapacityUnits(td.getProvisionedThroughput().getWriteCapacityUnits());

		return dto;
	}

	// 成功
	public void insert(String tableName, PayloadVo payloadVo) throws InterruptedException, NullPointerException {
		Map<String, Object> infoMap = new HashMap<String, Object>();
//		infoMap.put(DNDBConstants.TIMESTAMP, payloadVo.getTimestamp());
		infoMap.put(DNDBConstants.VOLTAGE, payloadVo.getVoltage());
		infoMap.put(DNDBConstants.CURRENT, payloadVo.getCurrent());
		infoMap.put(DNDBConstants.SOC, payloadVo.getSoc());
		infoMap.put(DNDBConstants.SOH, payloadVo.getSoh());
		infoMap.put(DNDBConstants.TEMPERATURE, payloadVo.getTemperature());
		infoMap.put(DNDBConstants.ALERT, payloadVo.getAlert());
		infoMap.put(DNDBConstants.LATITUDE, payloadVo.getLatitude());
		infoMap.put(DNDBConstants.LONGITUDE, payloadVo.getLongitude());
		infoMap.put(DNDBConstants.SPEED, payloadVo.getSpeed());

		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		PutItemOutcome outcome = table.putItem(new Item()
				.withPrimaryKey(DNDBConstants.PACKID, payloadVo.getPackId())
				.withPrimaryKey(DNDBConstants.TIMESTAMP, payloadVo.getTimestamp())
//				.with(DNDBConstants.VOLTAGE, payloadVo.getVoltage())
//				.with(DNDBConstants.CURRENT, payloadVo.getCurrent())
//				.with(DNDBConstants.SOC, payloadVo.getSoc())
//				.with(DNDBConstants.SOH, payloadVo.getSoh())
//				.with(DNDBConstants.TEMPERATURE, payloadVo.getTemperature())
//				.with(DNDBConstants.ALERT, payloadVo.getAlert())
//				.with(DNDBConstants.LATITUDE, payloadVo.getLatitude())
//				.with(DNDBConstants.LONGITUDE, payloadVo.getLongitude())
//				.with(DNDBConstants.SPEED, payloadVo.getSpeed()));
//			
				.withMap(DNDBConstants.INFO, infoMap));
		System.out.print(outcome);

	}

	public PayloadDto find(String tableName, PayloadVo payloadVo) throws InterruptedException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);

		GetItemSpec spec = new GetItemSpec().withPrimaryKey(DNDBConstants.PACKID, payloadVo.getPackId())
				.withPrimaryKey(DNDBConstants.TIMESTAMP, payloadVo.getTimestamp())
				.withPrimaryKey(DNDBConstants.VOLTAGE, payloadVo.getVoltage())
				.withPrimaryKey(DNDBConstants.CURRENT, payloadVo.getCurrent())
				.withPrimaryKey(DNDBConstants.SOC, payloadVo.getSoc())
				.withPrimaryKey(DNDBConstants.SOH, payloadVo.getSoh())
				.withPrimaryKey(DNDBConstants.TEMPERATURE, payloadVo.getTemperature())
				.withPrimaryKey(DNDBConstants.ALERT, payloadVo.getAlert())
				.withPrimaryKey(DNDBConstants.LATITUDE, payloadVo.getLatitude())
				.withPrimaryKey(DNDBConstants.LONGITUDE, payloadVo.getLongitude())
				.withPrimaryKey(DNDBConstants.SPEED, payloadVo.getSpeed());

		Item outcome = table.getItem(spec);
		PayloadDto dto = new PayloadDto();
		dto.setPackId((Long) outcome.get(DNDBConstants.PACKID));
		dto.setTimestamp((Long) outcome.get(DNDBConstants.TIMESTAMP));
		dto.setVoltage((Double) outcome.get(DNDBConstants.VOLTAGE));
		dto.setCurrent((Double) outcome.get(DNDBConstants.CURRENT));
		dto.setSoc((Integer) outcome.get(DNDBConstants.SOC));
		dto.setSoh((Integer) outcome.get(DNDBConstants.SOH));
		dto.setTemperature((Integer) outcome.get(DNDBConstants.TEMPERATURE));
		dto.setAlert((String) outcome.get(DNDBConstants.ALERT));
		dto.setLatitude((Double) outcome.get(DNDBConstants.LATITUDE));
		dto.setLongitude((Double) outcome.get(DNDBConstants.LONGITUDE));
		dto.setSpeed((Integer) outcome.get(DNDBConstants.SPEED));

		return dto;
	}

	public ArrayList<PayloadDto> findByPackId(String tableName, Long packId) {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(DNDBConstants.PACKID, packId);
		Item outcome = table.getItem(spec);

		System.out.println("GetItem succeeded: " + outcome);

		ArrayList<PayloadDto> dtoList = new ArrayList<PayloadDto>();
		// while (iterator.hasNext()) {
		// item = iterator.next();
		// PayloadDto dto = new PayloadDto();
		// dto.setPackId(item.getLong(DNDBConstants.PACKID));
		// dto.setTimestamp(item.getLong(DNDBConstants.TIMESTAMP));
		// dto.setVoltage(item.getDouble(DNDBConstants.VOLTAGE));
		// dto.setCurrent(item.getDouble(DNDBConstants.CURRENT));
		// dto.setSoc(item.getInt(DNDBConstants.SOC));
		// dto.setSoh(item.getInt(DNDBConstants.SOH));
		// dto.setTemperature(item.getInt(DNDBConstants.TEMPERATURE));
		// dto.setAlert(item.getString(DNDBConstants.ALERT));
		// dto.setLatitude(item.getDouble(DNDBConstants.LATITUDE));
		// dto.setLongitude(item.getDouble(DNDBConstants.LONGITUDE));
		// dto.setSpeed(item.getInt(DNDBConstants.SPEED));
		// dtoList.add(dto);
		// }
		return dtoList;
	}

	public void update(String tableName, PayloadVo payloadVo) throws InterruptedException, NullPointerException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(DNDBConstants.PACKID, payloadVo.getPackId())
				.withPrimaryKey(DNDBConstants.TIMESTAMP, payloadVo.getTimestamp())
				.withPrimaryKey(DNDBConstants.VOLTAGE, payloadVo.getVoltage())
				.withPrimaryKey(DNDBConstants.CURRENT, payloadVo.getCurrent())
				.withPrimaryKey(DNDBConstants.SOC, payloadVo.getSoc())
				.withPrimaryKey(DNDBConstants.SOH, payloadVo.getSoh())
				.withPrimaryKey(DNDBConstants.TEMPERATURE, payloadVo.getTemperature())
				.withPrimaryKey(DNDBConstants.ALERT, payloadVo.getAlert())
				.withPrimaryKey(DNDBConstants.LATITUDE, payloadVo.getLatitude())
				.withPrimaryKey(DNDBConstants.LONGITUDE, payloadVo.getLongitude())
				.withPrimaryKey(DNDBConstants.SPEED, payloadVo.getSpeed()).withReturnValues(ReturnValue.UPDATED_NEW);
		// .withConditionExpression("size(" + DNDBConstants.PACKID + " >= :num")
		// // 可設置條件
		// .withValueMap(new ValueMap().withNumber(":num", 3))

		UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
		System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
	}

	public void deleteTable(String tableName) throws InterruptedException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		table.delete();
		table.waitForDelete();
	}
}
