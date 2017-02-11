package api.fms.dao;

import java.util.Iterator;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import api.fms.constants.DNDBConstants;
import api.fms.server.DBconnect;
import api.fms.vo.PayloadVo;

public class PayloadDao {
	private static PayloadDao PAYLOAD_DAO;

	public static PayloadDao getInstance() {
		if (PAYLOAD_DAO == null) {
			PAYLOAD_DAO = new PayloadDao();
		}
		return PAYLOAD_DAO;
	}

	public void putItemBytableNameAndPayloaVo(String tableName, PayloadVo payloadVo)
			throws InterruptedException, NullPointerException {

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
	}
	
	/**
	 * 掃描表所有符合條件的 Item
	 * @param tableName 
	 * @param filter ex:"#time_r < :start_tr"
	 * @param valueMap ex: .withNumber(":start_tr", startTime) 
	 * @return
	 */
	public Iterator<Item> scanItemIteratorByValueMapAndFilter(String tableName, String filter, ValueMap valueMap) {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		
		ScanSpec spec = new ScanSpec()
				.withProjectionExpression(getProjectionExpression())
				.withFilterExpression(filter)
				.withNameMap(getNameMap())
				.withValueMap(valueMap);

		ItemCollection<ScanOutcome> items = null;
		Iterator<Item> iterator = null;
		try {
			items = table.scan(spec);
			iterator = items.iterator();
		} catch (Exception e) {
			System.err.println("Unable to scan the table:");
			System.err.println(e.getMessage());
		}
		return iterator;
	}
	
	/**
	 * 查询表所有符合條件的 Item
	 * @param tableName
	 * @param condition
	 * @param valueMap
	 * @return
	 */
	public Iterator<Item> queryItemIteratorByValueMapAndCondition(String tableName, String condition, ValueMap valueMap) {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		
		QuerySpec querySpec = new QuerySpec()
				.withProjectionExpression(getProjectionExpression())
				.withKeyConditionExpression(condition)
				.withNameMap(getNameMap())
				.withValueMap(valueMap)
				.withConsistentRead(true);

		ItemCollection<QueryOutcome> items = null;
		Iterator<Item> iterator = null;
		try {
			items = table.query(querySpec);
			iterator = items.iterator();
		} catch (Exception e) {
			System.err.println("Unable to query the table:");
			System.err.println(e.getMessage());
		}
		return iterator;
	}
	
	public Item findItemByPackId(String tableName, Integer packId) throws NullPointerException {

		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(DNDBConstants.PACKID, packId, DNDBConstants.TIMESTAMP, 1486823679603L);

		Item outcome = table.getItem(spec);
		System.out.println(outcome);
		return outcome;
	}

	public void deleteItemByPackId(String tableName, Integer packId) {

		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);

		DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey(DNDBConstants.PACKID, packId);
		DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
		// Check the response.
		System.out.println("Printing item that was deleted...");
		System.out.println(outcome.getItem().toJSONPretty());
	}
	
	private NameMap getNameMap(){
		NameMap nameMap = new NameMap()
				.with(DNDBConstants.PACKID_R, DNDBConstants.PACKID)
				.with(DNDBConstants.TIMESTAMP_R, DNDBConstants.TIMESTAMP)
				.with(DNDBConstants.VOLTAGE_R, DNDBConstants.VOLTAGE)
				.with(DNDBConstants.CURRENT_R, DNDBConstants.CURRENT)
				.with(DNDBConstants.SOC_R, DNDBConstants.SOC)
				.with(DNDBConstants.SOH_R, DNDBConstants.SOH)
				.with(DNDBConstants.TEMPERATURE_R, DNDBConstants.TEMPERATURE)
				.with(DNDBConstants.ALERT_R, DNDBConstants.ALERT)
				.with(DNDBConstants.LATITUDE_R, DNDBConstants.LATITUDE)
				.with(DNDBConstants.LONGITUDE_R, DNDBConstants.LONGITUDE)
				.with(DNDBConstants.SPEED_R, DNDBConstants.SPEED);
		return nameMap;
	}
	
	private String getProjectionExpression(){
		String projection = "#time_r, #id_r, #voltage_r, #current_r, #soc_r, #soh_r, #temp_r, #alert_r, #lat_r, #lon_r, #speed_r";
		return projection ;
	}
}
