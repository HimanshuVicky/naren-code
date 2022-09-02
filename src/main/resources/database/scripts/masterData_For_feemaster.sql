--liquibase formatted sql
--changeset {authorName}:{id}

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Agreement Fees','Both',1,1);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Processing Fees','FixValue',1,2);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Indemnity Bond','FixValue',0,3);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Name Change Effidevit','FixValue',0,4);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Address Change Effidevit','FixValue',0,5);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Signature Change Effidevit','FixValue',0,6);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Face Value Effidevit','FixValue',0,7);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Transmission of Shares Effidevit','FixValue',0,8);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Effidevit for Duplicat Share Cert','FixValue',0,9);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Lawyer Succession Fees','FixValue',0,10);

insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Paper Advertisement Fees','FixValue',0,11);