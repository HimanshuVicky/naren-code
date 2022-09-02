--liquibase formatted sql
--changeset {authorName}:{id}
UPDATE message_template  SET message = 'Dear {0}\r\nYour application {1} has been rejected. Please login to submit the case. Click on {2}' WHERE CODE='ADMIN_APPLICATION_REJECTED_SMS'; 