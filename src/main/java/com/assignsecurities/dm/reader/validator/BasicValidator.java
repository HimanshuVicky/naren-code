package com.assignsecurities.dm.reader.validator;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.exception.ValidationException;
import com.assignsecurities.app.util.*;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.*;
import com.assignsecurities.domain.dm.AttributeConfigBean;
import com.assignsecurities.domain.dm.AttributeValidationConfigBean;
import com.assignsecurities.domain.dm.DataColumnModel;





/**
 * 
 *
 */
public class BasicValidator {
	private static final Logger logger = LogManager.getLogger(BasicValidator.class);

	/**
	 * process basic validation based on configuration
	 * 
	 * @param dataColumnModel
	 * @param attributeConfigModel
	 * @param uam
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public boolean processBasicValidation(DataColumnModel dataColumnModel,
										  AttributeConfigBean attributeConfigModel, UserLoginBean uam)
			throws ServiceException {
		boolean isValidColumn = true;
		try {
			if (attributeConfigModel == null) {
//				logger.info( " attributeConfigModel is null for "
//						+ dataColumnModel.getColIndex());
				return isValidColumn;
			}
			if (attributeConfigModel.getHeaderName() == null) {
				return isValidColumn;
			}
			if (attributeConfigModel.isRequired()
					&& dataColumnModel.getColValue() == null) {
				isValidColumn = false;
				setErrorMessage(
						dataColumnModel,
						getErrorMessage(
								MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY,
								uam));
				return false;
			}
			if (dataColumnModel.getColValue() != null) {

				if (attributeConfigModel.isRequired()
						&& dataColumnModel.getColValue().toString().trim()
								.isEmpty()) {
					isValidColumn = false;
					setErrorMessage(
							dataColumnModel,
							getErrorMessage(
									MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY,
									uam));
				}
				if (!isValidDataType(dataColumnModel, attributeConfigModel)) {
					isValidColumn = false;
					setErrorMessage(
							dataColumnModel,
							getErrorMessage(
									MessageConstants.GLOBAL_CONSTRAINT_VALUE_REQUIRED_KEY,
									uam));
				}
				if (dataColumnModel.getColValue().toString().trim().length() > attributeConfigModel
						.getLength() && attributeConfigModel.getLength() > 0) {
					isValidColumn = false;
					setErrorMessage(
							dataColumnModel,
							"Column Value is longer than expected lenght :"+attributeConfigModel
							.getLength());
				}
				try {
					if (isValidForBasicNumValidation(dataColumnModel,
							attributeConfigModel)) {
						if ((StringUtil.isValidString(attributeConfigModel
								.getDataFormatKey()))
								|| (StringUtil
										.isValidString(attributeConfigModel
												.getDelimitedKey()))
								|| !StringUtil
										.isValidString((String) dataColumnModel
												.getColValue())) {
							isValidColumn = true;
							if (attributeConfigModel
									.getValidationConFigModels() != null
									&& !attributeConfigModel
											.getValidationConFigModels()
											.isEmpty()) {
								for (AttributeValidationConfigBean attributeValidationConfigModel : attributeConfigModel
										.getValidationConFigModels()) {
									if (StringUtil
											.isValidString(attributeValidationConfigModel
													.getValidatorClassName())) {
										try {
											Class validatorClass = Class
													.forName(attributeValidationConfigModel
															.getValidatorClassName());
											Object validatorIns = validatorClass
													.newInstance();

											Class[] parameterTypes = {
													Object.class,
													StringBuilder.class,
													UserLoginBean.class,
													AttributeConfigBean.class };
											Method velidateMethod = validatorClass
													.getMethod(
															attributeValidationConfigModel
																	.getValidatorMethodName(),
															parameterTypes);
											StringBuilder errorMessage = new StringBuilder();
											Object[] paramObj = {
													dataColumnModel
															.getColValue(),
													errorMessage, uam,
													attributeConfigModel };
											boolean isValid = (Boolean) velidateMethod
													.invoke(validatorIns,
															paramObj);
											if (!isValid) {
												isValidColumn = false;
												setErrorMessage(
														dataColumnModel,
														errorMessage.toString());
											}
										} catch (Exception e) {
											throw new ServiceException(e);
										}
									}

								}
							}
							return isValidColumn;
						}
						boolean isNumeber = PropertyMapperService
								.getPropertyMapper().isNumeric(
										(String) dataColumnModel.getColValue());
						if (isNumeber) {
							int decimalPlaceAllowed = attributeConfigModel
									.getDecimalPlaces();
							if (decimalPlaceAllowed != -1) {
								String rateStr = ""
										+ dataColumnModel.getColValue();
								if (rateStr.indexOf(".") > 0) {
									rateStr = rateStr.substring(rateStr
											.indexOf(".") + 1);
								} else {
									rateStr = "";
								}
								if (rateStr.indexOf("0") > 0) {
									rateStr = rateStr.substring(0,
											rateStr.indexOf("0"));
								}
								if ((rateStr.length() > 0 && Long
										.parseLong(rateStr) > 0)
										&& rateStr.length() > decimalPlaceAllowed) {
									setErrorMessage(
											dataColumnModel,
											getErrorMessage(
													MessageConstants.DECIMAL_PRECISION_ERR_MSG_KEY,
													uam,
													new Integer(
															decimalPlaceAllowed)));
								}
							}
							Long minValue = attributeConfigModel.getMinValue();
							Long maxValue = attributeConfigModel.getMaxValue();
							Double colValue = Double.parseDouble(String
									.valueOf(dataColumnModel.getColValue()));
							if (minValue != null) {
								if (colValue < minValue) {
									setErrorMessage(
											dataColumnModel,
											getErrorMessage(
													MessageConstants.GLOBAL_CONSTRAINT_MIN_VALUE_KEY,
													uam, minValue));
								}
							}
							if (maxValue != null) {
								if (colValue > maxValue) {
									setErrorMessage(
											dataColumnModel,
											getErrorMessage(
													MessageConstants.GLOBAL_CONSTRAINT_MAX_VALUE_KEY,
													uam, maxValue));
								}
							}
						} else {
							isValidColumn = false;
							setErrorMessage(
									dataColumnModel,
									getErrorMessage(
											MessageConstants.GLOBAL_CONSTRAINT_DOUBLE_KEY,
											uam));
						}

					}
				} catch (ClassNotFoundException e) {
					throw new ServiceException(e);
				}
			}
			if (attributeConfigModel.getValidationConFigModels() != null
					&& !attributeConfigModel.getValidationConFigModels()
							.isEmpty()) {
				for (AttributeValidationConfigBean attributeValidationConfigModel : attributeConfigModel
						.getValidationConFigModels()) {
					if (StringUtil.isValidString(attributeValidationConfigModel
							.getValidatorClassName())) {
						try {
							Class validatorClass = Class
									.forName(attributeValidationConfigModel
											.getValidatorClassName());
							Object validatorIns = validatorClass.newInstance();
							Class[] parameterTypes = { Object.class,
									StringBuilder.class, UserLoginBean.class,
									AttributeConfigBean.class };
							Method velidateMethod = validatorClass.getMethod(
									attributeValidationConfigModel
											.getValidatorMethodName(),
									parameterTypes);
							StringBuilder errorMessage = new StringBuilder();
							Object[] paramObj = {
									dataColumnModel.getColValue(),
									errorMessage, uam, attributeConfigModel };
							boolean isValid = (Boolean) velidateMethod.invoke(
									validatorIns, paramObj);
							if (!isValid) {
								isValidColumn = false;
								setErrorMessage(dataColumnModel,
										errorMessage.toString());
							}
						} catch (Exception e) {
							throw new ServiceException(e);
						}
					}

				}
			}
		} catch (Exception e) {
			 e.printStackTrace();
			isValidColumn = false;
			setErrorMessage(dataColumnModel,
					getErrorMessage(MessageConstants.DATA_VALIDATED_KEY, uam));
		}
		return isValidColumn;

	}

	/**
	 * @param dataColumnModel
	 * @param attributeConfigModel
	 * @return
	 * @throws ClassNotFoundException
	 */
	private boolean isValidForBasicNumValidation(
			DataColumnModel dataColumnModel,
			AttributeConfigBean attributeConfigModel)
			throws ClassNotFoundException {
		return (PropertyMapperService.getPropertyMapper().getWrapperClass(
				attributeConfigModel.getDataType()).getSuperclass()) == Number.class
				&& isResolverDefaultValue(dataColumnModel, attributeConfigModel);
	}

