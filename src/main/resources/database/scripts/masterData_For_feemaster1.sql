--liquibase formatted sql
--changeset {authorName}:{id}
insert  into `feemaster`(`FeeFor`,`FeeType`,`isGSTApplicable`,`TempOrder`) 
values ('Advance Fee','FixValue',0,3);


update `feemaster` set TempOrder = TempOrder +1 where `FeeFor` ='Indemnity Bond';
update `feemaster` set TempOrder = TempOrder +1 where `FeeFor` ='Name Change Effidevit';
update `feemaster` set TempOrder = TempOrder +1 where `FeeFor` ='Address Change Effidevit';
update `feemaster` set TempOrder = TempOrder +1 where `FeeFor` ='Signature Change Effidevit';
update `feemaster` set TempOrder = TempOrder +1 where `FeeFor` ='Face Value Effidevit';
update `feemaster` set TempOrder = TempOrder +1 where `FeeFor` ='Transmission of Shares Effidevit';
update `feemaster` set TempOrder = TempOrder +1 where `FeeFor` ='Effidevit for Duplicat Share Cert';
update `feemaster` set TempOrder = TempOrder +1 where `FeeFor` ='Lawyer Succession Feest';
update `feemaster` set TempOrder = TempOrder +1 where `FeeFor` ='Paper Advertisement Fees';

