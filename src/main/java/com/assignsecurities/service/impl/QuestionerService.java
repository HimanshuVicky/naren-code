package com.assignsecurities.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.bean.QuestionerBean;
import com.assignsecurities.converter.QuestionerConverter;
import com.assignsecurities.repository.impl.QuestionerRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class QuestionerService {
	@Autowired
	private QuestionerRepo questionerRepo;

	public List<QuestionerBean> getQuestioners() {
		return QuestionerConverter.convert(questionerRepo.getQuestioners());
	}
	public List<QuestionerBean> getActiveQuestioners() {
		return QuestionerConverter.convert(questionerRepo.getActiveQuestioners());
	}
}