	/**
	 * @param dataColumnModel
	 * @param attributeConfigModel
	 * @return
	 */
	private boolean isResolverDefaultValue(DataColumnModel dataColumnModel,
			AttributeConfigBean attributeConfigModel) {
		return !(attributeConfigModel.getValueBasedResolver() != null
				&& !attributeConfigModel.getValueBasedResolver().isEmpty() && attributeConfigModel
				.getValueBasedResolver().containsKey(
						(String) dataColumnModel.getColValue()));
	}

	/**
	 * 
	 * @param dataColumnModel
	 * @param msg
	 */
	public void setErrorMessage(DataColumnModel dataColumnModel, String msg) {
		if (dataColumnModel.getErrorMessage() == null
				|| dataColumnModel.getErrorMessage().trim().isEmpty()) {
			dataColumnModel.setErrorMessage(msg);
		} else {
			dataColumnModel.setErrorMessage(dataColumnModel.getErrorMessage()
					+ "\n" + msg);
		}
	}

	private boolean isValidDataType(DataColumnModel dataColumnModel,
			AttributeConfigBean attributeConfigModel) {
		boolean isValidData = true;
		try {
			if ((!(StringUtil.isValidString(attributeConfigModel
					.getDataFormatKey()) || StringUtil
					.isValidString(attributeConfigModel.getDelimitedKey())))
					&& StringUtil.isValidString(((String) dataColumnModel
							.getColValue()))
					&& isResolverDefaultValue(dataColumnModel,
							attributeConfigModel)) {
				PropertyMapperService.getPropertyMapper()
						.getNewObjectInstanceWithValue(
								attributeConfigModel.getDataType(),
								(String) dataColumnModel.getColValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValidData = false;
		}
		return isValidData;
	}

	/**
	 * valdateTime HH:MM (4 hour format)
	 * 
	 * @param value
	 * @param uam
	 * @return
	 */
	public boolean valdateTime(Object value, StringBuilder errorMessage,
			UserLoginBean uam, AttributeConfigBean attributeConfigModel)
			throws ServiceException {
		if (value == null || !StringUtil.isValidString(value.toString())) {
			return true;
		}
		boolean valide = true;
		if (value != null && StringUtil.isValidString(String.valueOf(value))) {
			String msg = null;
			msg = getErrorMessage(MessageConstants.TIME_ERROR_MSG_KEY, uam);
			long count = StringUtils.countMatches(String.valueOf(value), ":");
			if (count > 1 || count == 0) {
				valide = false;
				errorMessage.append(msg);
			} else {
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); // HH
																				// =
																				// 24h
																				// format
				dateFormat.setLenient(false); // this will not enable 25:67 for
												// example
				try {
					String date = String.valueOf(value);
					dateFormat.parse(date);
					// Negative Value greater than 0 was throwing Parse
					// Exception
					// But Negative value like "-0:40" was converted to Date
					// like 1970. So to
					// Prevent this checked whether date starts with "-"
					if (date != null && date.startsWith("-")) {
						valide = false;
						errorMessage.append(msg);
					}
				} catch (ParseException e) {
					valide = false;
					errorMessage.append(msg);
				}
			}
		}
		return valide;
	}

	/**
	 * validatePercent >0 and <100
	 * 
	 * @param value
	 * @param uam
	 * @return
	 */
	public boolean validatePercent(Object value, StringBuilder errorMessage,
			UserLoginBean uam, AttributeConfigBean attributeConfigModel)
			throws ServiceException {
		if (value == null || !StringUtil.isValidString(value.toString())) {
			return true;
		}
		String msg = null;
		msg = getErrorMessage(MessageConstants.PERCENT_VALUE_ERROR_MSG_KEY, uam);
		boolean valide = true;
		Double percent = Double.parseDouble(String.valueOf(value));
		if (percent < 0 || percent > 100) {
			valide = false;
			errorMessage.append(msg);
		}
		return valide;
	}

	/**
	 * PositiveInteger >0
	 * 
	 * @param value
	 * @param uam
	 * @return
	 * @throws ServiceException
	 */
	public boolean validateNonZeroPositiveInteger(Object value,
			StringBuilder errorMessage, UserLoginBean uam,
			AttributeConfigBean attributeConfigModel) throws ServiceException {
		if (value == null || !StringUtil.isValidString(value.toString())) {
			return true;
		}

		boolean valide = true;
		Integer num = null;
		try {
			num = Integer.parseInt(value.toString());
			valide = num.longValue() == num.doubleValue()
					&& num.longValue() > 0 ? true : false;
		} catch (Exception pe) {
			logger.error( "Error in parsing int");
			valide = false;
		}

		if (!valide) {
			errorMessage
					.append(getErrorMessage(
							MessageConstants.GLOBAL_CONSTRAINT_POSITIVE_INTEGER_NUMBER_KEY,
							uam));
		}
		return valide;
	}

	/**
	 * PositiveNumber >0
	 * 
	 * @param value
	 * @param uam
	 * @return
	 * @throws ServiceException
	 */
	public boolean validateNonZeroPositiveNumber(Object value,
			StringBuilder errorMessage, UserLoginBean uam,
			AttributeConfigBean attributeConfigModel) throws ServiceException {
		if (value == null || !StringUtil.isValidString(value.toString())) {
			return true;
		}
		boolean valide = true;

		Double num = null;

		try {
			num = Double.parseDouble(String.valueOf(value));
			valide = num.doubleValue() > 0 ? true : false;
		} catch (Exception pe) {
			logger.error( "Error in getting formatter");
			valide = false;
		}
		if (!valide) {
			errorMessage
					.append(getErrorMessage(
							MessageConstants.GLOBAL_CONSTRAINT_NONZERO_POSITIVE_NUMBER_KEY,
							uam));
		}
		return valide;
	}

	/**
	 * PositiveInteger >0
	 * 
	 * @param value
	 * @param uam
	 * @return
	 * @throws ServiceException
	 */
	public boolean validatePositiveInteger(Object value,
			StringBuilder errorMessage, UserLoginBean uam,
			AttributeConfigBean attributeConfigModel) throws ServiceException {
		if (value == null || !StringUtil.isValidString(value.toString())) {
			return true;
		}
		boolean valide = true;
		Integer num = null;
		try {

			num = Integer.parseInt(value.toString());
			valide = ((num.longValue() >= 0) && (num.longValue() == num
					.doubleValue())) ? true : false;
		} catch (Exception pe) {
			logger.error( "Error in getting formatter");
			valide = false;
		}

		if (!valide) {
			errorMessage
					.append(getErrorMessage(
							MessageConstants.GLOBAL_CONSTRAINT_POSITIVE_INTEGER_NUMBER_KEY,
							uam));
		}
		return valide;
	}

	/**
	 * PositiveNumber >0
	 * 
	 * @param value
	 * @param uam
	 * @return
	 * @throws ServiceException
	 */
	public boolean validatePositiveNumber(Object value,
			StringBuilder errorMessage, UserLoginBean uam,
			AttributeConfigBean attributeConfigModel) throws ServiceException {
		if (value == null || !StringUtil.isValidString(value.toString())) {
			return true;
		}
		boolean valide = true;

		Double num = null;
		try {
			num = Double.parseDouble(String.valueOf(value));
			valide = num.doubleValue() >= 0 ? true : false;
		} catch (Exception pe) {
			logger.error( "Error in getting formatter");
			valide = false;
		}

		if (!valide) {
			errorMessage
					.append(getErrorMessage(
							MessageConstants.GLOBAL_CONSTRAINT_POSITIVE_NUMBER_KEY,
							uam));
		}
		return valide;

	}

	public boolean valdateUserCode(Object value, StringBuilder errorMessage,
			UserLoginBean uam, AttributeConfigBean attributeConfigModel)
			throws ServiceException {
		return true;
	}

	/**
	 * valdateDate format DD-MMM-YYYY
	 * 
	 * @param value
	 * @param uam
	 * @return
	 */
	public boolean valdateDate(Object value, StringBuilder errorMessage,
			UserLoginBean uam, AttributeConfigBean attributeConfigModel)
			throws ServiceException {

		// resolver not returning "01-Jan-1900" #Default
		if ((value.toString()).equalsIgnoreCase("#Default")
				|| (value.toString()).equalsIgnoreCase("01-Jan-1900")) {
			return true;
		}

		if (value == null || !StringUtil.isValidString(value.toString())) {
			return true;
		}
		String msg = null;
		boolean valide = true;
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			msg = getErrorMessage(MessageConstants.DATE_ERROR_MSG_KEY, uam);
			date = dateFormat.parse(String.valueOf(value));
			String dateStr = dateFormat.format(date);
			if (dateStr.equals(value.toString())) {
				valide = true;
			} else {
				valide = false;
				errorMessage.append(msg);
			}
		} catch (ParseException e) {
			valide = false;
			errorMessage.append(msg);
		}

		return valide;
	}

