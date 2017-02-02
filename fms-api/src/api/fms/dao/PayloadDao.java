package api.fms.dao;

import java.util.List;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

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
	
	public List<PayloadDto> findAllItem(String tableName){
		
		return null;
	}

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

}
