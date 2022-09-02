--liquibase formatted sql
--changeset {authorName}:{id}

delete  from citymaster where id in( SELECT cid FROM (select max(ID) cid from citymaster group by city,State_Union_Territory having count(city)>1
order by State_Union_Territory,city) AS aa)