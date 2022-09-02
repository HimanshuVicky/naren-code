--liquibase formatted sql
--changeset {authorName}:{id}

CREATE INDEX script_Comp
    ON `script` (InvestorFirstName,InvestorLastName,FolioNumber,DpIDClientID);
    
CREATE INDEX script_CompanyName
    ON `script` (CompanyName);