	/**
	 * valdateDate format DD-MMM-YYYY
	 * 
	 * @param value
	 * @param uam
	 * @return
	 */
	public boolean valdateFetureDate(Object value, StringBuilder errorMessage,
			UserLoginBean uam, AttributeConfigBean attributeConfigModel)
			throws ServiceException {
		if (value == null || !StringUtil.isValidString(value.toString())) {
			return true;
		}
		boolean valid = true;
		String msg = null;
		String futureDateMsg = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date selectedDateValue = null;
		try {
			msg = getErrorMessage(MessageConstants.DATE_ERROR_MSG_KEY, uam);
			futureDateMsg = getErrorMessage(
					MessageConstants.FUTURE_DATE_MSG_KEY, uam);
			selectedDateValue = dateFormat.parse(String.valueOf(value));
			String dateStr = dateFormat.format(selectedDateValue);
			if (dateStr.equals(value.toString())) {
				valid = true;
			} else {
				valid = false;
				errorMessage.append(msg);
			}
		} catch (ParseException e) {
			valid = false;
			errorMessage.append(msg);
		}
		if (selectedDateValue != null) {
			if (!DateUtil.isAfterIgnoreTime(DateUtil.getTodaysDate(false),
					selectedDateValue)) {
				valid = false;
				errorMessage.append(futureDateMsg);
			}
		}
		return valid;
	}

