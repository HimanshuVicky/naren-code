package com.assignsecurities.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.assignsecurities.app.util.ArrayUtil;
import com.assignsecurities.bean.AddFolioToApplicationBean;
import com.assignsecurities.bean.AddNewFolioToApplicationBean;
import com.assignsecurities.bean.ApplicationBean;
import com.assignsecurities.bean.ApplicationDetailsBean;
import com.assignsecurities.bean.ApplicationFeeBean;
import com.assignsecurities.bean.ApplicationQuestionerBean;
import com.assignsecurities.bean.ApplicationScriptBean;
import com.assignsecurities.bean.CreateApplicationBean;
import com.assignsecurities.bean.FeeBean;
import com.assignsecurities.bean.QuestionerBean;
import com.assignsecurities.bean.RemoveApplicationScriptBean;
import com.assignsecurities.bean.UpdateApplicationFeeBean;
import com.assignsecurities.bean.UpdateApplicationStatusBean;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.bean.ValidationError;
import com.assignsecurities.service.impl.ApplicationService;
import com.assignsecurities.service.impl.DashBoardService;
import com.assignsecurities.validator.FolioValidator;

@RestController
public class ApplicationController extends BaseController {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private FolioValidator folioValidator;

	@RequestMapping(value = "/createApplication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createApplication(@Validated @RequestBody CreateApplicationBean createApplicationBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		ArgumentHelper.requiredNonNullAndNonEmptyList(createApplicationBean.getScriptIds(),
				"Please provide script for validation.");
		ArgumentHelper.requiredNonNullAndNonEmptyValue(userLoginBean.getApplicationUserBean().getEmailId(),
				"Please update the email in from the profile.");
		applicationService.createApplication(createApplicationBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/application/byId/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationDetailsBean getApplicationById(@PathVariable("id") Long id) {
		UserLoginBean userLoginBean = getUser();
		return applicationService.getById(id, userLoginBean);
	}

	@RequestMapping(value = "/application/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ApplicationBean> getApplications(@PathVariable("status") String status) {
		UserLoginBean userLoginBean = getUser();
		if (DashBoardService.displayApplicationStatusMapping.containsValue(status)) {
			// status
			status = ArrayUtil.getKey(DashBoardService.displayApplicationStatusMapping, status);
		}
		return applicationService.getApplicationByStatus(status, userLoginBean);
	}

	@RequestMapping(value = "/addNewFolioToApplication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<ApplicationScriptBean> addNewFolioToApplication(
			@Validated @RequestBody AddNewFolioToApplicationBean addNewFolioToApplicationBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC))) {
			return null;
		}
		ArgumentHelper.requiredNonNullAndNonEmptyValue(addNewFolioToApplicationBean.getApplicationId() + "",
				"Please provide application Id. ");
		ArgumentHelper.requiredObjectNonNull(addNewFolioToApplicationBean.getScript(),
				"Please provide script/Folio to add into the application.");
		folioValidator.validateFolio(addNewFolioToApplicationBean.getScript(), userLoginBean);
	
		return applicationService.addNewFolioToApplication(addNewFolioToApplicationBean, userLoginBean);
	}

	@RequestMapping(value = "/addFolioToApplication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<ApplicationScriptBean> addFolioToApplication(
			@Validated @RequestBody AddFolioToApplicationBean addFolioToApplicationBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC))) {
			return null;
		}
		ArgumentHelper.requiredNonNullAndNonEmptyValue(addFolioToApplicationBean.getApplicationId() + "",
				"Please provide application Id. ");
		ArgumentHelper.requiredNonNullAndNonEmptyList(addFolioToApplicationBean.getScriptIds(),
				"Please provide script Ids. ");
		return applicationService.addFolioToApplication(addFolioToApplicationBean, userLoginBean);
	}

	@RequestMapping(value = "/updateApplicationStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateApplicationStatus(
			@Validated @RequestBody UpdateApplicationStatusBean applicationStatusBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC))) {
			return null;
		}
		ArgumentHelper.requiredNonNullAndNonEmptyValue(applicationStatusBean.getApplicationId() + "",
				"Please provide application Id. ");
		ArgumentHelper.requiredNonNullAndNonEmptyValue(applicationStatusBean.getStatus(),
				"Please provide application status.");
		if(applicationStatusBean.getStatus().equals(AppConstant.APPLICATION_STATUS_REJECT) ) {
			ArgumentHelper.requiredNonNullAndNonEmptyValue(applicationStatusBean.getRemarks(),
					"Please provide comments/remarks for the application rejection.");
		}
		if(Objects.nonNull(applicationStatusBean.getApplicationFeeBean())) {
			validateInput(applicationStatusBean.getApplicationFeeBean());
		}
		
		applicationService.updateApplicationStatus(applicationStatusBean, userLoginBean);
		return ResponseEntity.ok().build();
	}

