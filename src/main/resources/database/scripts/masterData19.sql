--liquibase formatted sql
--changeset {authorName}:{id}

update `documentmaster` set uploadedOrGenerated ='Generated'
where particulars in('RtaLetter1','RtaLetter2','CustomerAgreement');