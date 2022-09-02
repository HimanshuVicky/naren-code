--liquibase formatted sql
--changeset {authorName}:{id}

CREATE TABLE `customer_documents_required` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CaseId` bigint(20) NOT NULL,
  `DocumentMasterId` bigint(20)  NOT NULL,
  `DocumentId` bigint(20)  NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_customer_documents_required__CaseId` (`CaseId`),
  CONSTRAINT `fk_customer_documents_required__CaseId` FOREIGN KEY (`CaseId`) REFERENCES `case` (`Id`),
   KEY `fk_customer_documents_required__DocumentMasterId` (`DocumentMasterId`),
  CONSTRAINT `fk_customer_documents_required__DocumentMasterId` FOREIGN KEY (`DocumentMasterId`) REFERENCES `documentmaster` (`Id`),
   KEY `fk_customer_documents_required__DocumentId` (`DocumentId`),
  CONSTRAINT `fk_customer_documents_required__DocumentId` FOREIGN KEY (`DocumentId`) REFERENCES `document` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

