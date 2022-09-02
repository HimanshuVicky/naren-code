--liquibase formatted sql
--changeset {authorName}:{id}

update property set CURRENT_VALUE='https://www.bseindia.com/download/BhavCopy/Equity/EQ_ISINCODE_$DD$$MM$$YY$.zip' where PROP_NAME='BHAV_BSE_URL';

