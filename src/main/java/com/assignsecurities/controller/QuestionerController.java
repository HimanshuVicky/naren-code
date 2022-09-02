package com.assignsecurities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.bean.QuestionerBean;
import com.assignsecurities.service.impl.QuestionerService;

@RestController
public class QuestionerController extends BaseController {

	@Autowired
	private QuestionerService questionerService;

	@RequestMapping(value = "/getQuestioners", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<QuestionerBean> getAllFranchise() {
		return questionerService.getQuestioners();
	}

	@RequestMapping(value = "/getAactiveQuestioners", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<QuestionerBean> getAactiveQuestioners() {
		return questionerService.getActiveQuestioners();
	}
}
