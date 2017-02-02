package api.fms.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

import api.fms.constants.DNDBConstants;
import api.fms.dto.PayloadDto;
import api.fms.dto.TableInfoDto;
import api.fms.vo.PayloadVo;
// 2017-02-02
public class DNDBService {
	private static DNDBService DNDBSERVICE;

	public static DNDBService getInstance() {
		if (DNDBSERVICE == null) {
			DNDBSERVICE = new DNDBService();
		}
		return DNDBSERVICE;
	}

	// public void update(String tableName, PayloadVo payloadVo) throws
	// InterruptedException, NullPointerException {
	// DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
	// Table table = dynamoDB.getTable(tableName);
	// UpdateItemSpec updateItemSpec = new UpdateItemSpec()
	// .withPrimaryKey(DNDBConstants.PACKID, payloadVo.getPackId())
	// .withPrimaryKey(DNDBConstants.TIMESTAMP, payloadVo.getTimestamp())
	// .withPrimaryKey(DNDBConstants.VOLTAGE, payloadVo.getVoltage())
	// .withPrimaryKey(DNDBConstants.CURRENT, payloadVo.getCurrent())
	// .withPrimaryKey(DNDBConstants.SOC, payloadVo.getSoc())
	// .withPrimaryKey(DNDBConstants.SOH, payloadVo.getSoh())
	// .withPrimaryKey(DNDBConstants.TEMPERATURE, payloadVo.getTemperature())
	// .withPrimaryKey(DNDBConstants.ALERT, payloadVo.getAlert())
	// .withPrimaryKey(DNDBConstants.LATITUDE, payloadVo.getLatitude())
	// .withPrimaryKey(DNDBConstants.LONGITUDE, payloadVo.getLongitude())
	// .withPrimaryKey(DNDBConstants.SPEED, payloadVo.getSpeed())
	// .withReturnValues(ReturnValue.UPDATED_NEW);
	// // .withConditionExpression("size(" + DNDBConstants.PACKID + " >= :num")
	// // // 可設置條件
	// // .withValueMap(new ValueMap().withNumber(":num", 3))
	//
	// UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
	// System.out.println("UpdateItem succeeded:\n" +
	// outcome.getItem().toJSONPretty());
	// }
	
	
	/**
	 * 建立 Table
	 * @param tableName
	 * @return Status
	 * @throws InterruptedException
	 */
	public String createTable(String tableName) throws InterruptedException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.createTable(tableName,
				Arrays.asList(new KeySchemaElement(DNDBConstants.PACKID, KeyType.HASH)),
				Arrays.asList(new AttributeDefinition(DNDBConstants.PACKID, ScalarAttributeType.S)),
				new ProvisionedThroughput(10L, 10L));
		table.waitForActive();
		TableDescription tableDescription = dynamoDB.getTable(tableName).describe();
		System.out.println(tableDescription.getTableStatus());
		return tableDescription.getTableStatus();
	}

	/**
	 * 依照 tableName ＆ PayloadVo 建立 Item ，PayloadVoh0 參數不可為 null
	 * 
	 * @param tableName
	 * @param payloadVo
	 * @throws InterruptedException
	 * @throws NullPointerException
	 */
	public void insert(String tableName, PayloadVo payloadVo) throws InterruptedException, NullPointerException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);

		Item item = new Item().withPrimaryKey(DNDBConstants.PACKID, payloadVo.getPackId())
				.withNumber(DNDBConstants.TIMESTAMP, payloadVo.getTimestamp())
				.withNumber(DNDBConstants.VOLTAGE, payloadVo.getVoltage())
				.withNumber(DNDBConstants.CURRENT, payloadVo.getCurrent())
				.withNumber(DNDBConstants.SOC, payloadVo.getSoc()).withNumber(DNDBConstants.SOH, payloadVo.getSoh())
				.withNumber(DNDBConstants.TEMPERATURE, payloadVo.getTemperature())
				.withString(DNDBConstants.ALERT, payloadVo.getAlert())
				.withNumber(DNDBConstants.LATITUDE, payloadVo.getLatitude())
				.withNumber(DNDBConstants.LONGITUDE, payloadVo.getLongitude())
				.withNumber(DNDBConstants.SPEED, payloadVo.getSpeed());
		
		table.putItem(item);
//		PutItemOutcome outcome = table.putItem(item);
//		System.out.print(outcome.getPutItemResult());
//		System.out.print(outcome.getItem());
	}
	
	
	public List<PayloadDto> findAllItem(String tableName){
		
		return null;
	}

	/**
	 * 依照 PackId 查詢轉 PayloadDto
	 * 
	 * @param tableName
	 * @param packId
	 * @return
	 */
	public PayloadDto findByPackId(String tableName, String packId) {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(DNDBConstants.PACKID, packId);

		Item outcome = table.getItem(spec);

		PayloadDto dto = new PayloadDto();
		dto.setPackId(outcome.getJSON(DNDBConstants.PACKID));
		dto.setTimestamp(Long.parseLong(outcome.getJSON(DNDBConstants.TIMESTAMP)));
		dto.setVoltage(Double.parseDouble(outcome.getJSON(DNDBConstants.VOLTAGE)));
		dto.setCurrent(Double.parseDouble(outcome.getJSON(DNDBConstants.CURRENT)));
		dto.setSoc(Integer.parseInt(outcome.getJSON(DNDBConstants.SOC)));
		dto.setSoh(Integer.parseInt(outcome.getJSON(DNDBConstants.SOH)));
		dto.setTemperature(Integer.parseInt(outcome.getJSON(DNDBConstants.TEMPERATURE)));
		dto.setAlert(outcome.getJSON(DNDBConstants.ALERT));
		dto.setLatitude(Double.parseDouble(outcome.getJSON(DNDBConstants.LATITUDE)));
		dto.setLongitude(Double.parseDouble(outcome.getJSON(DNDBConstants.LONGITUDE)));
		dto.setSpeed(Integer.parseInt(outcome.getJSON(DNDBConstants.SPEED)));

		return dto;
	}

	/**
	 * 依照 tableName & packId 刪除 Item
	 * @param tableName
	 * @param packId
	 */
	public void deleteItemByPackId(String tableName, String packId) {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);

		DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
				.withPrimaryKey(DNDBConstants.PACKID, packId);
		DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
		// Check the response.
		System.out.println("Printing item that was deleted...");
		System.out.println(outcome.getItem().toJSONPretty());

	}

	/**
	 * 取得 Table 細部資訊
	 * 
	 * @param tableName
	 * @return
	 * @throws ResourceNotFoundException
	 */
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

	/**
	 * 取得目前 Tables 數量
	 * 
	 * @return
	 */
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

	/**
	 * 取得全部 Table 可作為判斷重複 Table
	 * 
	 * @return ArrayList<String>
	 */
	public List<String> listTables() {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		TableCollection<ListTablesResult> tables = dynamoDB.listTables();
		Iterator<Table> iterator = tables.iterator();

		System.out.println("Listing table names");

		List<String> tableList = new ArrayList<String>();
		while (iterator.hasNext()) {
			Table table = iterator.next();
			tableList.add(table.getTableName());
			System.out.println(table.getTableName());
		}
		return tableList;
	}

	/**
	 * 刪除 Table
	 * 
	 * @param tableName
	 * @throws InterruptedException
	 */
	public void deleteTable(String tableName) throws InterruptedException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		table.delete();
		table.waitForDelete();
	}
}
