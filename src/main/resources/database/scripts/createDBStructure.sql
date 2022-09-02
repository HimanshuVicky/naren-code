--liquibase formatted sql
--changeset {authorName}:{id}


CREATE TABLE `state` (
  `ID` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `CODE` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
);


CREATE TABLE `typeofusers` (
  `ID` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


CREATE TABLE `email` (
  `ID` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `From_EMail` varchar(300) NOT NULL,
  `SUBJECT` varchar(300) NOT NULL,
  `DATE_CREATED` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DATE_MODIFIED` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATED_BY` varchar(100) DEFAULT NULL,
  `MODIFIED_BY` varchar(100) NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `Status_Id` int(11) NOT NULL DEFAULT '1201',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



CREATE TABLE `email_bcc` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EMail_Id` bigint(20) unsigned zerofill NOT NULL,
  `EMail` varchar(300) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `email_email_bcc_EMail_Id_to_ID` (`EMail_Id`),
  CONSTRAINT `email_email_bcc_EMail_Id_to_ID` FOREIGN KEY (`EMail_Id`) REFERENCES `email` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `email_body` (
  `ID` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `EMail_Id` bigint(20) unsigned zerofill NOT NULL,
  `EMail_body` blob NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `email_email_body_EMail_Id_to_ID` (`EMail_Id`),
  CONSTRAINT `email_email_body_EMail_Id_to_ID` FOREIGN KEY (`EMail_Id`) REFERENCES `email` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



CREATE TABLE `email_cc` (
  `ID` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `EMail_Id` bigint(20) unsigned zerofill NOT NULL,
  `EMail` varchar(300) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `email_email_cc_EMail_Id_to_ID` (`EMail_Id`),
  CONSTRAINT `email_email_cc_EMail_Id_to_ID` FOREIGN KEY (`EMail_Id`) REFERENCES `email` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `email_to` (
  `ID` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `EMail_Id` bigint(20) unsigned zerofill NOT NULL,
  `EMail` varchar(300) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `email_email_to_EMail_Id_to_ID` (`EMail_Id`),
  CONSTRAINT `email_email_to_EMail_Id_to_ID` FOREIGN KEY (`EMail_Id`) REFERENCES `email` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



CREATE TABLE `message_template` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `NAME` varchar(50) NOT NULL,
  `TYPE` varchar(15) NOT NULL COMMENT 'EMAIL/NOTIFICATION/BOTH',
  `MESSAGE` varchar(2000) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=892 DEFAULT CHARSET=utf8;


CREATE TABLE `property` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `PROP_NAME` varchar(300) DEFAULT NULL,
  `CURRENT_VALUE` varchar(300) DEFAULT NULL,
  `DESCRIPTION` varchar(600) DEFAULT NULL,
  `ORIG_VALUE` varchar(300) DEFAULT NULL,
  `EDITABLE` tinyint(1) DEFAULT NULL,
  `DATE_MODIFIED` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_BY` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;




CREATE TABLE `address` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Address` varchar(400) NULL,
  `City` varchar(100) NOT NULL,
  `PinCode` varchar(10) NULL,
  `State` varchar(100) NULL,
  `Country` varchar(255) NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*Table structure for table `franchise` */



CREATE TABLE `franchise` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(150) NOT NULL,
  `AddressId` bigint(20) NOT NULL,
  `ContactNumber` varchar(30) DEFAULT NULL,
  `OwnerFirstName` varchar(250) NOT NULL,
  `OwnerLastName` varchar(250) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*Table structure for table `applicationuser` */


CREATE TABLE `applicationuser` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(250) NOT NULL,
  `MiddleName` varchar(250) DEFAULT NULL,
  `LastName` varchar(250) NOT NULL,
  `UserType` varchar(10) NOT NULL,
  `EmailId` varchar(200) DEFAULT NULL,
  `DateOfBirth` date DEFAULT NULL,
  `AlternateMobileNo` varchar(10) DEFAULT NULL,
  `AddressId` bigint(20) NOT NULL,
  `IsActive` tinyint(1) NOT NULL DEFAULT '1',
  `FranchiseId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `AddressId_Id_applicationuser__AddressId` (`AddressId`),
  CONSTRAINT `AddressId_Id_applicationuser__AddressId` FOREIGN KEY (`AddressId`) REFERENCES `address` (`Id`),
  KEY `franchise_Id_applicationuser__franchiseId` (`FranchiseId`),
  CONSTRAINT `franchise_Id_applicationuser__franchiseId` FOREIGN KEY (`FranchiseId`) REFERENCES `franchise` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;





/*Table structure for table `userlogin` */


CREATE TABLE `userlogin` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `MobileNo` varchar(11) NOT NULL,
  `Pin` varchar(150) NOT NULL,
  `ApplicationUserId` bigint(20) NOT NULL,
  `IsActive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`Id`),
  KEY `userlogin_ApplicationUserId_applicationuser__Id` (`ApplicationUserId`),
  CONSTRAINT `userlogin_ApplicationUserId_applicationuser__Id` FOREIGN KEY (`ApplicationUserId`) REFERENCES `applicationuser` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*Table structure for table `securitysession` */



CREATE TABLE `securitysession` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `SessionId` varchar(100) NOT NULL,
  `UserId` bigint(20) NOT NULL,
  `ValidityPeriod` int(11) NOT NULL DEFAULT '0',
  `DateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  KEY `securitysession_UserId_userlogin__Id` (`UserId`),
  CONSTRAINT `securitysession_UserId_userlogin__Id` FOREIGN KEY (`UserId`) REFERENCES `userlogin` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;


CREATE TABLE `dm_dl_exp_files` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COMPLETION_DATE` datetime DEFAULT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `DATE_MODIFIED` datetime NOT NULL,
  `FILE` tinyblob,
  `FILE_FORMAT` varchar(255) NOT NULL,
  `FILE_NAME` varchar(255) DEFAULT NULL,
  `TYPE_ID` bigint(20) NOT NULL,
  `STATUS_ID` bigint(20) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `MODIFIED_BY` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `dm_obj_conf` */

CREATE TABLE `dm_obj_conf` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BO_MODEL_NAME` varchar(255) NOT NULL,
  `BUS_DATA_PROC_CLASS` varchar(255) DEFAULT NULL,
  `BUS_DATA_VALIDATOR_CLASS` varchar(255) DEFAULT NULL,
  `DL_DATA_PROC_CLASS` varchar(255) DEFAULT NULL,
  `DATA_START_ROW_NO` int(11) DEFAULT NULL,
  `ERROR_INDEX_NO` int(11) DEFAULT NULL,
  `HEADER_ROW_INDEX` int(11) NOT NULL,
  `INPUT_FILE_FORMAT` varchar(255) NOT NULL,
  `IS_ACTION_COLUMN_REQUIRED` tinyint(1) DEFAULT NULL,
  `IS_REQUIRED` tinyint(1) NOT NULL,
  `OBJECT_ASSEMBLER_CLASS` varchar(255) DEFAULT NULL,
  `DIS_ASSEMBLER_CLASS_NAME` varchar(255) DEFAULT NULL,
  `OBJECT_CODE` varchar(255) NOT NULL,
  `OBJECT_TYPE` varchar(255) DEFAULT NULL,
  `PARENT_REF_KEY_COL_IDX` varchar(255) NOT NULL,
  `PRIMARY_KEY_COL_IDX` varchar(255) NOT NULL,
  `SEQ_NO` int(11) NOT NULL,
  `TAB_OR_FILE_NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


/*Table structure for table `dm_obj_attribute_dtl` */

CREATE TABLE `dm_obj_attribute_dtl` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CURRENT_OBJECT_ID` bigint(20) DEFAULT NULL,
  `COLLECTION_DATA_TYPE` varchar(255) DEFAULT NULL,
  `DATA_FORMATOR_KEY` varchar(255) DEFAULT NULL,
  `TYPE` varchar(255) NOT NULL,
  `DECIMAL_PLACES` int(11) DEFAULT NULL,
  `DELIMITER_KEY` varchar(255) DEFAULT NULL,
  `ATTRIBUTE_HEADER_NAME` varchar(255) NOT NULL,
  `IS_BO_ATTRIBUTE` tinyint(1) DEFAULT NULL,
  `IS_REQUIRED` tinyint(1) NOT NULL,
  `IS_REVERSE_VALUE_USED` tinyint(1) DEFAULT NULL,
  `isXMLAttribute` tinyint(1) DEFAULT NULL,
  `MAX_LENGTH` int(11) NOT NULL,
  `MAX_VALUE` bigint(20) DEFAULT NULL,
  `MIN_VALUE` bigint(20) DEFAULT NULL,
  `ATTRIBUTE_NAME` varchar(255) NOT NULL,
  `PARENT_OBJECT_ID` bigint(20) DEFAULT NULL,
  `SAMPLE_VALUE` varchar(255) DEFAULT NULL,
  `SEQUENCE_NUMBER` bigint(20) DEFAULT NULL,
  `XML_TAG_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `dm_obj_attribute_dtl_PARENT_OBJECT_ID__dm_obj_conf_id` (`PARENT_OBJECT_ID`),
  CONSTRAINT `dm_obj_attribute_dtl_PARENT_OBJECT_ID__dm_obj_conf_id` FOREIGN KEY (`PARENT_OBJECT_ID`) REFERENCES `dm_obj_conf` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8;


/*Table structure for table `dm_obj_template` */


CREATE TABLE `dm_obj_template` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LOCALE_CODE` varchar(255) NOT NULL,
  `OBJ_ID` bigint(20) NOT NULL,
  `TEMPLATE_BYTE` blob NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;



/*Table structure for table `dm_obj_def` */

CREATE TABLE `dm_obj_def` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(255) NOT NULL,
  `MAX_PARENT_ROWS` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `OBJECT_PER_FILE` bigint(20) NOT NULL,
  `STATUS_ID` bigint(20) NOT NULL,
  `IS_MAIN_OBJECT_YN` tinyint(1) NOT NULL,
  `DATE_CREATED` date NOT NULL,
  `DATE_MODIFIED` date DEFAULT NULL,
  `CREATED_BY` varchar(100) DEFAULT NULL,
  `MODIFIED_BY` varchar(100) DEFAULT NULL,
  `OBJ_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `dm_obj_def_OBJ_ID__dm_obj_template_Id` (`OBJ_ID`),
  CONSTRAINT `dm_obj_def_OBJ_ID__dm_obj_template_Id` FOREIGN KEY (`OBJ_ID`) REFERENCES `dm_obj_template` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


/*Table structure for table `dm_obj_import` */

CREATE TABLE `dm_obj_import` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATE_CREATED` datetime NOT NULL,
  `DATE_MODIFIED` datetime NOT NULL,
  `ERROR_REC_COUNT` bigint(20) DEFAULT NULL,
  `FILE_BYTE` blob NOT NULL,
  `FILE_FORMAT` varchar(255) NOT NULL,
  `FILE_NAME` varchar(255) NOT NULL,
  `LOCALE_CODE` varchar(255) NOT NULL,
  `OBJ_ID` bigint(20) NOT NULL,
  `RETRY_FILE` blob,
  `RETRY_FILE_NAME` varchar(255) DEFAULT NULL,
  `STATUS_ID` bigint(20) NOT NULL,
  `TOTAL_REC_COUNT` bigint(20) DEFAULT NULL,
  `IMPORTED_BY` bigint(20) NOT NULL,
  `MODIFIED_BY` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;




/*Table structure for table `Script` */

CREATE TABLE `script` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `SecurityCode` varchar(255) NOT NULL,
  `CompanyName` varchar(255) NOT NULL,
  `InvestorName` varchar(255) NOT NULL,
  `FatherName` varchar(255) NOT NULL,
  `Address` varchar(400) NOT NULL,
  `Country` varchar(255) NOT NULL,
  `State` varchar(255) NOT NULL,
  `City` varchar(255) NOT NULL,
  `Pincode` varchar(10) NOT NULL,
  `FolioNumber` varchar(100) NULL,
  `DpIDClientID` varchar(100) NULL,
  `NumberOfShare` double NOT NULL,
  `NominalValue` double NOT NULL,
  `MarketPrice` double NOT NULL,
  `ActualDateTransferIEPF` datetime NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Table structure for table `bsescriptdetails` */

CREATE TABLE `bsescriptdetails` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SC_CODE` varchar(255) NOT NULL,
  `SC_NAME` varchar(255) NOT NULL,
  `SC_GROUP` varchar(255) NOT NULL,
  `SC_TYPE` varchar(255) NOT NULL,
  `OPEN` double NOT NULL,
  `HIGH` double NOT NULL,
  `LOW` double NOT NULL,
  `CLOSE` double NOT NULL,
  `LAST` double NOT NULL,
  `PREVCLOSE` double NOT NULL,
  `NO_TRADES` double NOT NULL,
  `NO_OF_SHRS` double NOT NULL,
  `NET_TURNOV` double NOT NULL,
  `TDCLOINDI` varchar(255)  NULL,
  `ISIN_CODE` varchar(255) NOT NULL,
  `TRADING_DATE` datetime NOT NULL,
  `FILLER2` varchar(255)  NULL,
  `FILLER3` varchar(255)  NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;



/*Table structure for table `nsescriptdetails` */

CREATE TABLE `nsescriptdetails` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SYMBOL` varchar(255) NOT NULL,
  `SERIES` varchar(255) NOT NULL,
  `OPEN` double NOT NULL,
  `HIGH` double NOT NULL,
  `LOW` double NOT NULL,
  `CLOSE` double NOT NULL,
  `LAST` double NOT NULL,
  `PREVCLOSE` double NOT NULL,
  `TOTTRDQTY` double NOT NULL,
  `TOTTRDVAL` double NOT NULL,
  `TIMESTAMP` datetime NOT NULL,
  `TOTALTRADES` double NOT NULL,
  `ISIN` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Table structure for table `rtadata` */

CREATE TABLE `rtadata` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CompanyName` varchar(255) NOT NULL,
  `RegistrarName` varchar(255) NOT NULL,
  `Address` varchar(400) NOT NULL,
  `State` varchar(255) NOT NULL,
  `City` varchar(255) NOT NULL,
  `ContactNumber` varchar(30) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `DDAmount` double NOT NULL,
  `SecurityCode` varchar(255) NOT NULL,
  `IsinNumber` varchar(255) NOT NULL,
  `SecurityId` varchar(255) NOT NULL,
  `MPS` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;


/*Table structure for table `myfavourte` */

CREATE TABLE `myfavourte` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(20) NOT NULL,
  `ScriptId` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_myfavourte__userlogin_Id` (`UserId`),
  CONSTRAINT `fk_myfavourte__userlogin_Id` FOREIGN KEY (`UserId`) REFERENCES `userlogin` (`Id`),
  KEY `fk_myfavourte__script_Id` (`ScriptId`),
  CONSTRAINT `fk_myfavourte__script_Id` FOREIGN KEY (`ScriptId`) REFERENCES `script` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;


/*Table structure for table `questioner` */
CREATE TABLE `questioner` (
  `ID` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `Question` varchar(400) NOT NULL,
  `IsActive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`)
);

/*Table structure for table `application` */

CREATE TABLE `application` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(250) NOT NULL,
  `MiddleName` varchar(250) DEFAULT NULL,
  `LastName` varchar(250) NOT NULL,
  `CompanyName` varchar(255) NOT NULL,
  `Status` varchar(25) NOT NULL,
  `UserId` bigint(20) NOT NULL,
  `FeeType` varchar(15) NULL,
  `FeeValue` double NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Remarks` varchar(450) NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_application__userlogin_Id` (`UserId`),
  CONSTRAINT `fk_application__userlogin_Id` FOREIGN KEY (`UserId`) REFERENCES `userlogin` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Table structure for table `applicationscript` */

CREATE TABLE `applicationscript` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ApplicationId` bigint(20) NOT NULL,
  `ScriptId` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_applicationscript__ApplicationId` (`ApplicationId`),
  CONSTRAINT `fk_applicationscript__ApplicationId` FOREIGN KEY (`ApplicationId`) REFERENCES `application` (`Id`),
  KEY `fk_applicationscript__ScriptId` (`ScriptId`),
  CONSTRAINT `fk_applicationscript__ScriptId` FOREIGN KEY (`ScriptId`) REFERENCES `script` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;



/*Table structure for table `ApplicationScriptQues` */

CREATE TABLE `applicationscriptques` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ApplicationScriptId` bigint(20) NOT NULL,
  `QuestionId` int(10) unsigned zerofill NOT NULL,
  `Answer` varchar(15) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_applicationscriptques__applicationscript_Id` (`ApplicationScriptId`),
  CONSTRAINT `fk_applicationscriptques__applicationscript_Id` FOREIGN KEY (`ApplicationScriptId`) REFERENCES `applicationscript` (`Id`),
  KEY `fk_applicationscriptques__QuestionId_Id` (`QuestionId`),
  CONSTRAINT `fk_applicationscriptques__QuestionId_Id` FOREIGN KEY (`QuestionId`) REFERENCES `questioner` (`Id`)  
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;


/*Table structure for table `Document` */

CREATE TABLE `document` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(250) NOT NULL,
  `Type` varchar(20) NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CreatedBy` bigint(20) NOT NULL,
  `BucketName` varchar(300) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_document_CreatedBy__userlogin_Id` (`CreatedBy`),
  CONSTRAINT `fk_document_CreatedBy__userlogin_Id` FOREIGN KEY (`CreatedBy`) REFERENCES `userlogin` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;


/*Table structure for table `case` */

CREATE TABLE `case` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(250) NOT NULL,
  `MiddleName` varchar(250) DEFAULT NULL,
  `LastName` varchar(250) NOT NULL,
  `CompanyName` varchar(255) NOT NULL,
  `Status` varchar(15) NOT NULL,
  `UserId` bigint(20) NOT NULL,
  `FeeType` varchar(15) NOT NULL,
  `FeeValue` double NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Remarks` varchar(450) NULL,
  `AddressId` bigint(20) NOT NULL,
  `CommAddressId` bigint(20) NULL,
  `ApplicationId` bigint(20) NOT NULL,
  `AadharNumber` varchar(15) NULL,
  `PanNumber` varchar(15) NULL,
  `CancelCheckDoccumentId` bigint(20) NULL,
  `AadharDocId` bigint(20) NULL,
  `PanDocId` bigint(20) NULL,
  `AggrmentDcoumentId` bigint(20) NULL,
  `LegalHireCertId` bigint(20) NULL,
  `FranchiseId` bigint(20) NULL,
  `DigitalSignVerificationReference` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_case__userlogin_Id` (`UserId`),
  CONSTRAINT `fk_case__userlogin_Id` FOREIGN KEY (`UserId`) REFERENCES `userlogin` (`Id`),
  KEY `fk_case_AddressId__address_Id` (`AddressId`),
  CONSTRAINT `fk_case_AddressId__address_Id` FOREIGN KEY (`AddressId`) REFERENCES `address` (`Id`),
  KEY `fk_case_CommAddressId__address_Id` (`CommAddressId`),
  CONSTRAINT `fk_case_CommAddressId__address_Id` FOREIGN KEY (`CommAddressId`) REFERENCES `address` (`Id`),
  KEY `fk_case__application_Id` (`ApplicationId`),
  CONSTRAINT `fk_case__application_Id` FOREIGN KEY (`ApplicationId`) REFERENCES `application` (`Id`),
  KEY `fk_case__CancelCheckDoccumentId` (`CancelCheckDoccumentId`),
  CONSTRAINT `fk_case__CancelCheckDoccumentId` FOREIGN KEY (`CancelCheckDoccumentId`) REFERENCES `document` (`Id`),
  KEY `fk_case__AadharDocId` (`AadharDocId`),
  CONSTRAINT `fk_case__AadharDocId` FOREIGN KEY (`AadharDocId`) REFERENCES `document` (`Id`),
  KEY `fk_case__PanDocId` (`PanDocId`),
  CONSTRAINT `fk_case__PanDocId` FOREIGN KEY (`PanDocId`) REFERENCES `document` (`Id`),
  KEY `fk_case__AggrmentDcoumentId` (`AggrmentDcoumentId`),
  CONSTRAINT `fk_case__AggrmentDcoumentId` FOREIGN KEY (`AggrmentDcoumentId`) REFERENCES `document` (`Id`),
  KEY `fk_case__LegalHireCertId` (`LegalHireCertId`),
  CONSTRAINT `fk_case__LegalHireCertId` FOREIGN KEY (`LegalHireCertId`) REFERENCES `document` (`Id`),
   KEY `fk_case_FranchiseId` (`FranchiseId`),
  CONSTRAINT `fk_case_FranchiseId` FOREIGN KEY (`FranchiseId`) REFERENCES `franchise` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;








