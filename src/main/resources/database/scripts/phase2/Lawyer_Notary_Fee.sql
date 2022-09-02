--liquibase formatted sql
--changeset {authorName}:{id}
insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`,userTypeId) 
values ('Lawyer Succession Fees2','FixValue',0,13,4);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`,userTypeId) 
values ('Courier Charges 1','FixValue',0,14,7);
insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`,userTypeId) 
values ('Courier Charges 2','FixValue',0,15,7);
insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`,userTypeId) 
values ('Courier Charges 3','FixValue',0,16,7);
insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`,userTypeId) 
values ('Courier Charges 4','FixValue',0,17,7);