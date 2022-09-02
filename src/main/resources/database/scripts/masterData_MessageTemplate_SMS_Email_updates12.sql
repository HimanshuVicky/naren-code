--liquibase formatted sql
--changeset {authorName}:{id}
UPDATE message_template SET message='The RTA response has been upload for case {0}. Please verify.\\r\\nADHYATA IMF PVT. LTD'  
WHERE code='RTA_DOCUMENTS_UPLOADED_SMS';
