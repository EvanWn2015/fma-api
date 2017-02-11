package api.fms.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

import api.fms.constants.DNDBConstants;
import api.fms.dto.TableInfoDto;

public class DnTableService {

	/**
	 * 建立 Table
	 * 
	 * @param tableName
	 * @return Status
	 * @throws InterruptedException
	 */
	public String createTable(String tableName) throws InterruptedException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		if (!checkRepeatTable(tableName)) {
			Table table = dynamoDB.createTable(tableName,
					Arrays.asList(new KeySchemaElement(DNDBConstants.PACKID, KeyType.HASH),
							new KeySchemaElement(DNDBConstants.TIMESTAMP, KeyType.RANGE)),
					Arrays.asList(new AttributeDefinition(DNDBConstants.PACKID, ScalarAttributeType.N),
							new AttributeDefinition(DNDBConstants.TIMESTAMP, ScalarAttributeType.N)),
					new ProvisionedThroughput(5L, 5L));
			table.waitForActive();
			return "success";
		}
		return "fail";
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
	public Integer getTablesCount() {
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
	 * 取得全部 Table
	 * 
	 * @return ArrayList<String>
	 */
	public List<String> listTables() {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		TableCollection<ListTablesResult> tables = dynamoDB.listTables();
		Iterator<Table> iterator = tables.iterator();

		List<String> tableList = new ArrayList<String>();
		while (iterator.hasNext()) {
			Table table = iterator.next();
			tableList.add(table.getTableName());
		}
		return tableList;
	}

	/**
	 * 刪除 Table
	 * 
	 * @param tableName
	 * @throws InterruptedException
	 */
	public String deleteTable(String tableName) throws InterruptedException {
		DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
		if (checkRepeatTable(tableName)) {
			Table table = dynamoDB.getTable(tableName);
			table.delete();
			table.waitForDelete();
			return "success";
		}
		return "fail";
	}

	/**
	 * 檢查是否有重複 Table
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean checkRepeatTable(String tableName) {
		List<String> tableList = listTables();
		boolean status = false;
		for (String table : tableList) {
			if (tableName.equals(table)) {
				status = true;
				break;
			}
		}
		return status;
	}

	// private String getTableStatus(String tableName){
	// DynamoDB dynamoDB = DBconnect.getInstance().getDynamoDB();
	// TableDescription td = dynamoDB.getTable(tableName).describe();
	// System.out.println(td.getTableName());
	// return td.getTableStatus();
	// }
}
