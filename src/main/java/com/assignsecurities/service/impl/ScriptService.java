package com.assignsecurities.service.impl;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.app.util.DateUtil;
import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.bean.AddToFavouriteBean;
import com.assignsecurities.bean.ScriptBean;
import com.assignsecurities.bean.ScriptSearchBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.converter.AddToFavouriteConvertor;
import com.assignsecurities.converter.ScriptConvertor;
import com.assignsecurities.converter.ScriptSearchConvertor;
import com.assignsecurities.domain.BSEScriptDetailsModel;
import com.assignsecurities.domain.NSEScriptDetailsModel;
import com.assignsecurities.domain.Pagination;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.domain.ScriptPriceUpdateModel;
import com.assignsecurities.domain.ScriptSearchLogModel;
import com.assignsecurities.domain.dm.ObjectImportModel;
import com.assignsecurities.repository.impl.BSEScriptRepo;
import com.assignsecurities.repository.impl.NSEScriptRepo;
import com.assignsecurities.repository.impl.ScriptRepo;
import com.assignsecurities.repository.impl.ScriptSearchLogRepo;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
@Slf4j
public class ScriptService {

	@Autowired
	private NSEScriptRepo nSEScriptRepo;

	@Autowired
	private BSEScriptRepo bSEScriptRepo;

