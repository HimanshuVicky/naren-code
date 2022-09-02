--liquibase formatted sql
--changeset {authorName}:{id}

--New Documents for Notary 
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Witness True Copy','Witness True Copy ','Uploaded','57','','','');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Surety True Copy','Surety True Copy','Uploaded','58','','','');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Death Certificates','Death Certificates','Uploaded','59','','','');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('IEPF Indemnity','IEPF Indemnity','Uploaded','60','','','');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Advance Receipt','Advance Receipt','Uploaded','61','','','');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Notary Misclleneous 1','Notary Misclleneous 1','Uploaded','62','','','');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Notary Misclleneous 2','Notary Misclleneous 2','Uploaded','63','','','');


--New Documents for Lawyer
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Petition','Petition','Uploaded','64','','','');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Succession Certificate','Succession Certificate','Uploaded','64','','','');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Lawyer Misclleneous 1','Lawyer Misclleneous 1','Uploaded','64','','','');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Lawyer Misclleneous 2','Lawyer Misclleneous 2','Uploaded','64','','','');
