--liquibase formatted sql
--changeset {authorName}:{id}
 
update `property` set `CURRENT_VALUE`=1200 where `PROP_NAME`='SurePassSign2XCo-Ordinate';

