--liquibase formatted sql
--changeset {authorName}:{id}

/*Data for the table `message_template` */

insert  into `message_template`(`ID`,`code`,`NAME`,`TYPE`,`MESSAGE`) values (883,'USER_REG_SMS','USER_REG_SMS','SMS','Dear User, %nThanks for Registration. %nYour PIN for login is {0}.%nPIN has also been sent to your registered email address.'),(884,'USER_REG_EMAIL_SUB','USER_REG_EMAIL_SUB','EMAIL','Enrol-Me Registration'),(885,'USER_REG_EMAIL_BODY','USER_REG_EMAIL_BODY','EMAIL','Dear User,<br/>&nbsp;&nbsp;Thanks for Registration.<br/>&nbsp;&nbsp;Your PIN for login is {1}. PIN has also been sent to your registered mobile number.<br/>&nbsp;&nbsp;Team Enrol-Me'),(886,'MONTHLY_REMINDER_SMS_TEXT','MONTHLY_REMINDER_SMS_TEXT','SMS','Dear {0},%n %nYour fees for the {1} is pending. Please pay at the earliest. Please ignore if already paid.'),(887,'USER_SUCCESS_REG_SMS','USER_SUCCESS_REG_SMS','SMS','Dear User, %nThanks for Registration. %nYour User ID for login is {0}.%nLogin PIN will be sent to your registered phone and email address.'),(888,'USER_SUCCESS_REG_EMAIL_SUB','USER_SUCCESS_REG_EMAIL_SUB','EMAIL','Enrol-Me Registration'),(889,'USER_SUCCESS_REG_EMAIL_BODY','USER_SUCCESS_REG_EMAIL_BODY','EMAIL','Dear User, %nThanks for Registration.%nYour User ID for login is {0}.%nLogin PIN will be sent to your registered phone and email address.'),(890,'USER_SUCCESS_REFERRAL_REG_SMS','USER_SUCCESS_REFERRAL_REG_SMS','SMS','Dear Referrer {0},%nThanks for joining Enrol-Me referral program for future royalty income.%nYour referral code is {1}.'),(891,'USER_SUCCESS_REFERRAL_REG_EMAIL_SUB','USER_SUCCESS_REFERRAL_REG_EMAIL_SUB','EMAIL','Enrol-Me Referral Program');

/*Data for the table `state` */

insert  into `state`(`ID`,`NAME`,`CODE`) values (0000000005,'Andaman and Nicobar Islands','Andaman and Nicobar Islands'),(0000000006,'Andhra Pradesh','Andhra Pradesh'),(0000000007,'Arunachal Pradesh','Arunachal Pradesh'),(0000000008,'Assam','Assam'),(0000000009,'Bihar','Bihar'),(0000000010,'Chandigarh','Chandigarh'),(0000000011,'Chhattisgarh','Chhattisgarh'),(0000000012,'Dadra and Nagar Haveli','Dadra and Nagar Haveli'),(0000000013,'Daman and Diu','Daman and Diu'),(0000000014,'Delhi','Delhi'),(0000000015,'Goa','Goa'),(0000000016,'Gujarat','Gujarat'),(0000000017,'Haryana','Haryana'),(0000000018,'Himachal Pradesh','Himachal Pradesh'),(0000000019,'Jammu and Kashmir','Jammu and Kashmir'),(0000000020,'Jharkhand','Jharkhand'),(0000000021,'Karnataka','Karnataka'),(0000000022,'Kerala','Kerala'),(0000000023,'Lakshadweep','Lakshadweep'),(0000000024,'Madhya Pradesh','Madhya Pradesh'),(0000000025,'Maharashtra','Maharashtra'),(0000000026,'Manipur','Manipur'),(0000000027,'Meghalaya','Meghalaya'),(0000000028,'Mizoram','Mizoram'),(0000000029,'Nagaland','Nagaland'),(0000000030,'Orissa','Orissa'),(0000000031,'Pondicherry','Pondicherry'),(0000000032,'Punjab','Punjab'),(0000000033,'Rajasthan','Rajasthan'),(0000000034,'Sikkim','Sikkim'),(0000000035,'Tamil Nadu','Tamil Nadu'),(0000000036,'Tripura','Tripura'),(0000000037,'Uttaranchal','Uttaranchal'),(0000000038,'Uttar Pradesh','Uttar Pradesh'),(0000000039,'West Bengal','West Bengal');

