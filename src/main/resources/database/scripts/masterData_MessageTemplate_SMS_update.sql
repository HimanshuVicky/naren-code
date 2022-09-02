--liquibase formatted sql
--changeset {authorName}:{id}
UPDATE message_template SET message='Dear {0} \\r\\nWelcome to {1}\\r\\nYour pin for login is {2}.' WHERE id=901;

UPDATE message_template SET message='Dear {0}\\r\\nYour pin for login is {1}.' WHERE id=902;

UPDATE message_template SET message='Dear {0}\\r\\nYour application has been submitted for validation. Reference number is {1}.' WHERE id=903;

UPDATE message_template SET message='Dear {0}\\r\\nYour application {1} has been validated. Please login to submit the case. Click on {2}' WHERE id=904;

UPDATE message_template SET message='Dear {0}\\r\\nYour case {1} is pending submission. Please login to submit the case for processing. {2}' WHERE id=905;

UPDATE message_template SET message='Dear {0}\\r\\nYour case {1} has been created. Please pay the processing fees and update details. Please login to {2}' WHERE id=906;

UPDATE message_template SET message='Dear {0}\\r\\nThe payment has been updated for case {1}. Please approve the payment.' WHERE id=907;

UPDATE message_template SET message='Dear {0}\\r\\nYour payment for case {1} has been approved. Please complete your eAdhar verification. Please login at {2}' WHERE id=908;

UPDATE message_template SET message='Dear {0}\\r\\nYour case no. {1} is now ready for processing. Please check status on {2}' WHERE id=909;

UPDATE message_template SET message= 'Dear {0}\\r\\nYour case no. {1} is now ready for eAdhar. Please complete eAdhar' WHERE id=910;

UPDATE message_template SET message= 'The RTA letter for case no. {0} has been generated. Please check your email for further actions.' WHERE id=911;

UPDATE message_template SET message= 'Documents have been uploaded to case {0}. Please verify.' WHERE id=912;

UPDATE message_template SET message='Dear {0}\\r\\nYour application {1} has been rejected. Please login to check the details. Click {2}' WHERE id=913;