--liquibase formatted sql
--changeset {authorName}:{id}

UPDATE `property` SET CURRENT_VALUE='smtp.gmail.com' where PROP_NAME='MAIL_HOST';

UPDATE `property` SET CURRENT_VALUE='Enrol-Me' where PROP_NAME='SEND_MAIL_AS';

UPDATE `property` SET CURRENT_VALUE='enrolme.tracksoft@gmail.com' where PROP_NAME='SMTP_AUTH_USER';

UPDATE `property` SET CURRENT_VALUE='Track$oft' where PROP_NAME='SMTP_AUTH_PWD';

UPDATE `property` SET CURRENT_VALUE='qaBrKdSwW64-yHFLHJn25L9OYmwnFmgPMnqURHNu5E' where PROP_NAME='SMS_API_KEY';

UPDATE `property` SET CURRENT_VALUE='https://api.textlocal.in/send/?' where PROP_NAME='SMS_API_URL';

UPDATE `property` SET CURRENT_VALUE='ENRLME' where PROP_NAME='SMS_API_SENDER';

UPDATE `property` SET CURRENT_VALUE='E:/ShareProject/' where PROP_NAME='BHAV_COPY_ROOT';

--S3 bucket Access Key ID and Secret Access Key
UPDATE `property` SET CURRENT_VALUE='AKIAJOSOBOATWQZUXGLA' where PROP_NAME='AWS_S3_ACCESSKEY';
UPDATE `property` SET CURRENT_VALUE='Vz1Jfi6Ned3ho5+JD0JFKv/J+8nrtDTx3QjjtRNw' where PROP_NAME='AWS_S3_SECRETKEY';



#--OTP:9676
insert into `securitysession` (`Id`, `SessionId`, `UserId`, `ValidityPeriod`, `DateCreated`) values('1','35aq5u7o79jjvr224beb9tdkb51609577446232','1','24','2021-01-03 06:30:49');


#--insert into `dm_obj_import` (`ID`, `DATE_CREATED`, `DATE_MODIFIED`, `ERROR_REC_COUNT`, `FILE_BYTE`, `FILE_FORMAT`, `FILE_NAME`, `LOCALE_CODE`, `OBJ_ID`, `RETRY_FILE`, `RETRY_FILE_NAME`, `STATUS_ID`, `TOTAL_REC_COUNT`, `IMPORTED_BY`, `MODIFIED_BY`)
#--values('2',now(),now(),'0','test','xlsx','Script.xlsx','en_US','1',NULL,NULL,'90796',NULL,'1','1');

#--insert into `dm_obj_import` (`ID`, `DATE_CREATED`, `DATE_MODIFIED`, `ERROR_REC_COUNT`, `FILE_BYTE`, `FILE_FORMAT`, `FILE_NAME`, `LOCALE_CODE`, `OBJ_ID`, `RETRY_FILE`, `RETRY_FILE_NAME`, `STATUS_ID`, `TOTAL_REC_COUNT`, `IMPORTED_BY`, `MODIFIED_BY`)
#--values('1',now(),now(),'0','test','xlsx','RtaData.xlsx','en_US','2',NULL,NULL,'90796',NULL,'1','1');


