package com.acer.fms.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Index {

	static final Logger logger = LoggerFactory.getLogger(Index.class);

	
	@RequestMapping(value = { "/" })
	public String init(HttpSession session) {
		return "index";
	}
	
	
	@RequestMapping(value = { "/createTable" }, method = RequestMethod.POST)
	public String createTable() {
		return "index";
	}
	
	
	@RequestMapping(value = { "/login", "/" }, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView verifyUser(Model model, HttpSession session) {
	
		return new ModelAndView("login");

	}
}
