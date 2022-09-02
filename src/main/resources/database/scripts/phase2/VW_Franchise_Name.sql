--liquibase formatted sql
--changeset {authorName}:{id}

CREATE OR REPLACE VIEW `VW_Franchise_Name` AS 
select distinct name,UserId,id from (
select distinct name, au1.UserId,f.id  from franchise f , VW_Application_Detls au1 
 where (id=au1.ReferalPartnerFranchiseId or id=au1.ReferalFranchiseId)
union 
select name,'0',f.id UserId from franchise f where f.id=1) VW_Franchise_Name;
