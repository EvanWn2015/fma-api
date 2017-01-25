package com.acer.fms.vo;

import java.io.Serializable;

public class PayloadVo implements Serializable {
	private static final long serialVersionUID = -5370527696614210407L;

	private Long packId;
	private Long timestamp;
	private double voltage;
	private double current;
	private Integer soc;
	private Integer soh;
	private Integer temperature;
	private String alert;
	private double latitude;
	private double longitude;
	private Integer speed;

	public PayloadVo() {
		this.timestamp = 1L;
		this.voltage = 1.0;
		this.current = 1.0;
		this.soc = 1;
		this.soh = 1;
		this.temperature = 1;
		this.alert = "1";
		this.latitude = 1.1;
		this.longitude = 1.1;
		this.speed = 1;
	}

	public Long getPackId() {
		return packId;
	}

	public void setPackId(Long packId) {
		this.packId = packId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public double getCurrent() {
		return current;
	}

	public void setCurrent(double current) {
		this.current = current;
	}

	public Integer getSoc() {
		return soc;
	}

	public void setSoc(Integer soc) {
		this.soc = soc;
	}

	public Integer getSoh() {
		return soh;
	}

	public void setSoh(Integer soh) {
		this.soh = soh;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}