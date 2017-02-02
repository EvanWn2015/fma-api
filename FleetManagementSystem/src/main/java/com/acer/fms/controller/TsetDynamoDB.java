package com.acer.fms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acer.fms.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;

import api.fms.dto.PayloadDto;
import api.fms.server.DnTableService;
import api.fms.server.PayloadService;
import api.fms.vo.PayloadVo;

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
	@ResponseBody
	public String createTable(Model model) {
		try {
			dnTableService.createTable("testTable");
		} catch (InterruptedException e) {
			LOG.info("createTable : {}", e.getMessage());
		}
		return "tsetDynamoDB";
	}

	@RequestMapping(value = "/putItem", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String putItem() {
		PayloadVo payloadVo = new PayloadVo("22");
		try {
			payloadService.insert("testTable", payloadVo);
		} catch (NullPointerException | InterruptedException e) {
			LOG.info("createTable : {}", e.getMessage());
		}
		return "putItem";
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public String query() {
		PayloadDto dto = payloadService.findByPackId("testTable", "22");
		try {
			LOG.info("PayloadDto : {}", Util.getInstance().toJSon(dto));
		} catch (JsonProcessingException e) {
			LOG.info("createTable : {}", e.getMessage());
		}
		return "query";
	}

	@RequestMapping(value = "/deleteTable", method = RequestMethod.POST)
	@ResponseBody
	public String deleteTable() {
		try {
			dnTableService.deleteTable("test");
		} catch (InterruptedException e) {
			LOG.info("createTable : {}", e.getMessage());
		}
		return "deleteTable";
	}

}
