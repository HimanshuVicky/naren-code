--liquibase formatted sql
--changeset {authorName}:{id}
UPDATE message_template SET message='Dear {0}\n\nWelcome to {1}\n\nYour pin for login is {2}.' WHERE code='USER_REGISTRATION_SMS';

UPDATE message_template SET message='Dear {0}\n\nYour pin for login is {1}.' WHERE code='USER_LOGIN_PIN_SMS';

UPDATE message_template SET message='Dear {0}\n\nYour application has been submitted for validation. Reference number is {1}.' WHERE code='VALIDATION_SUBMISSION_SMS';

UPDATE message_template SET message='Dear {0}\n\nYour application {1} has been validated. Please login to submit the case. Click on {2}' WHERE code='APPLICATION_APPROVED_SMS';

UPDATE message_template SET message='Dear {0}\n\nYour case {1} is pending submission. Please login to submit the case for processing. {2}' WHERE code='REMIND_USER_SMS';

UPDATE message_template SET message='Dear {0}\n\nYour case {1} has been created. Please pay the processing fees and update details. Please login to {2}' WHERE code='CASE_CREATED_SMS';

UPDATE message_template SET message='Dear {0}\n\nThe payment has been updated for case {1}. Please approve the payment.' WHERE code='PAYMENT_UPDATED_SMS';

UPDATE message_template SET message='Dear {0}\n\nYour payment for case {1} has been approved. Please complete your eAdhar verification. Please login at {2}' WHERE code='PAYMENT_APPROVED_SMS';

UPDATE message_template SET message='Dear {0}\n\nYour case no. {1} is now ready for processing. Please check status on {2}' WHERE code='EADHAR_COMPLETE_SMS';

UPDATE message_template SET message= 'Dear {0}\n\nYour case no. {1} is now ready for eAdhar. Please complete eAdhar' WHERE code='EADHAR_COMPLETE_ADMIN_SMS';

UPDATE message_template SET message= 'The RTA letter for case no. {0} has been generated. Please check your email for further actions.' WHERE code='RTA_LETTER_GENERATED_SMS';

UPDATE message_template SET message= 'Documents have been uploaded to case {0}. Please verify.' WHERE code='DOCUMENTS_UPLOADED_SMS';

UPDATE message_template SET message='Dear {0}\n\nYour application {1} has been rejected. Please login to check the details. Click {2}' WHERE code='ADMIN_APPLICATION_REJECTED_SMS';

