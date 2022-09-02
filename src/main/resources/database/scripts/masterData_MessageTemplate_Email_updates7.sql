--liquibase formatted sql
--changeset {authorName}:{id}
UPDATE message_template SET message='Dear {0},<BR/><BR/>Your application has been submitted for validation. Reference number is {1}. Please note that you will get a call for verification and validation purpose from the customer care. You will be required to furnish the details of your application. Please note that we do not ask for any confidential information and passwords.
For any assistance please contact customer care at {2}.<br/><br/>Regards,<br/>Team {3}.' 
WHERE id=916;