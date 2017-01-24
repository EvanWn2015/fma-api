package com.acer.fms.dto;

import java.io.Serializable;

public class tableInfoDto implements Serializable{
	private static final long serialVersionUID = -3445137665904376105L;
	
	private String tableName;
	private String tableStatus;
	private Long readCapacityUnits;
	private Long witeCapacityUnits;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableStatus() {
		return tableStatus;
	}
	public void setTableStatus(String tableStatus) {
		this.tableStatus = tableStatus;
	}
	public Long getReadCapacityUnits() {
		return readCapacityUnits;
	}
	public void setReadCapacityUnits(Long readCapacityUnits) {
		this.readCapacityUnits = readCapacityUnits;
	}
	public Long getWiteCapacityUnits() {
		return witeCapacityUnits;
	}
	public void setWiteCapacityUnits(Long witeCapacityUnits) {
		this.witeCapacityUnits = witeCapacityUnits;
	}
	
	

}
