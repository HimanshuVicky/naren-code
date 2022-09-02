--liquibase formatted sql
--changeset {authorName}:{id}


ALTER TABLE `case`
ADD `IsSignedDocumentsVerified` tinyint(1) NOT NULL DEFAULT '0';

ALTER TABLE `case`
ADD `IsUploadRTAResponseVerified` tinyint(1) NOT NULL DEFAULT '0';

ALTER TABLE `case`
ADD `IseAdharComplete` tinyint(1) NOT NULL DEFAULT '0';