	@Autowired
	private ScriptRepo scriptRepo;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ScriptSearchLogRepo scriptSearchLogRepo;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void loadAndUpdateScriptValue(String scriptFileRoot) {
		log.info("Start of loadAndUpdateScriptValue");
		loadAndUpdateNseScriptValue(scriptFileRoot);

		loadAndUpdateBseScriptValue(scriptFileRoot);
		updateMarketValue();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Throwable.class)
	public void updateMarketValue() {
		log.info("Start of updateMarketValue");
		List<ScriptPriceUpdateModel> scripts = new ArrayList<>();
		List<NSEScriptDetailsModel> nseScriptDetailsModels = nSEScriptRepo.getScripts();
		nseScriptDetailsModels.forEach(nseScript->{
			scripts.add(ScriptPriceUpdateModel.builder().unitPrice(nseScript.getClose()).isinCode(nseScript.getIsIn()).build());
		});
		scriptRepo.updatePriceForNse(scripts);
		List<ScriptPriceUpdateModel> bseScripts = new ArrayList<>();
		List<BSEScriptDetailsModel> bseScriptDetailsModels = bSEScriptRepo.getScripts();
		bseScriptDetailsModels.forEach(bseScript->{
			bseScripts.add(ScriptPriceUpdateModel.builder().unitPrice(bseScript.getClose()).securityCode(bseScript.getScCode()).build());
		});
		scriptRepo.updatePriceForBse(bseScripts);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public Long save(ScriptBean scriptBean) {
		ScriptModel scriptModel = ScriptConvertor.convert(scriptBean);
//		if(scriptModel.getMarketPrice())
		double marketPrice = 	scriptModel.getMarketPrice() * scriptModel.getNumberOfShare();
		scriptModel.setMarketPrice(marketPrice);
		return scriptRepo.add(scriptModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void update(ScriptBean scriptBean) {
		ScriptModel scriptModel = ScriptConvertor.convert(scriptBean);
		scriptRepo.update(scriptModel);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public void addToFavourite(AddToFavouriteBean bean, UserLoginBean userLoginBean) {
//		boolean getNewUser = true;
//		if(Objects.isNull(bean.getUserId())) {
//			bean.setUserId(userLoginBean.getId());
//			getNewUser = false;
//		}
//		UserLoginBean userLoginBeanTemp = null;
//		if(getNewUser) {
//			userLoginBeanTemp = loginService.getUserLogin(bean.getUserId());
//		}else {
//			userLoginBeanTemp = userLoginBean;
//		}
		bean.setUserId(userLoginBean.getId());
		List<ScriptModel> scriptModels = scriptRepo.getMyFavouriteScript(userLoginBean);
		List<ValidationError> errorList = new ArrayList<>();
		scriptModels.forEach(script->{
			if (bean.getScriptIds().contains(script.getId())) {
//				String msg = String.format("Script %s already there in the Favourite.", script.getFolioNumberOrDpIdClientId());
				String msg = String.format("Script %s already there in the Favourite.", script.getId());
				errorList.add(ValidationError.builder().message(msg).build());
			}
		});
		
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		
		scriptRepo.addToFavourite(AddToFavouriteConvertor.convert(bean));
	}
	public ScriptBean getScriptById(Long id, UserLoginBean userLoginBean) {
		ScriptBean scriptBean = ScriptConvertor.convert(scriptRepo.getScriptById(id));
		boolean mask = isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		maskFolioNumberOrDpIdClientId(scriptBean, mask);
		return scriptBean;
	}

	
	public ScriptBean getScriptBySecurityCode(String securityCode, UserLoginBean userLoginBean) {
		ScriptBean scriptBean = ScriptConvertor.convert(scriptRepo.getScriptBySecurityCode(securityCode));
		boolean mask = isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		maskFolioNumberOrDpIdClientId(scriptBean, mask);
		return scriptBean;
	}

	public ScriptBean getScriptByFolioNumber(String folioNumber, UserLoginBean userLoginBean) {
		ScriptBean scriptBean = ScriptConvertor.convert(scriptRepo.getScriptByFolioNumber(folioNumber));
		boolean mask = isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		maskFolioNumberOrDpIdClientId(scriptBean, mask);
		return scriptBean;
	}
	
	public ScriptBean getScriptByApplicationScriptId(Long applicationScriptId, UserLoginBean userLoginBean) {
		ScriptBean scriptBean = ScriptConvertor.convert(scriptRepo.getScriptByApplicationScriptId(applicationScriptId));
		boolean mask = isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		maskFolioNumberOrDpIdClientId(scriptBean, mask);
		return scriptBean;
	}
	

	public ScriptBean getFolioNumberOrDpIdClientId(String folioNumberOrDpIdClientId, UserLoginBean userLoginBean) {
		ScriptBean scriptBean = ScriptConvertor
				.convert(scriptRepo.getFolioNumberOrDpIdClientId(folioNumberOrDpIdClientId));
		boolean mask = isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		maskFolioNumberOrDpIdClientId(scriptBean, mask);
		return scriptBean;
	}

	public List<ScriptBean> getFolioNumberOrDpIdClientIdAndActualDateTransferIEPF(String folioNumberOrDpIdClientId,
			LocalDateTime actualDateTransferIEPF, UserLoginBean userLoginBean) {
		List<ScriptBean> scriptBeans = ScriptConvertor
				.convert(scriptRepo.getFolioNumberOrDpIdClientIdAndActualDateTransferIEPF(folioNumberOrDpIdClientId,
						actualDateTransferIEPF));
		boolean mask = isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		maskFolioNumberOrDpIdClientId(scriptBeans, mask);
		return scriptBeans;
	}
	
	public Boolean isFolioNumberOrDpIdClientIdAndActualDateTransferIEPF(String folioNumberOrDpIdClientId,
			LocalDateTime actualDateTransferIEPF, UserLoginBean userLoginBean) {
		return scriptRepo.isFolioNumberOrDpIdClientIdAndActualDateTransferIEPF(folioNumberOrDpIdClientId,
						actualDateTransferIEPF);
	}
	
	
	public List<ScriptBean> getScriptByCompanyCode(String companyName, UserLoginBean userLoginBean) {
		List<ScriptBean> scriptBeans = ScriptConvertor.convert(scriptRepo.getScriptByCompanyCode(companyName));
		boolean mask = isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		scriptBeans.forEach(scriptBean->{
			maskFolioNumberOrDpIdClientId(scriptBean, mask);
		});
		return scriptBeans;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Throwable.class)
	public List<ScriptBean> searchScript(ScriptSearchBean scriptSearchBean, UserLoginBean userLoginBean) {
		List<ScriptBean> scriptBeans = ScriptConvertor
				.convert(scriptRepo.searchScript(ScriptSearchConvertor.convert(scriptSearchBean)));
		
		Double totalScriptCost = scriptBeans.stream()
				.filter(s->s.getMarketPrice()!=null).mapToDouble(ScriptBean::getMarketPrice).sum();
		
		ScriptSearchLogModel logModel = ScriptSearchLogModel.builder()
				.firstName(scriptSearchBean.getFirstName()).lastName(scriptSearchBean.getLastName())
				.searchBy(userLoginBean.getId()).totalScriptCost(totalScriptCost)
				.build();
		
		scriptSearchLogRepo.add(logModel);
		boolean mask = isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		maskFolioNumberOrDpIdClientId(scriptBeans, mask);
		return scriptBeans;
	}
	public List<ScriptBean> getMyFavouriteScript(UserLoginBean userLoginBean) {
		List<ScriptBean> scriptBeans = ScriptConvertor
				.convert(scriptRepo.getMyFavouriteScript(userLoginBean));
		boolean mask = isFolioNumberOrDpIdClientIdMasked(userLoginBean);
		maskFolioNumberOrDpIdClientId(scriptBeans, mask);
		return scriptBeans;
	}
	
	
	public Pagination<ScriptModel> getScripts(UserLoginBean userLoginBean, String companyName,
			  Pagination<ScriptModel> pagination) {
		return scriptRepo.getScripts(userLoginBean, companyName, pagination);
	}
	
	public boolean isFolioNumberOrDpIdClientIdMasked(UserLoginBean userLoginBean) {
		boolean mask = Boolean.FALSE;// TODO based on status
		if (AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			mask = Boolean.TRUE;
		}
		return mask;
	}

	public void maskFolioNumberOrDpIdClientId(List<ScriptBean> scriptBeans, boolean mask) {
		scriptBeans.forEach(script -> {
			maskFolioNumberOrDpIdClientId(script, mask);
		});

	}

	private void maskFolioNumberOrDpIdClientId(ScriptBean scriptBean, boolean mask) {
		if (mask) {
			int preMaxLength =1;
			int postMaxLength =3;
			if (scriptBean.isFolioNumberExists()) {
				scriptBean.setFolioNumber(StringUtil.maskCardNumber(scriptBean.getFolioNumber(), preMaxLength, postMaxLength));
			} else if (scriptBean.isDpIdClientIdExists()) {
				scriptBean.setDpIdClientId(StringUtil.maskCardNumber(scriptBean.getDpIdClientId(), preMaxLength, postMaxLength));
			} else {
				scriptBean.setFolioNumber("xxxxx");
				scriptBean.setDpIdClientId("xxxxx");
			}
		}
	}

	private void loadAndUpdateBseScriptValue(String scriptFileRoot) {
		String dateFormat = "dd-MMM-yyyy";
		String bseDestination = scriptFileRoot + "bse";
		String bseCSVFile;
		Optional<Path> bsePath;
		try (Stream<Path> paths = Files.walk(Paths.get(bseDestination))) {
			bsePath = paths.filter(Files::isRegularFile).findFirst();
		} catch (IOException e1) {
			throw new ServiceException(e1);
		}

		List<BSEScriptDetailsModel> bseScriptDetailsModels = new ArrayList<>();
		if (bsePath.isPresent()) {
			bseCSVFile = bsePath.get().toAbsolutePath().toString();
			try (CSVReader reader = new CSVReader(new FileReader(bseCSVFile))) {
				String[] lineInArray;
				long lineCounter = 0;
				while ((lineInArray = reader.readNext()) != null) {
//			          System.out.println(lineInArray[0] + lineInArray[15] + "etc...");
//			    	  System.out.println("lineCounter-->"+lineCounter);
					if (lineCounter++ > 0) {
						BSEScriptDetailsModel bseScriptDetailsModel = BSEScriptDetailsModel.builder().id(lineCounter)
								.scCode(lineInArray[0]).scName(lineInArray[1]).scGroup(lineInArray[2])
								.scType(lineInArray[3]).open(Double.parseDouble(lineInArray[4]))
								.high(Double.parseDouble(lineInArray[5])).low(Double.parseDouble(lineInArray[6]))
								.close(Double.parseDouble(lineInArray[7])).last(Double.parseDouble(lineInArray[8]))
								.prevClose(Double.parseDouble(lineInArray[9]))
								.noTrades(Double.parseDouble(lineInArray[10]))
								.noOfShrs(Double.parseDouble(lineInArray[11]))
								.netTurnOv(Double.parseDouble(lineInArray[12])).tdcloindi(lineInArray[13])
								.isnCode(lineInArray[14])
								.tradingDate(DateUtil.getLocalDateTime(lineInArray[15], dateFormat))
								.filler2(lineInArray[16]).filler3(lineInArray[17]).build();
						bseScriptDetailsModels.add(bseScriptDetailsModel);
					}
				}
			} catch (IOException | CsvValidationException e) {
				throw new ServiceException(e);
			}
		}
		log.info("bseScriptDetailsModels()--->" + bseScriptDetailsModels.size());
//		System.out.println("bseScriptDetailsModels()--->" + bseScriptDetailsModels.size());
		bSEScriptRepo.addAll(bseScriptDetailsModels);
	}

	private void loadAndUpdateNseScriptValue(String scriptFileRoot) {
		String dateFormat = "dd-MMM-yyyy";

		String nseDestination = scriptFileRoot + "nse";
		String nseCSVFile;
		Optional<Path> nsePath;
		try (Stream<Path> paths = Files.walk(Paths.get(nseDestination))) {
			nsePath = paths.filter(Files::isRegularFile).findFirst();
		} catch (IOException e1) {
			throw new ServiceException(e1);
		}
		List<NSEScriptDetailsModel> nseScriptDetailsModels = new ArrayList<>();
		if (nsePath.isPresent()) {
			nseCSVFile = nsePath.get().toAbsolutePath().toString();
			try (CSVReader reader = new CSVReader(new FileReader(nseCSVFile))) {
				String[] lineInArray;
				long lineCounter = 0;
				while ((lineInArray = reader.readNext()) != null) {
//			          System.out.println(lineInArray[0] + lineInArray[10] + "etc...");
//			    	  System.out.println("lineCounter-->"+lineCounter);
					if (lineCounter++ > 0) {
						NSEScriptDetailsModel nseScriptDetailsModel = NSEScriptDetailsModel.builder().id(lineCounter)
								.symbol(lineInArray[0]).series(lineInArray[1]).open(Double.parseDouble(lineInArray[2]))
								.high(Double.parseDouble(lineInArray[3])).low(Double.parseDouble(lineInArray[4]))
								.close(Double.parseDouble(lineInArray[5])).last(Double.parseDouble(lineInArray[6]))
								.prevClose(Double.parseDouble(lineInArray[7]))
								.totTrdQty(Double.parseDouble(lineInArray[8]))
								.totTrdVal(Double.parseDouble(lineInArray[9]))
								.timeStamp(DateUtil.getLocalDateTime(lineInArray[10], dateFormat))
								.totalTrades(Double.parseDouble(lineInArray[11])).isIn(lineInArray[12]).build();

						nseScriptDetailsModels.add(nseScriptDetailsModel);
					}
				}
			} catch (IOException | CsvValidationException e) {
				throw new ServiceException(e);
			}
		}
		log.info("nseScriptDetailsModels.size()--->" + nseScriptDetailsModels.size());
		nSEScriptRepo.addAll(nseScriptDetailsModels);
	}

	public static void main(String[] args) {
		ScriptService scriptService = new ScriptService();
		scriptService.loadAndUpdateScriptValue("E:/ShareProject/");
	}
}
