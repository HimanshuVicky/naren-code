--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `case`
DROP FOREIGN KEY fk_case__FranchiseId; 
	
	
ALTER TABLE `case`
ADD CONSTRAINT `fk_case__FranchiseId` FOREIGN KEY (`FranchiseId`) REFERENCES `franchise` (`Id`);

