--liquibase formatted sql
--changeset {authorName}:{id}
--Passport Copy
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Passport Copy','Passport Copy','Uploaded','56','','','');

--Noc
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Noc','Noc','Generated','57','','','');