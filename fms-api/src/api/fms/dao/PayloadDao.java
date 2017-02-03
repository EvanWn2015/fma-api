package api.fms.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import api.fms.constants.DNDBConstants;
import api.fms.dto.PayloadDto;
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

	public void insert(String tableName, PayloadVo payloadVo) throws InterruptedException, NullPointerException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);

		Item item = new Item()
				.withPrimaryKey(DNDBConstants.PACKID, payloadVo.getPackId())
				.withNumber(DNDBConstants.TIMESTAMP, payloadVo.getTimestamp())
				.withNumber(DNDBConstants.VOLTAGE, payloadVo.getVoltage())
				.withNumber(DNDBConstants.CURRENT, payloadVo.getCurrent())
				.withNumber(DNDBConstants.SOC, payloadVo.getSoc())
				.withNumber(DNDBConstants.SOH, payloadVo.getSoh())
				.withNumber(DNDBConstants.TEMPERATURE, payloadVo.getTemperature())
				.withString(DNDBConstants.ALERT, payloadVo.getAlert())
				.withNumber(DNDBConstants.LATITUDE, payloadVo.getLatitude())
				.withNumber(DNDBConstants.LONGITUDE, payloadVo.getLongitude())
				.withNumber(DNDBConstants.SPEED, payloadVo.getSpeed());

		table.putItem(item);
	}

	public List<PayloadDto> findAllItem(String tableName) {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		QuerySpec querySpec = new QuerySpec();

		ItemCollection<QueryOutcome> items = null;
		Iterator<Item> iterator = null;

//		List<Item> itemList = new ArrayList<Item>();
		try {
			items = table.query(querySpec);
			iterator = items.iterator();
//			while (iterator.hasNext()) {
////				itemList.add(iterator.next());
//			}
		} catch (Exception e) {
			System.err.println("Unable to query movies from 1985");
			System.err.println(e.getMessage());
		}
		return setPayloadDtoListByIterator(iterator);
	}

	private List<PayloadDto> setPayloadDtoListByIterator(Iterator<Item> iterator) {
		List<PayloadDto> payloadDtoList = new ArrayList<PayloadDto>();
		while (iterator.hasNext()) {
			Item item = iterator.next();
			PayloadDto dto = new PayloadDto ();
			dto.setPackId(item.getString(DNDBConstants.PACKID));
			dto.setTimestamp(item.getLong(DNDBConstants.TIMESTAMP));
			dto.setVoltage(item.getDouble(DNDBConstants.VOLTAGE));
			dto.setCurrent(item.getDouble(DNDBConstants.CURRENT));
			dto.setSoc(item.getInt(DNDBConstants.SOC));
			dto.setSoh(item.getInt(DNDBConstants.SOH));
			dto.setTemperature(item.getInt(DNDBConstants.TEMPERATURE));
			dto.setAlert(item.getString(DNDBConstants.ALERT));
			dto.setLatitude(item.getDouble(DNDBConstants.LATITUDE));
			dto.setLongitude(item.getDouble(DNDBConstants.LONGITUDE));
			dto.setSpeed(item.getInt(DNDBConstants.SPEED));
			payloadDtoList.add(dto);
		}
		return payloadDtoList;
	}

	public PayloadDto findByPackId(String tableName, String packId) {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(DNDBConstants.PACKID, packId);

		Item outcome = table.getItem(spec);

		PayloadDto dto = new PayloadDto();
		dto.setPackId(outcome.getString(DNDBConstants.PACKID));
		dto.setTimestamp(outcome.getLong(DNDBConstants.TIMESTAMP));
		dto.setVoltage(outcome.getDouble(DNDBConstants.VOLTAGE));
		dto.setCurrent(outcome.getDouble(DNDBConstants.CURRENT));
		dto.setSoc(outcome.getInt(DNDBConstants.SOC));
		dto.setSoh(outcome.getInt(DNDBConstants.SOH));
		dto.setTemperature(outcome.getInt(DNDBConstants.TEMPERATURE));
		dto.setAlert(outcome.getString(DNDBConstants.ALERT));
		dto.setLatitude(outcome.getDouble(DNDBConstants.LATITUDE));
		dto.setLongitude(outcome.getDouble(DNDBConstants.LONGITUDE));
		dto.setSpeed(outcome.getInt(DNDBConstants.SPEED));

		return dto;
	}

	public void deleteItemByPackId(String tableName, String packId) {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		Table table = dynamoDB.getTable(tableName);

		DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey(DNDBConstants.PACKID, packId);
		DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);
		// Check the response.
		System.out.println("Printing item that was deleted...");
		System.out.println(outcome.getItem().toJSONPretty());

	}

}