//	@RequestMapping(value = "/updateApplicationFee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Void> updateApplicationFee(
//			@Validated @RequestBody UpdateApplicationFeeBean applicationFeeBean) {
//		UserLoginBean userLoginBean = getUser();
//		validateUpdateApplicationFee(applicationFeeBean);
//		applicationService.updateApplicationFee(applicationFeeBean, userLoginBean);
//		return ResponseEntity.ok().build();
//	}

	@RequestMapping(value = "/saveOrRemoveApplicationFolio", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> saveOrRemoveApplicationFolio(
			@Validated @RequestBody RemoveApplicationScriptBean removeApplicationScriptBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC))) {
			return null;
		}
		validateSaveOrRemoveApplicationFolio(removeApplicationScriptBean);
		applicationService.saveOrRemoveApplicationFolio(removeApplicationScriptBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/saveApplicationQuestioner", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> saveApplicationQuestioner(
			@Validated @RequestBody ApplicationQuestionerBean applicationQuestionerBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC))) {
			return null;
		}
		validateInput(applicationQuestionerBean);
		applicationService.saveApplicationQuestioner(applicationQuestionerBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/getApplicationQuestionerAns/{applicationScriptId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<QuestionerBean> getApplicationQuestionerAns(@PathVariable("applicationScriptId") Long applicationScriptId) {
		UserLoginBean userLoginBean = getUser();
		return applicationService.getApplicationQuestionerAns(applicationScriptId, userLoginBean).getQuestioners();
	}
	
	@RequestMapping(value = "/getApplicationQuestionerAnsWihtScript/{applicationScriptId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationQuestionerBean getApplicationQuestionerAnsWithScript(@PathVariable("applicationScriptId") Long applicationScriptId) {
		UserLoginBean userLoginBean = getUser();
		return applicationService.getApplicationQuestionerAnsWithScript(applicationScriptId, userLoginBean);
	}
	
	@RequestMapping(value = "/saveApplicationFee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> saveApplicationFee(
			@Validated @RequestBody ApplicationFeeBean applicationFeeBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		//TODO current user and application user is same
//		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
//				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC))) {
//			return null;
//		}
		validateInput(applicationFeeBean);
		applicationService.saveApplicationFee(applicationFeeBean, userLoginBean);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/application/fees/byId/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FeeBean> getApplicationFeesById(@PathVariable("id") Long id) {
		UserLoginBean userLoginBean = getUser();
		return applicationService.getApplicationFeesById(id, userLoginBean);
	}
	
	
	
	private void validateInput(ApplicationFeeBean applicationFeeBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(applicationFeeBean.getApplicationId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide application Id.").build());
		}
		
		
		if (ArgumentHelper.isEmpty(applicationFeeBean.getFees())) {
			errorList.add(ValidationError.builder().message("Please provide application fee.").build());
		}
		applicationFeeBean.getFees().forEach(fee->{
			if (!ArgumentHelper.isValid(fee.getFeeFor())) {
				errorList.add(ValidationError.builder().message("Please provide application fee For.").build());
			}
			if (!ArgumentHelper.isValid(fee.getFeeType())) {
				errorList.add(ValidationError.builder().message("Please provide application fee type.").build());
			}
			if (!(AppConstant.FeeType.Percent.name().equals(fee.getFeeType())
					|| AppConstant.FeeType.FixValue.name().equals(fee.getFeeType()))) {
				String message = String.format("Please provide application fee type as either %s or %s",
						AppConstant.FeeType.Percent.name(), AppConstant.FeeType.FixValue.name());
				errorList.add(ValidationError.builder().message(message).build());
			}
			if (!ArgumentHelper.isPositiveWithZero(fee.getFeeValue())) {
				errorList.add(ValidationError.builder()
						.message(String.format("Please provide %s fee value.", fee.getFeeFor())).build());
			}
		});
		
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
		
	}

	private void validateSaveOrRemoveApplicationFolio(RemoveApplicationScriptBean removeApplicationScriptBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(removeApplicationScriptBean.getApplicationId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide application Id.").build());
		}

//		if (!ArgumentHelper.isValid(removeApplicationScriptBean.getFeeType())) {
//			errorList.add(ValidationError.builder().message("Please provide application fee type.").build());
//		}
//		if (!(AppConstant.FeeType.Percent.name().equals(removeApplicationScriptBean.getFeeType())
//				|| AppConstant.FeeType.FixValue.name().equals(removeApplicationScriptBean.getFeeType()))) {
//			String message = String.format("Please provide application fee type as either %s or %s",
//					AppConstant.FeeType.Percent.name(), AppConstant.FeeType.FixValue.name());
//			errorList.add(ValidationError.builder().message(message).build());
//		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
	}

	private void validateUpdateApplicationFee(UpdateApplicationFeeBean applicationFeeBean) {
		List<ValidationError> errorList = new ArrayList<>();
		if (!ArgumentHelper.isValid(applicationFeeBean.getApplicationId() + "")) {
			errorList.add(ValidationError.builder().message("Please provide application Id.").build());
		}

		if (!ArgumentHelper.isValid(applicationFeeBean.getFeeType())) {
			errorList.add(ValidationError.builder().message("Please provide application fee type.").build());
		}
		if (!ArgumentHelper.isPositive(applicationFeeBean.getFeeValue())) {
			errorList.add(ValidationError.builder().message("Please provide application fee value.").build());
		}
		if (!(AppConstant.FeeType.Percent.name().equals(applicationFeeBean.getFeeType())
				|| AppConstant.FeeType.FixValue.name().equals(applicationFeeBean.getFeeType()))) {
			String message = String.format("Please provide application fee type as either %s or %s",
					AppConstant.FeeType.Percent.name(), AppConstant.FeeType.FixValue.name());
			errorList.add(ValidationError.builder().message(message).build());
		}
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
	}

	private void validateInput(ApplicationQuestionerBean applicationQuestionerBean) {
		List<ValidationError> errorList = new ArrayList<>();

		if (ArgumentHelper.isEmpty(applicationQuestionerBean.getQuestioners())) {
			errorList.add(ValidationError.builder().message("Please provide values questions and answers.").build());
		}
		applicationQuestionerBean.getQuestioners().forEach(q -> {
			if (Objects.isNull(q.getId()) || !ArgumentHelper.isPositive(q.getId())) {
				errorList.add(ValidationError.builder().message("Please provide value for questions.").build());
			}
			if (Objects.isNull(q.getAnswer())) {
				errorList.add(ValidationError.builder().message("Please provide value for answer.").build());
			}
		});
		if (ArgumentHelper.isNotEmpty(errorList)) {
			throw new ValidationException(errorList);
		}
	}
	
	@RequestMapping(value = "/sendReminderForPendingApplication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> sendReminderForPendingApplication(
			@Validated @RequestBody ApplicationBean applicationBean, BindingResult binding) {
		if(binding.hasErrors()) {
			throw new ValidationException("Invalid input char used, please correct and try.", ValidationError.builder().build());
	      }
		UserLoginBean userLoginBean = getUser();
		if (!(userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_ADMIN)||
				userLoginBean.getApplicationUserBean().getUserType().equals(AppConstant.USER_TYPE_CC))) {
			return null;
		}
		applicationService.sendReminderForPendingApplication(applicationBean, userLoginBean);
		return ResponseEntity.ok().build();
	}
}
