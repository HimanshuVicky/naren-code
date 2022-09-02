--liquibase formatted sql
--changeset {authorName}:{id}
UPDATE property SET current_value='MjM0OWUyODVmMzZkYjVmOGYwMjI2M2Y3ZGE2NjgxMTA='  WHERE prop_name='SMS_API_KEY';
 
UPDATE property SET current_value='ADHYTA'  WHERE prop_name='SMS_API_SENDER';

