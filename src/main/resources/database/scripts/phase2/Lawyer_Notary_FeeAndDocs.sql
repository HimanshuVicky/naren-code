--liquibase formatted sql
--changeset {authorName}:{id}

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`,userTypeId) 
values ('True Copy Fees','FixValue',0,17,7);
insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`,userTypeId) 
values ('Notary Mislleneous Fees 1','FixValue',0,18,7);
insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`,userTypeId) 
values ('Notary Mislleneous Fees 1','FixValue',0,19,7);

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('1st holder True copy','1st holder True copy','Uploaded','70','','','');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('2nd holder True copy','2nd holder True copy','Uploaded','71','','','');