	/**
	 * 
	 * @param value
	 * @param errorMessage
	 * @param uam
	 * @param attributeConfigModel
	 * @return
	 */
	public boolean validateAlphaNumericString(Object value,
			StringBuilder errorMessage, UserLoginBean uam,
			AttributeConfigBean attributeConfigModel) throws ServiceException {
		if (value == null || !StringUtil.isValidString(value.toString())) {
			return true;
		}
		boolean isValid = false;
		try {
			AlphaNumericConstraint alphaNumericConstraint = AlphaNumericConstraint
					.getInstance();
			isValid = alphaNumericConstraint.validate(String.valueOf(value));
			if (!isValid) {
				errorMessage.append(getErrorMessage(
						MessageConstants.GLOBAL_CONSTRAINT_ALPHANUMERIC_KEY,
						uam));
			}
		} catch (ValidationException e) {
			isValid = false;
			errorMessage.append(getErrorMessage(
					MessageConstants.GLOBAL_CONSTRAINT_ALPHANUMERIC_KEY, uam));
		}
		return isValid;
	}

	/**
	 * This method is used to validate user entered expense amount for precesion
	 * and positive number
	 * 
	 * @param value
	 * @param errorMessage
	 * @param uam
	 * @param attributeConfigModel
	 * @return
	 * @throws ServiceException
	 */
	public boolean validateUserEnteredAmount(Object value,
			StringBuilder errorMessage, UserLoginBean uam,
			AttributeConfigBean attributeConfigModel) throws ServiceException {
		boolean isValid = true;
		if (value != null && StringUtil.isValidString(value.toString())) {
			int decimalPlaceAllowed = 2;

			String userEnteredAmt = value.toString();
			isValid = validatePositiveNumber(value, errorMessage, uam,
					attributeConfigModel);
			if (isValid) {

				if (userEnteredAmt.indexOf(".") > 0) {
					userEnteredAmt = userEnteredAmt.substring(userEnteredAmt
							.indexOf(".") + 1);
				} else {
					userEnteredAmt = "";
				}
				if (userEnteredAmt.indexOf("0") > 0) {
					userEnteredAmt = userEnteredAmt.substring(0,
							userEnteredAmt.indexOf("0"));
				}
				if ((userEnteredAmt.length() > 0 && Long
						.parseLong(userEnteredAmt) > 0)
						&& userEnteredAmt.length() > decimalPlaceAllowed) {
					errorMessage.append(getErrorMessage(
							MessageConstants.DECIMAL_PRECISION_ERR_MSG_KEY,
							uam, new Integer(decimalPlaceAllowed)));
					isValid = false;
				}
			}
		}
		return isValid;
	}

