--liquibase formatted sql
--changeset {authorName}:{id}
insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('901','USER_REGISTRATION_SMS','USER_REGISTRATION_SMS','SMS','Dear  {0}<br>Welcome to  {1}<br>Your pin for login is  {2}.');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('902','USER_LOGIN_PIN_SMS','USER_LOGIN_PIN_SMS','SMS','Dear {0}<br>Your pin for login is {1}.');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('903','VALIDATION_SUBMISSION_SMS','VALIDATION_SUBMISSION_SMS','SMS','Dear {0}<br>Your application has been submitted for validation. Reference number is {1}.');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('904','APPLICATION_APPROVED_SMS','APPLICATION_APPROVED_SMS','SMS','Dear {0}<br>Your application {1} has been validated. Please login to submit the case. Click on {2}');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('905','REMIND_USER_SMS','REMIND_USER_SMS','SMS','Dear {0}<br>Your case {1} is pending submission. Please login to submit the case for processing. {2}');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('906','CASE_CREATED_SMS','CASE_CREATED_SMS','SMS','Dear {0}<br>Your case {1} has been created. Please pay the processing fees and update details. Please login to {2}');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('907','PAYMENT_UPDATED_SMS','PAYMENT_UPDATED_SMS','SMS','Dear {0}<br>Your payment for case {1} has been approved. Please complete your eAdhar verification. Please login at {2}');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('908','PAYMENT_APPROVED_SMS','PAYMENT_APPROVED_SMS','SMS','Dear {0}<br>Your payment for case {1} has been approved. Please complete your eAdhar verification. Please login at {2}');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('909','EADHAR_COMPLETE_SMS','EADHAR_COMPLETE_SMS','SMS','Dear {0}<br>Your case no. {1} is now ready for processing. Please check status on {2}');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('910','EADHAR_COMPLETE_ADMIN_SMS','EADHAR_COMPLETE_ADMIN_SMS','SMS','Dear {0}<br>Your case no. {1} is now ready for eAdhar. Please complete eAdhar');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('911','RTA_LETTER_GENERATED_SMS','RTA_LETTER_GENERATED_SMS','SMS','The RTA letter for case no.{0} has been generated. Please check your email for further actions.');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('912','DOCUMENTS_UPLOADED_SMS','DOCUMENTS_UPLOADED_SMS','SMS','Documents have been uploaded to case  {0}. Please verify.');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('913','ADMIN_APPLICATION_REJECTED_SMS','ADMIN_APPLICATION_REJECTED_SMS','SMS','Dear {0}<br>Your application {1} has been rejected. Please login to check the details. Click {2}');


insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('915','VALIDATION_SUBMISSION_EMAIL_SUB','VALIDATION_SUBMISSION_EMAIL_SUB','EMAIL','Application Validate Submission');
insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('916','VALIDATION_SUBMISSION_EMAIL_BODY','VALIDATION_SUBMISSION_EMAIL_BODY','EMAIL','Dear{0},<BR/><BR/>Your application has been submitted for validation. Reference number is {1}. Please note that you will get a call for verification and validation purpose from the customer care. You will be required to furnish the details of your application. Please note that we do not ask for any confidential information and passwords.
For any assistance please contact customer care at {2}.<br/><br/>Regards,<br/>Team {3}.');

INSERT INTO `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) VALUES('917','APPLICATION_APPROVED_EMAIL_SUB','APPLICATION_APPROVED_EMAIL_SUB','EMAIL','Application approved');
INSERT INTO `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) VALUES('918','APPLICATION_APPROVED_EMAIL_BODY','APPLICATION_APPROVED_EMAIL_BODY','EMAIL','Dear{0},<br/><br/>Your application number {1} has been validated. You are now required to check the details of the application and submit the application for the process.<br/><br/>Pre-Requisites :<br/>1. Documents needed<br/>Adhar Card<br/>Pan Card<br/>Cancelled Cheque<br/>Death Certificate (In case of succession)<br/>2. The Adhar card and pan card should be linked to your mobile number.<br/><br/>Steps to submit :<br/>1. Please login to the website at {2} and navigate to "My applications" section. Please click on the process button to submit the case.<br/><br/>2. Please note you will be required to have the valid Adhar card, PAN card to submit the application. You should have your mobile number connected to your aadhar card and pan card.<br/><br/>3. Please provide necessary information and submit the case.<br/>For any assistance please contact customer care at {3}<br/>Regards<br/>Team {4}.');




