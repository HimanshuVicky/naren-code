package com.assignsecurities.scheduler;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.service.impl.LoginService;

import lombok.extern.slf4j.Slf4j;

@Service("TokenCleaner")
@Component
@EnableAsync
@Slf4j
public class TokenCleaner {

	@Autowired
	private LoginService loginService;

	
	@Scheduled(cron = "${token.cleaner.expression}")
	public void execute() throws ServiceException {
		log.info("TokenCleaner @" + LocalDateTime.now());
		loginService.clearToken();
	}
}
