package com.acer.fms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.fms.dto.PayloadDto;
import api.fms.server.DNDBService;
import api.fms.vo.PayloadVo;

@Controller
public class TsetDynamoDB {

	static final Logger LOG = LoggerFactory.getLogger(TsetDynamoDB.class);

	@Autowired
	DNDBService dNDBService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String init() {
		LOG.info("~~ init ~~");
		return "tsetDynamoDB";
	}

	@RequestMapping(value = "/createTable", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String createTable(Model model) {
		LOG.info("createTable : {}", "in");
		try {
			dNDBService.createTable("testTable");
		} catch (InterruptedException e) {
			LOG.info("createTable : {}", e);
		}
		return "tsetDynamoDB";
	}

	@RequestMapping(value = "/putItem", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String putItem() {
		LOG.info("~~ putItem ~~");
		PayloadVo payloadVo = new PayloadVo("22");
		try {
			dNDBService.insert("testTable", payloadVo);
		} catch (NullPointerException | InterruptedException e) {
			LOG.info("createTable : {}", e.getMessage());
		}
		return "putItem";
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public String query() {
		LOG.info("~~ query ~~");
		PayloadDto dto = dNDBService.findByPackId("testTable", "22");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			LOG.info("PayloadDto : {}", objectMapper.writeValueAsString(dto));
		} catch (JsonProcessingException e) {
			LOG.info("createTable : {}", e.getMessage());
		}
		return "query";
	}

	@RequestMapping(value = "/deleteTable", method = RequestMethod.POST)
	@ResponseBody
	public String deleteTable() {
		LOG.info("~~ delete ~~");
		try {
			dNDBService.deleteTable("testTable");
		} catch (InterruptedException e) {
			LOG.error("createTable : {}", e);
			e.printStackTrace();
		}
		return "deleteTable";
	}

}
