--liquibase formatted sql
--changeset {authorName}:{id}
CREATE TABLE `email_attachment` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EMail_Id` bigint(20) unsigned zerofill NOT NULL,
  `AttachmentDocId` bigint(20) NULL,
  PRIMARY KEY (`ID`),
  KEY `email_attachment_EMail_Id_to_ID` (`EMail_Id`),
  CONSTRAINT `email_attachment_EMail_Id_to_ID` FOREIGN KEY (`EMail_Id`) REFERENCES `email` (`ID`),
   KEY `email_attachment__AttachmentDocId` (`AttachmentDocId`),
  CONSTRAINT `email_attachment__AttachmentDocId` FOREIGN KEY (`AttachmentDocId`) REFERENCES `document` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;