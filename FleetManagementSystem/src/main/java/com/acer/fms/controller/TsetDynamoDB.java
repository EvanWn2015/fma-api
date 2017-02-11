package com.acer.fms.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import api.fms.dto.PayloadDto;
import api.fms.server.DnTableService;
import api.fms.server.PayloadService;
import api.fms.vo.PayloadVo;
import fms.common.Util.Util;

@Controller
public class TsetDynamoDB {

	static final Logger LOG = LoggerFactory.getLogger(TsetDynamoDB.class);

	@Autowired
	DnTableService dnTableService;

	@Autowired
	PayloadService payloadService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String init() {
		LOG.info("~~ init ~~");
		return "tsetDynamoDB";
	}

	@RequestMapping(value = "/createTable", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String createTable() {
		String status = "";
		try {
			status = dnTableService.createTable("testTable");
			LOG.info("createTable : {}", status);
		} catch (InterruptedException e) {
			LOG.info("createTable : {}", e.getMessage());
		}
		return status;
	}

	@RequestMapping(value = "/putItem", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String putItem() {
		String tableName = "testTable";
		PayloadVo payloadVo = setTestPayloadVo(22);
		String status = "fail";
		try {
			payloadService.insert(tableName, payloadVo);
			status = "success";
		} catch (NullPointerException | InterruptedException e) {
			LOG.info("createTable : {}", e.getMessage());
		}
		return status;
	}

	@RequestMapping(value = "/putItem2", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String putItem2() {
		String tableName = "testTable";
		PayloadVo payloadVo = setTestPayloadVo(0);
		String status = "fail";
		try {
			payloadService.insert(tableName, payloadVo);
			status = "success";
		} catch (NullPointerException | InterruptedException e) {
			LOG.info("createTable : {}", e.getMessage());
		}
		return status;
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public @ResponseBody PayloadDto query() throws JsonProcessingException {
		String tableName = "testTable";
		PayloadDto dto = null;
		try {
			dto = payloadService.findByPackId(tableName, 22);
			LOG.info("PayloadDto : {}", Util.toJSon(dto));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			LOG.info("createTable : {}", e1.getMessage());
		}
		return dto;
	}

	@RequestMapping(value = "/deleteTable", method = RequestMethod.POST)
	public @ResponseBody String deleteTable() {
		String tableName = "testTable";
		String status = "";
		try {
			status = dnTableService.deleteTable(tableName);
			LOG.info("deleteTable : {}", status);
		} catch (InterruptedException e) {
			LOG.info("deleteTable : {}", e.getMessage());
		}
		return status;
	}

	@RequestMapping(value = "/getCount", method = RequestMethod.POST)
	public @ResponseBody Integer getCount() {
		int count = dnTableService.getTablesCount();
		LOG.info("getCount : {}", count);
		return count;
	}

	@RequestMapping(value = "/getByToday", method = RequestMethod.POST)
	public @ResponseBody List<PayloadDto> getPayloadDtoListByToday() {
		String tableName = "testTable";
		List<PayloadDto> dtos = null;
		try {
			dtos = payloadService.findPayloadDtoListByToday(tableName, 22);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dtos;
	}

	/**
	 * TEST DATA
	 * 
	 * @return
	 */
	private PayloadVo setTestPayloadVo(int packId) {

		Calendar calendar = Calendar.getInstance();
		Random random = new Random();
		PayloadVo payloadVo = new PayloadVo();

		payloadVo.setPackId(random.nextInt(100));
		if (packId != 0) {
			payloadVo.setPackId(packId);
		}
		payloadVo.setTimestamp(calendar.getTimeInMillis());
		payloadVo.setVoltage(random.nextDouble());
		payloadVo.setCurrent(random.nextDouble());
		payloadVo.setSoc(random.nextInt(99));
		payloadVo.setSoh(random.nextInt(99));
		payloadVo.setTemperature(random.nextInt(99));
		payloadVo.setAlert(random.nextInt(10) + "test");
		payloadVo.setLatitude(random.nextDouble());
		payloadVo.setLongitude(random.nextDouble());
		payloadVo.setSpeed(random.nextInt());

		return payloadVo;
	}

}
