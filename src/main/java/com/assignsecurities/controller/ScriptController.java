package com.assignsecurities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.AddToFavouriteBean;
import com.assignsecurities.bean.ScriptBean;
import com.assignsecurities.bean.ScriptSearchBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.converter.ScriptConvertor;
import com.assignsecurities.domain.Pagination;
import com.assignsecurities.domain.ScriptModel;
import com.assignsecurities.service.impl.ScriptService;
import com.assignsecurities.validator.FolioValidator;

@RestController
public class ScriptController extends BaseController {

	@Autowired
	private ScriptService scriptService;

	@Autowired
	private FolioValidator folioValidator;

	@RequestMapping(value = "/searchScript", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<ScriptBean> searchScript(@Validated @RequestBody ScriptSearchBean scriptSearchBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)
				|| userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC)
				|| userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE_USER)
				|| userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_END_USER)
				|| userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_FRANCHISE))) {
			return null;
		}
		ArgumentHelper.requiredNonNullAndNonEmptyValue(scriptSearchBean.getFirstName(),
				"Please provide First Name.");
//		if (!ArgumentHelper.isValid(scriptSearchBean.getLastName()) && AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			scriptSearchBean.setLastName(userLoginBean.getApplicationUserBean().getLastName());
//			if(AppConstant.Gender.Male.name().equals( userLoginBean.getApplicationUserBean().getGender()) ) {
//				scriptSearchBean.setLastName(userLoginBean.getApplicationUserBean().getLastName());
//			}else {
//				if(ArgumentHelper.isValid(userLoginBean.getApplicationUserBean().getDefaultSurname()) &&
//						userLoginBean.getApplicationUserBean().getDefaultSurname().equals(AppConstant.DefaultSurname.MadianName.name())) {
//					scriptSearchBean.setLastName(userLoginBean.getApplicationUserBean().getMadianSurname());
//				}
//			}
//		}else if(AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())){
//			scriptSearchBean.setLastName(userLoginBean.getApplicationUserBean().getLastName());
//			if(AppConstant.Gender.Male.name().equals( userLoginBean.getApplicationUserBean().getGender()) ) {
//				scriptSearchBean.setLastName(userLoginBean.getApplicationUserBean().getLastName());
//			}else {
//				if(ArgumentHelper.isValid(userLoginBean.getApplicationUserBean().getDefaultSurname()) &&
//						userLoginBean.getApplicationUserBean().getDefaultSurname().equals(AppConstant.DefaultSurname.MadianName.name())) {
//					scriptSearchBean.setLastName(userLoginBean.getApplicationUserBean().getMadianSurname());
//				}
//			}
//		}
		if(AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())){
			scriptSearchBean.setLastName(userLoginBean.getApplicationUserBean().getLastName());
			if(AppConstant.Gender.Male.name().equals( userLoginBean.getApplicationUserBean().getGender()) ) {
				scriptSearchBean.setLastName(userLoginBean.getApplicationUserBean().getLastName());
			}else {
				if(ArgumentHelper.isValid(userLoginBean.getApplicationUserBean().getDefaultSurname()) &&
						userLoginBean.getApplicationUserBean().getDefaultSurname().equals(AppConstant.DefaultSurname.MadianName.name())) {
					scriptSearchBean.setLastName(userLoginBean.getApplicationUserBean().getMadianSurname());
				}
			}
		}
		return scriptService.searchScript(scriptSearchBean, userLoginBean);
	}

	@RequestMapping(value = "/script/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ScriptBean getScript(@PathVariable("id") Long id) {
		UserLoginBean userLoginBean = getUser();
		return scriptService.getScriptById(id, userLoginBean);
	}

	@RequestMapping(value = "/script/folioNumberOrDpIdClientId/{folioNumberOrDpIdClientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ScriptBean getFolioNumberOrDpIdClientId(
			@PathVariable("folioNumberOrDpIdClientId") String folioNumberOrDpIdClientId) {
		UserLoginBean userLoginBean = getUser();
		return scriptService.getFolioNumberOrDpIdClientId(folioNumberOrDpIdClientId, userLoginBean);
	}

	@RequestMapping(value = "/createFolio", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long createFolio(@Validated @RequestBody ScriptBean script) {
		UserLoginBean userLoginBean = getUser();
		folioValidator.validateFolio(script, userLoginBean);
		return scriptService.save(script);
	}

	@RequestMapping(value = "/addToFavourite", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addToFavourite(@Validated @RequestBody AddToFavouriteBean addToFavourite, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if(!AppConstant.USER_TYPE_END_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())){
			return null;
		}
		ArgumentHelper.requiredNonNullAndNonEmptyList(addToFavourite.getScriptIds(),
				"Please provide script for adding in the Favourite.");
		scriptService.addToFavourite(addToFavourite, userLoginBean);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/myFavouriteScript", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ScriptBean> getMyFavouriteScript() {
		UserLoginBean userLoginBean = getUser();
		return scriptService.getMyFavouriteScript(userLoginBean);
	}
	
	@RequestMapping(value = "/script/companyName/{companyName}/currentPageNo/{currentPageNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ScriptBean> getScripts(@PathVariable("companyName") String companyName,
			@PathVariable("currentPageNo") Integer currentPageNo) {
		UserLoginBean userLoginBean = getUser();
		Integer pageSize = Pagination.DEFAULT_PAGE_SIZE.intValue();
		if (currentPageNo > 1) {

			currentPageNo = (currentPageNo - 1) * pageSize + 1;
		}
		Pagination<ScriptModel> pagination = new Pagination<ScriptModel>();
		pagination.setCurrPageNumber(currentPageNo.longValue());
		pagination.setPageSize(pageSize.longValue());
		pagination = scriptService.getScripts(userLoginBean, companyName, pagination);
		return  ScriptConvertor.convert(pagination.getList());
	}
	
	@RequestMapping(value = "/script/companyName/{companyName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ScriptBean> getScripts(@PathVariable("companyName") String companyName) {
		UserLoginBean userLoginBean = getUser();
		return scriptService.getScriptByCompanyCode(companyName, userLoginBean);
	}
	
}
