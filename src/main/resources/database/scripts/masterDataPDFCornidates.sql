--liquibase formatted sql
--changeset {authorName}:{id}

update `property` set `CURRENT_VALUE`=20 where `PROP_NAME`='SurePassSign1XCo-Ordinate';
update `property` set `CURRENT_VALUE`=10 where `PROP_NAME`='SurePassSign1YCo-Ordinate';

update `property` set `CURRENT_VALUE`=420 where `PROP_NAME`='SurePassSign2XCo-Ordinate';
update `property` set `CURRENT_VALUE`=10 where `PROP_NAME`='SurePassSign2YCo-Ordinate';