/*Data for the table `typeofusers` */

insert  into `typeofusers`(`NAME`)  values('Admin'),('Franchise'),('User'),('Advocate');

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('2','MAIL_HOST','AddSome','Email Host','None','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('3','ALTERNATE_MAIL_HOST','AddSome','ALTERNATE Email Host','','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('4','USE_ALTERNATE_HEADERS','true','ALTERNATE_HEADERS','false','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('5','SEND_MAIL_AS','AddSome','Email Host','imfadhyata@gmail.com','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('6','MAIL_CONNECTION_TIMEOUT','60000','Email Host','60000','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('7','MAIL_SOCKET_IO_TIMEOUT','60000','Email Host','60000','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('8','MAIL_SMTP_PORT','587','Email Host','25','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('9','MAIL_SMTP_STARTTLS_ENABLE','true','Email Host','false','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('10','MAIL_SMTP_AUTH','true','MAIL_SMTP_AUTH','false','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('11','SMTP_AUTH_USER','AddSome','SMTP_AUTH_USER','','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('12','SMTP_AUTH_PWD','AddSome','SMTP_AUTH_PWD','','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('13','SMS_API_KEY','AddSome','API Key to send SNS','TODO','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('14','SMS_API_URL','AddSome','URL to send SNS','https://api.textlocal.in/send/?','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('15','SMS_API_SENDER','AddSome','URL to send SNS','MyApplcationTeam','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('16','BHAV_COPY_ROOT','AddSome','BHAV_COPY_ROOT','E:/ShareProject/','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('17','BHAV_BSE_URL','http://www.bseindia.com/download/BhavCopy/Equity/EQ_ISINCODE_$DD$$MM$$YY$.zip','BHAV_BSE_URL','','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('18','BHAV_NSE_URL','https://www1.nseindia.com/content/historical/EQUITIES/$YYYY$/$MMM$/cm$DD$$MMM$$YYYY$bhav.csv.zip','BHAV_NSE_URL','','1','Sysadmin',now());

--S3 bucket Access Key ID and Secret Access Key
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('19','AWS_S3_ACCESSKEY','','AWS_S3_ACCESSKEY','','1','Sysadmin',now());
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`) values('20','AWS_S3_SECRETKEY','','AWS_S3_SECRETKEY','','1','Sysadmin',now());



insert into address (Id,Address,City,PinCode,State)
values (1,'Sr. No. 78/60, Plot No. 1, Sai Vision, A, Flat N Kunal Icon Rd, Pimple Saudagar','Pune','411027','Maharastra');

insert into franchise (Id,Name,AddressId,ContactNumber,OwnerFirstName,OwnerLastName) 
values (1,'Adhyata IMF Pvt. Ltd., India',1,'','Niranjan Ramanand ','Mantri');

insert into applicationuser (Id,FirstName,MiddleName,LastName,UserType,EmailId,DateOfBirth,AlternateMobileNo,AddressId,FranchiseId)
values(1,'Admin','','','Admin','imfadhyata@gmail.com',null,null,1,1);

insert into userlogin (Id,MobileNo,Pin,ApplicationUserId,IsActive)
values(1,'System','0000',1,1);

insert into `dm_obj_template` (`ID`, `LOCALE_CODE`, `OBJ_ID`, `TEMPLATE_BYTE`) values('1','en_US','1','PK');

insert into `dm_obj_def` (`ID`, `CODE`, `MAX_PARENT_ROWS`, `NAME`, `OBJECT_PER_FILE`, `STATUS_ID`, `IS_MAIN_OBJECT_YN`, `DATE_CREATED`, `DATE_MODIFIED`, `CREATED_BY`, `MODIFIED_BY`, `OBJ_ID`) 
values('1','SCRIPT','0','Script','0','90796','0',now(),now(),'System','System','1');


insert into `dm_obj_conf` (`ID`, `BO_MODEL_NAME`, `BUS_DATA_PROC_CLASS`, `BUS_DATA_VALIDATOR_CLASS`, `DL_DATA_PROC_CLASS`, `DATA_START_ROW_NO`, `ERROR_INDEX_NO`, `HEADER_ROW_INDEX`, `INPUT_FILE_FORMAT`, `IS_ACTION_COLUMN_REQUIRED`, `IS_REQUIRED`, `OBJECT_ASSEMBLER_CLASS`, `DIS_ASSEMBLER_CLASS_NAME`, `OBJECT_CODE`, `OBJECT_TYPE`, `PARENT_REF_KEY_COL_IDX`, `PRIMARY_KEY_COL_IDX`, `SEQ_NO`, `TAB_OR_FILE_NAME`)
values('1','com.assignsecurities.bean.ScriptBean','com.assignsecurities.service.impl.ScriptDataProcessor','com.assignsecurities.service.impl.ScriptDataValidator','com.assignsecurities.service.impl.ScriptDataDLProcessor','5','17','4','xlsx','1','1','com.assignsecurities.service.impl.ScriptDataAssembler','com.assignsecurities.service.impl.ScriptDataAssembler','SCRIPT',NULL,'1','1','2','scripts#');


insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
values('1',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Action','0','1',NULL,NULL,'32',NULL,NULL,'dlAction','1','Add','0','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
values('2',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Security Code','1','1',NULL,NULL,'255',NULL,NULL,'securityCode','1','500325','1','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('3',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Company Name','1','1',NULL,NULL,'255',NULL,NULL,'companyName','1','RELIANCE INDUSTRIES LTD.','2','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('4',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Investor Name','1','1',NULL,NULL,'255',NULL,NULL,'investorName','1','OM PRAKASH GUPTA','3','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('5',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Father Name','1','0',NULL,NULL,'255',NULL,NULL,'fatherName','1','NA','4','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('6',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Address','1','1',NULL,NULL,'400',NULL,NULL,'address','1','address','5','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('7',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Country','1','1',NULL,NULL,'255',NULL,NULL,'country','1','India','6','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('8',NULL,NULL,NULL,'java.lang.String','-1',NULL,'State','1','1',NULL,NULL,'255',NULL,NULL,'state','1','Maharastra','7','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('9',NULL,NULL,NULL,'java.lang.String','-1',NULL,'City','1','1',NULL,NULL,'255',NULL,NULL,'city','1','Pune','8','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('10',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Pincode','1','1',NULL,NULL,'10',NULL,NULL,'pincode','1','411003','9','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('11',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Folio Number','1','0',NULL,NULL,'100',NULL,NULL,'folioNumber','1','132069304','10','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('12',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Dp ID Client ID','1','0',NULL,NULL,'100',NULL,NULL,'dpIdClientId','1','','11','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('13',NULL,NULL,NULL,'java.lang.Double','2',NULL,'Number of Share','1','1',NULL,NULL,'-1',999999999999999999,NULL,'numberOfShare','1','10','12','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('14',NULL,NULL,NULL,'java.lang.Double','2',NULL,'Nominal Value','1','1',NULL,NULL,'-1',999999999999999999,NULL,'nominalValue','1','100','13','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
values('15',NULL,NULL,NULL,'java.lang.Double','2',NULL,'Market Price','1','1',NULL,NULL,'-1',999999999999999999,NULL,'marketPrice','1','10','14','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`) 
values('16',NULL,NULL,NULL,'java.util.Date','-1',NULL,'Market Actual Date Transfer IEPFPrice','1','1',NULL,NULL,'-1',NULL,NULL,'actualDateTransferDl','1','10-12-2020','15','1');




insert into `dm_obj_template` (`ID`, `LOCALE_CODE`, `OBJ_ID`, `TEMPLATE_BYTE`) values('2','en_US','2','PK');

insert into `dm_obj_def` (`ID`, `CODE`, `MAX_PARENT_ROWS`, `NAME`, `OBJECT_PER_FILE`, `STATUS_ID`, `IS_MAIN_OBJECT_YN`, `DATE_CREATED`, `DATE_MODIFIED`, `CREATED_BY`, `MODIFIED_BY`, `OBJ_ID`) 
values('2','RTADATA','0','RtaData','0','90796','0',now(),now(),'System','System','2');


insert into `dm_obj_conf` (`ID`, `BO_MODEL_NAME`, `BUS_DATA_PROC_CLASS`, `BUS_DATA_VALIDATOR_CLASS`, `DL_DATA_PROC_CLASS`, `DATA_START_ROW_NO`, `ERROR_INDEX_NO`, `HEADER_ROW_INDEX`, `INPUT_FILE_FORMAT`, `IS_ACTION_COLUMN_REQUIRED`, `IS_REQUIRED`, `OBJECT_ASSEMBLER_CLASS`, `DIS_ASSEMBLER_CLASS_NAME`, `OBJECT_CODE`, `OBJECT_TYPE`, `PARENT_REF_KEY_COL_IDX`, `PRIMARY_KEY_COL_IDX`, `SEQ_NO`, `TAB_OR_FILE_NAME`)
values('2','com.assignsecurities.bean.RtaDataBean','com.assignsecurities.service.impl.RtaDataDataProcessor','com.assignsecurities.service.impl.RtaDataDataValidator','com.assignsecurities.service.impl.RtaDataDataDLProcessor','5','13','4','xlsx','1','1','com.assignsecurities.service.impl.RtaDataDataAssembler','com.assignsecurities.service.impl.RtaDataDataAssembler','RTADATA',NULL,'1','1','2','scripts#');

insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('21',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Action','0','1',NULL,NULL,'32',NULL,NULL,'dlAction','2','Add','0','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('22',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Company Name','1','1',NULL,NULL,'255',NULL,NULL,'companyName','2','ABB INDIA LIMITED','1','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('23',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Registrar Name','1','1',NULL,NULL,'255',NULL,NULL,'registrarName','2','KARVY FINTECH PVT. LTD, BANGALORE','2','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('24',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Address','1','1',NULL,NULL,'400',NULL,NULL,'address','2','Some Address','3','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('25',NULL,NULL,NULL,'java.lang.String','-1',NULL,'State','1','1',NULL,NULL,'255',NULL,NULL,'state','2','Maharastra','4','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('26',NULL,NULL,NULL,'java.lang.String','-1',NULL,'City','1','1',NULL,NULL,'255',NULL,NULL,'city','2','Pune','5','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('27',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Contact Number','1','1',NULL,NULL,'30',NULL,NULL,'contactNumber','2','323232323','6','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('28',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Email','1','1',NULL,NULL,'255',NULL,NULL,'email','2','aa@test.com','7','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('29',NULL,NULL,NULL,'java.lang.Double','2',NULL,'DD Amount','1','1',NULL,NULL,'-1','99999999999999999',NULL,'ddAmount','2','123','8','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('30',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Security Code','1','1',NULL,NULL,'255',NULL,NULL,'securityCode','2','dd33','9','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('31',NULL,NULL,NULL,'java.lang.String','-1',NULL,'ISIN Number','1','1',NULL,NULL,'255',NULL,NULL,'isinNumber','2','dd111','10','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('32',NULL,NULL,NULL,'java.lang.String','-1',NULL,'Security ID','1','1',NULL,NULL,'255',NULL,NULL,'securityId','2','SID','11','1');
insert into `dm_obj_attribute_dtl` (`ID`, `CURRENT_OBJECT_ID`, `COLLECTION_DATA_TYPE`, `DATA_FORMATOR_KEY`, `TYPE`, `DECIMAL_PLACES`, `DELIMITER_KEY`, `ATTRIBUTE_HEADER_NAME`, `IS_BO_ATTRIBUTE`, `IS_REQUIRED`, `IS_REVERSE_VALUE_USED`, `isXMLAttribute`, `MAX_LENGTH`, `MAX_VALUE`, `MIN_VALUE`, `ATTRIBUTE_NAME`, `PARENT_OBJECT_ID`, `SAMPLE_VALUE`, `SEQUENCE_NUMBER`, `XML_TAG_NAME`)
 values('33',NULL,NULL,NULL,'java.lang.String','1',NULL,'MPS','1','1',NULL,NULL,'255',NULL,NULL,'mps','2','ABB','12','1');