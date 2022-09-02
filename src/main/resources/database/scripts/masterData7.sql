--liquibase formatted sql
--changeset {authorName}:{id}

update feemaster set isGSTApplicable=0,FeeType='Both',TempOrder=1 where FeeFor='Agreement Fees';

update feemaster set FeeType='FixValue',TempOrder=0 where FeeFor='Processing Fees';

