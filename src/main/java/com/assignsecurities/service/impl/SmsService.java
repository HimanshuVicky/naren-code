package com.assignsecurities.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.dm.PropertyKeys;


@Service("smsService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class SmsService {
	private static final Logger LOG = LogManager.getLogger(SmsService.class);

	public String sendSms(String message, String numbers) {
		try {
			
			
			String smsServiceOnOff =ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMS_SERVICE_ON_OFF); // "ADHYTA";
			if (smsServiceOnOff.equalsIgnoreCase("OFF") ) {
				return "NoSMS";
			}
			
			// Construct data
		//	String apiKeyValue = ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMS_API_KEY);
			String apiKeyValue ="MjM0OWUyODVmMzZkYjVmOGYwMjI2M2Y3ZGE2NjgxMTA=";
			if (Objects.isNull(apiKeyValue)) {
				apiKeyValue = "MjM0OWUyODVmMzZkYjVmOGYwMjI2M2Y3ZGE2NjgxMTA=";//"qaBrKdSwW64-yHFLHJn25L9OYmwnFmgPMnqURHNu5E";
			}
			
			String testNumbers="917709599882";
			if(testNumbers.length()>numbers.length() && !numbers.startsWith("91")) {
				numbers="91"+numbers;
			}
			String apiKey = "apikey=" + apiKeyValue;			// String message = "&message=" + "This is your message";
			// String sender = "&sender=" + "ENRLME";
			// String numbers = "&numbers=" + "918123456789";
			String sendSMSURL = "https://api.textlocal.in/send/?";//ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMS_API_URL);
			if (Objects.isNull(sendSMSURL)) {
				sendSMSURL = "https://api.textlocal.in/send/?";//"https://api.textlocal.in/send/?";
			}
			String sender =ApplicationPropertiesService.getPropertyStringValue(PropertyKeys.SMS_API_SENDER); // "ADHYTA";
			if (Objects.isNull(sender)) {
				sender = "ADHYTA";
			}
			sender = "ADHYTA";//"ENRLME";
			// Send data
			LOG.info("Sending SMS------------------------------------------------------"+numbers);
			HttpURLConnection conn = (HttpURLConnection) new URL(sendSMSURL).openConnection();
			String data = apiKey +"&numbers=" + numbers + "&message=" + message + "&sender="+sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
//			LOG.info("data:"+data);
			LOG.info("Response:"+stringBuffer);
//			System.out.println("Response:"+stringBuffer);
			return stringBuffer.toString();
		} catch (Exception e) {
//			System.out.println("Error SMS " + e);
			LOG.error("Error While Sending SMS ", e);
			return "Error " + e;
		}
	}
	
	public static void main(String[] args) {
		SmsService sendSms = new SmsService();
		//REGISTER
		String eMailBody="";
//		eMailBody="Dear User, %nThanks for Registration. %nYour PIN for login is {0}.%nPIN has also been sent to your registered email address.";
//		eMailBody = MessageFormat.format(eMailBody, "Anvika");//Candidate Name

		//Pending Fee
//		eMailBody="Dear {0}, %n%nYour fees for the {1} is pending. Please pay at the earliest. Please ignore if already paid.";
//		eMailBody = MessageFormat.format(eMailBody, "Narendra","Narendra Classes");//Candidate Name and Branch Name
//		
		//Application Fee Payment
//		eMailBody="Dear {0}%n%nYou have successfully made the payment for your application. Please visit \"My Payments\" section for the payment receipt. %n%nEnrol Me Team";
//		eMailBody = MessageFormat.format(eMailBody, "Anvika");//Candidate Name 
		
		//Application Submitted
//		eMailBody="Dear {0}%n%nYour application has been submitted via Enrol Me. You will be informed about the status changes.%n%nEnrol Me Team";
////		eMailBody = AppConstant.APP_SUBMIT_EMAIL_BODY;
//		eMailBody = MessageFormat.format(eMailBody, "Anvika");//Candidate Name 
//		
		//Program Fee Payment
//		eMailBody="Dear {0}, %n%nYou have successfully made a fee payment to {1} for your program.%n%nEnrol Me Team";
//		eMailBody = MessageFormat.format(eMailBody, "Anvika","Narendra Classes");//Candidate Name and Branch Name
		
		//Application Selected
//		eMailBody="Dear {0}, %n%nYour application has been selected. Please visit \"My Applications\" section for further action.%n%nEnrol Me Team";
//		eMailBody = MessageFormat.format(eMailBody, "Anvika");//Candidate Name
		
		//Application Rejected
//		eMailBody="Dear {0},%n%nYour application has been rejected. Please select the \"My Applications\" section for further action. %n%nEnrol Me Team";
//		eMailBody = MessageFormat.format(eMailBody, "Anvika");//Candidate Name
		
		

		//OnBoard
//		eMailBody="Dear {0}, %n%nYour admission in {1}, is successful. Please visit \"My Admissions\" section for any further action.%n%nEnrol Me Team";
//		eMailBody = MessageFormat.format(eMailBody, "Anvika","Narendra Classes");//Candidate Name and Branch Name
		
		//Reset Pin
//		eMailBody="Dear {0}, %nYour Pin Reset is successful. Your new PIN for login is {1}.PIN has also been sent to your registered email. http:// www.enrol-me.com";
//		eMailBody=AppConstant.APP_RESET_SMS_TEXT;
//		eMailBody = MessageFormat.format(eMailBody, "Anvika","1234");//Candidate Name and Branch Name
		
//		eMailBody = AppConstant.APP_REJECT_EMAIL_BODY;
//		eMailBody = "Dear {0}%n%nYour application has been submitted via Enrol Me.You will be informed about the status changes.%n%nEnrol Me Team";
//		eMailBody = "Dear User, %nThanks for Registration. %nYour PIN for login is {0}.%nPIN has also been sent to your registered email address.";
//		eMailBody = MessageFormat.format(eMailBody, "1212");//Candidate Name 
		
		//eMailBody="Dear {0},%n %n Your admission in {1}, is successful. Please visit \"My Admissions\" section for any further action.%n %nEnrol Me Team";
//		eMailBody = MessageFormat.format(eMailBody, "Narendra Classes");//Candidate Name
		
//		Admission Successful 
		
		//eMailBody=AppConstant.APP_ON_BOARD_EMAIL_BODY;
	 	
//		Pending Fee
		
		//eMailBody="Dear {0},%n %nYour fees for the {1} is pending. Please pay at the earliest. Please ignore if already paid.";
		
//		Application Fee Payment 
	//	eMailBody=AppConstant.APP_MONTHLY_FEE_EMAIL_BODY;
		
		
//		Application Submitted 
	//	eMailBody=AppConstant.APP_FEE_UPDATE_EMAIL_BODY;
//		eMailBody="Dear {0}%n %nYour revised fee for program {1} is Admission fee {2}/-, Program fee {3}/-.";
//		eMailBody="Dear {0}%n %nYour application has been submitted via Enrol Me. You will be informed about the status changes.%n %nEnrol Me Team";
	 	
//		Program Fee Payment 
//		eMailBody=AppConstant.APP_MONTHLY_FEE_EMAIL_BODY;
		
	 	
//		Application Selected 
//		eMailBody=AppConstant.APP_SELECTED_EMAIL_BODY;
		
		
//		Application Rejected 
//		eMailBody=AppConstant.APP_REJECT_EMAIL_BODY;
		
		
//		eMailBody="Dear {0},%n %nYour fees for the {1} is pending. Please pay at the earliest. Please ignore if already paid.";
//		eMailBody = MessageFormat.format(eMailBody, "Kamal Chatopadhya","Craft Tuition Afternoon","300","6000");//Candidate Name
//		eMailBody="Dear {0}%n %nYour revised Admission and Program fee for program {1} is Admision fee {2}/-, and Program fee {2}/- respectively.";
		//eMailBody = MessageFormat.format(eMailBody, "Kamal Chatopadhya","Craft Tuition Afternoon","300","6000");//Candidate Name
		
		
//		sendSms.sendSms(eMailBody, "9890960765");
		//sendSms.sendSms(eMailBody, "8788339683");
		
//		Dear {0}, %n%nKindly note, your program {1} has been suspended for today.%n%nTeam {2}
		//Dear KartikTest, %n%nKindly note, your program TestProgram has been suspended for today.%n%nTeam TestBranch
//		Dear {0}, %n%nKindly note, your program {1} has been suspended for tomorrow.%n%nTeam {2}
//		Dear {0}, %n%nKindly note, your program {1} has been suspended for the period from {2} to {3}.%n%nTeam {4}
//		
//		eMailBody="Dear Ram, \r\n" + 
//				"Your Pin Reset is successful. Your new PIN for login is 2134.PIN has also been sent to your registered email. http:// www.enrol-me.com";
//123 eMailBody="Dear {0} \r\nWelcome to {1}\r\nYour pin for login is {2}.";	
//eMailBody=AppConstant.APP_RESET_SMS_TEXT;//"Dear {0}\r\nWelcome to {1}\r\nYour pin for login is {2}.";
//	 MessageFormat.format(eMailBody, "KartikTest","testabcd","1234");	
//eMailBody="Dear {0}\r\nYour pin for login is {1}.";	
//eMailBody = MessageFormat.format(eMailBody, "KartikTest","1234");		
//eMailBody="Dear {0}<br>Your application has been submitted for validation. Reference number is {1}.";
//eMailBody="Dear {0}\r\nYour application has been submitted for validation. Reference number is {1}.";
//eMailBody="Dear {0}\r\nYour payment for case {1} has been approved. Please complete your eAdhar verification. Please login at {2}";
//		eMailBody="Dear {0}\r\nYour case no. {1} is now ready for processing. Please check status on {2}";
// eMailBodyAdmin="Dear {0}\r\nYour case no. {1} is now ready for eAdhar. Please complete eAdhar";   	
//eMailBody="The RTA letter for case no. {0} has been generated. Please check your email for further actions."; 
		//eMailBody="Documents have been uploaded to case {0}. Please verify."; 
		//eMailBody="Dear {0}\r\nYour case {1} is pending submission. Please login to submit the case for processing. {2}";
		//eMailBody="Dear {0}\r\nYour application {1} has been validated. Please login to submit the case. Click on {2}";
		//eMailBody="Dear {0}\r\nYour case {1} has been created. Please pay the processing fees and update details. Please login to {2}";
		//eMailBody="Dear {0}\r\nThe payment has been updated for case {1}. Please approve the payment.";
//		eMailBody="Dear {0}\n\nWelcome to {1}\n\nYour pin for login is {2}.";
		
		//eMailBody="Dear %%|username^{"inputtype" : "text", "maxlength" : "30"}%%
//Your payment for case %%|casenumber^{"inputtype" : "text", "maxlength" : "15"}%% has been approved. Please complete your eAdhar verification. Please login at %%|website^{"inputtype" : "text", "maxlength" : "30"}%%"
		//eMailBody = MessageFormat.format(eMailBody, "KartikTest","http://www.findmymoney.in","1234");//Candidate Name
		
		//eMailBody="Dear {0}\n\nYour pin for login is {1}.";
		
		
		//eMailBody = MessageFormat.format(eMailBody, "KartikTest","1234");
		//sendSms.sendSms(eMailBody, "9921399990");
//	sucess--	eMailBody="The RTA response has been upload for case {0}. Please verify.\r\nADHYATA IMF PVT. LTD";
//		eMailBody = MessageFormat.format(eMailBody, "1234");
//		sucess-		eMailBody="Dear {0}\r\nYour application {1} has been rejected. Please login to submit the case. Click on {2}";
//		sucess-		eMailBody = MessageFormat.format(eMailBody, "kartik","1234","www.findmymoney.com");
		ApplicationPropertiesService.addProperty(PropertyKeys.SMS_SERVICE_ON_OFF, "ON");
//		eMailBody="Dear {0}\r\nYou have been created as a Referral Partner/Franchise for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nAIPL";
				/*
				 * AppConstant.REFERAL_FRANCHISE_USER_ESIGNED_COMPLETED_SMS;
				 */
		//eMailBody="Dear {0}\r\nReferral Partner/Franchise has signed his agreement. Please sign the agreement.\r\nAIPL";
//		eMailBody = MessageFormat.format(eMailBody, "KartikTest");
		//		eMailBody = MessageFormat.format(eMailBody, "KartikTest","1234");
		
		//eMailBody="Dear {0}\r\nYour Referral Partner/Franchise agreement for Findmymoney has been signed. System is now available to you. Your PIN is :{1}\r\nAIPL";
		//eMailBody = MessageFormat.format(eMailBody, "KartikTest","5252");
	
		
		//partner 1 - Done
		//eMailBody="Dear {0}\r\nYou have been created as a Partner for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT LTD";
		//eMailBody = MessageFormat.format(eMailBody, "KartikTest","5252");
//		sendSms.sendSms(eMailBody, "9921399990");
		
		//RP partners 2 - Done
		//eMailBody="Dear {0}\r\nYou have been created as a Referral Partner for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT LTD";
		//eMailBody = MessageFormat.format(eMailBody, "KartikTest","5252");
		
		//Franchise 3 -done
//		eMailBody="Dear {0}\r\nYou have been created as a Franchise for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT LTD";
//		eMailBody = MessageFormat.format(eMailBody, "KartikTest","5252");
//		sendSms.sendSms(eMailBody, "9921399990");
		
		//NP 4 done
//		eMailBody="Dear {0}\r\nYou have been created as a Notary Partner for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT LTD";
//		eMailBody = MessageFormat.format(eMailBody, "KartikTest","5252");
		//CA 5 done
		//eMailBody="Dear {0}\r\nYou have been created as a Chartered Accountant for Findmymoney. Please login using your phone number and complete your agreement. Your PIN is :{1}\r\nADHYATA IMF PVT. LTD";
		//eMailBody = MessageFormat.format(eMailBody, "KartikTest","5252");
		//Admin Franchise signed 6 done -- NOT INCLIDED IN APP CONSTANT 
//		eMailBody="Dear {0}\r\nYour Franchise agreement for Findmymoney has been signed. System is now available to you. Your PIN is :{1}\r\nAIPL";
//		eMailBody = MessageFormat.format(eMailBody, "KartikTest","5252");
		//Admin RP Signed 7 - not working -- NOT INCLIDED IN APP CONSTANT 
		//eMailBody="Dear {0}\r\nYour  Referral Partner agreement for Findmymoney has been signed. System is now available to you. Your PIN is :{1}\r\nAIPL";
//		eMailBody="Dear {0}\r\nYour Referral Partner agreement for Findmymoney has been signed. System is now available to you. Your PIN is :{1}\r\nAIPL";
//		eMailBody = MessageFormat.format(eMailBody, "KartikTest","5252");
		
		//Franchise sign agreement 8 done
//		eMailBody="Dear {0}\r\nFranchise has signed his agreement. Please sign the agreement.\r\nAIPL";
//		eMailBody= MessageFormat.format(eMailBody, "KartikTest");
		//RP Signed Agreemnt 9 done
//		eMailBody="Dear {0}\r\nReferral Partner has signed his agreement. Please sign the agreement.\r\nAIPL";
//		eMailBody= MessageFormat.format(eMailBody, "KartikTest","5252");
		
		
		sendSms.sendSms(eMailBody, "9921399990");
		System.out.println("SMS Sent");
	}
}