	public boolean validateLoginId(Object value, StringBuilder errorMessage,
			UserLoginBean uam, AttributeConfigBean attributeConfigModel)
			throws ServiceException {
		String loginId = value.toString();
		boolean isValid = true;
		if (loginId != null && !loginId.equals("")) {
			for (int i = 0; i < loginId.length(); i++) {
				char ch = loginId.charAt(i);

				if ((Character.isLetter(ch) || Character.isDigit(ch)
						|| (ch == ' ') || (ch == '@') || (ch == '.') || (ch == '_'))) {
					continue;
				} else {
					isValid = false;
					String LoginIderrorMessage = getErrorMessage(
							MessageConstants.LOGIN_USERNAME_VALIDATION_KEY,
							uam, ": \'@\', \'.\', and \'_\'.");
					errorMessage.append(LoginIderrorMessage);
					break;
				}
			}
		}
		return isValid;

	}

	public boolean validatePhone(Object value, StringBuilder errorMessage,
			UserLoginBean uam, AttributeConfigBean attributeConfigModel)
			throws ServiceException {
		String phone = value.toString();
		boolean isValid = true;
		if (phone != null && !phone.equals("")) {
			Constraint ct = new MaxLengthConstraint(
					DMConstants.PHONE_FIELD_LENGTH);
			try {

				if (!phone.equals("") && !ct.validate(phone)) {
					errorMessage
							.append(getErrorMessage(
									MessageConstants.GLOBAL_CONSTRAINT_EXCEED_STORAGE_LIMIT_KEY,
									uam));
					isValid = false;
				}

			} catch (ValidationException e) {
				throw new ServiceException(
						"ValidationException while validating Phone : " + phone,
						e);
			}
		}
		return isValid;
	}

