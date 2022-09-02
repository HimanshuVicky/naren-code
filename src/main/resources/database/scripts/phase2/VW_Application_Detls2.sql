--liquibase formatted sql
--changeset {authorName}:{id}

CREATE OR REPLACE VIEW `VW_Application_Detls` AS 
select ul.id UserId,MobileNo,Pin,ul.IsActive,au.id ApplicationUserId,FirstName,MiddleName,LastName
,UserType,EmailId,DateOfBirth,AlternateMobileNo,AddressId,au.IsActive AuIsActive,FranchiseId
,madian_surname,default_surname,gender,DateCreated,ReferalFranchiseId,ReferalUserId,ReferalPartnerFranchiseId from 
userlogin ul  
	inner join applicationuser au on ul.ApplicationUserId=au.id;