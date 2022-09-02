--liquibase formatted sql
--changeset {authorName}:{id}

CREATE OR REPLACE VIEW `VW_Referral_Detls` AS 
select distinct * from (
select au.UserId ,au.ReferalUserId,au.ReferalFranchiseId,f.Name  ReferralName,'RF' ReferalType
from VW_Application_Detls au 
 inner join franchise f on f.Id=au.ReferalFranchiseId and au.UserType ='User'
union
select au.UserId ,au.ReferalUserId,au.ReferalFranchiseId,CONCAT(auRef.`FirstName`, ' ', auRef.`LastName`) ReferralName,'RP' ReferalType
 from VW_Application_Detls au 
inner Join VW_Application_Detls auRef on auRef.UserId =au.ReferalUserId  and au.UserType ='User') VW_Referral;