	public boolean validateEmail(Object value, StringBuilder errorMessage,
			UserLoginBean uam, AttributeConfigBean attributeConfigModel)
			throws ServiceException {
		String email = value.toString();
		boolean isValid = true;
		if (email != null && !email.equals("")) {
			StringTokenizer stEmail = new StringTokenizer(email, ",");
			Constraint ct = EmailAddressConstraint.getInstance();
			try {
				while (stEmail.hasMoreTokens()) {
					String emailID = stEmail.nextToken().trim();
					if (!emailID.equals("") && !ct.validate(emailID)) {
						errorMessage.append(getErrorMessage(
								MessageConstants.GLOBAL_CONSTRAINT_EMAIL_KEY,
								uam));
						isValid = false;
					}
				}
			} catch (ValidationException e) {
				throw new ServiceException(
						"ValidationException while validating E-Mail : "
								+ email, e);
			}
		}
		return isValid;
	}

	public static String getErrorMessage(
			String globalConstraintValueRequiredKey, UserLoginBean uam)
			throws ServiceException {
		try {
			String messageStting = ResourceBundleFactory.getInstance(
					uam.getLocale())
					.getString(globalConstraintValueRequiredKey);
			if (messageStting != null) {
				return messageStting;
			}
		} catch (Exception e) {
			return '!' + globalConstraintValueRequiredKey + '!';
		}
		return globalConstraintValueRequiredKey;

	}

	/**
	 * 
	 * @param globalConstraintValueRequiredKey
	 * @param uam
	 * @return
	 * @throws ServiceException
	 */
	public static String getErrorMessage(
			String globalConstraintValueRequiredKey, UserLoginBean uam,
			Object... params) throws ServiceException {
		try {
			String messageStting = ResourceBundleFactory.getInstance(
					uam.getLocale())
					.getString(globalConstraintValueRequiredKey);
			if (messageStting != null) {
				messageStting = MessageFormat.format(messageStting, params);
				return messageStting;
			}
		} catch (MissingResourceException e) {
			return '!' + globalConstraintValueRequiredKey + '!';
		}
		return globalConstraintValueRequiredKey;

	}

}
