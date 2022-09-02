package com.assignsecurities.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.MetaDataConfigService;
import com.assignsecurities.dm.MetaDataConfigServiceImpl;
import com.assignsecurities.domain.dm.ObjectImportModel;
import com.assignsecurities.service.impl.LoginService;

import lombok.extern.slf4j.Slf4j;

@Service("DataUploadCommand")
@Component
//@EnableAsync
@EnableScheduling
@Slf4j
public class DataUploadCommand {

	private @Autowired AutowireCapableBeanFactory beanFactory;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private  AssignSecuritiesDataProcessor dataProcessor;
	
//	@Autowired
//	private  DataProcessor dataProcessor;

	private @Autowired AssignSecuritiesObjectService objectService;
	

	private static boolean isJobRinning = false;


	@Scheduled(cron = "${dataload.cron.expression}")
	public void execute() {
		log.info("DataUploadCommand @" + LocalDateTime.now());
		MetaDataConfigService metaDataConfigService = new MetaDataConfigServiceImpl();
		beanFactory.autowireBean(metaDataConfigService);
//		ObjectServiceImpl objectService = new ObjectServiceImpl();
//		beanFactory.autowireBean(objectService);
//		DataProcessor dataProcessor = new DataProcessor();
//		beanFactory.autowireBean(dataProcessor);
//		System.out.println("Beore isJobRinning===>" + isJobRinning);
		if(!isJobRinning) {
			try {
				log.info("Processing Start  @" + LocalDateTime.now());
				isJobRinning = true;
				UserLoginBean userBean = loginService.getUserLoginByMobileNo(AppConstant.SYS_USER);
				List<ObjectImportModel> importModels = objectService.getImports(userBean, DMConstants.FILE_STATUS_SCHEDULED);//and FILE_STATUS_IN_PROCESS
				for (ObjectImportModel importModel : importModels) {
					userBean =loginService.getUserLogin(importModel.getImportedBy());
					log.info("Processing userBean  @" + userBean.getDisplayName());
					if (objectService.existsByFileName(userBean, importModel.getFileName())) {
						objectService.updateImportStatus(importModel.getId(), DMConstants.FILE_STATUS_FAILD_DUPLICATE,
								userBean);
					} else {
						objectService.updateImportStatus(importModel.getId(), DMConstants.FILE_STATUS_IN_PROCESS,
								userBean);
						dataProcessor.processData(importModel.getId(), userBean);
					}
				}
				isJobRinning = false;
				log.info("Processing End  @" + LocalDateTime.now());
			}catch(Exception e) {
				//ignore
				e.printStackTrace();
			}finally {
				isJobRinning = false;
			}
		}
		
		
	}
}
