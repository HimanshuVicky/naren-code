--liquibase formatted sql
--changeset {authorName}:{id}
INSERT INTO `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) VALUES('919','CASE_RTALETTER_GEN_EMAIL_SUB','CASE_RTALETTER_GEN_EMAIL_SUB','EMAIL','RTA Letter generated');
INSERT INTO `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) VALUES('920','CASE_RTALETTER_GEN_EMAIL_BODY','CASE_RTALETTER_GEN_EMAIL_BODY','EMAIL','Dear  {0},<br/><br/>The RTA Letter for Your case number {1} has been generated. Please follow the steps as outlined.<br/><br/>1. Please download the attached documents and print them on the A4 size paper.<br/>2. Please sign the documents at the designated place.<br/>3. Please scan the signed documents.<br/>4. Please mail all the documents to the RTA.<br/>5. Please upload all the documents to the website along with the courier receipt.<br/>When you receive a response from the RTA, please upload the RTA response letter on the website. {2}');


insert into `property` (`Id`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `DATE_MODIFIED`, `MODIFIED_BY`) 
values('32','WEBSITE','http://www.findmymoney.in','http://www.findmymoney.in','','1','2021-04-17 13:58:12','Sysadmin');
insert into `property` (`Id`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `DATE_MODIFIED`, `MODIFIED_BY`) 
values('33','CUSTOMER_CARE_SUPPORT_NUMBER','91-8050570505','91-8050570505','','1','2021-04-17 13:58:12','Sysadmin');