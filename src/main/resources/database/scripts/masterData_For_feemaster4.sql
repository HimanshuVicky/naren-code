--liquibase formatted sql
--changeset {authorName}:{id}
UPDATE feemaster SET FeeFor = 'Name Change Affidavit' WHERE FeeFor = 'Name Change Effidevit';
UPDATE feemaster SET FeeFor = 'Address Change Affidavit' WHERE  FeeFor = 'Address Change Effidevit';
UPDATE feemaster SET FeeFor = 'Signature Change Affidavit' WHERE  FeeFor = 'Signature Change Effidevit';
UPDATE feemaster SET FeeFor = 'Face Value Affidavit' WHERE  FeeFor = 'Face Value Effidevit';
UPDATE feemaster SET FeeFor = 'Transmission of Shares Affidavit' WHERE  FeeFor = 'Transmission of Shares Effidevit';
UPDATE feemaster SET FeeFor = 'Affidavit for Duplicat Share Cert' WHERE FeeFor = 'Effidevit for Duplicat Share Cert';
