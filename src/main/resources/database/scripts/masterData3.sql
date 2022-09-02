--liquibase formatted sql
--changeset {authorName}:{id}
insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('23','SurePassAuthenticationToken','eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MTI2MDEwNzcsIm5iZiI6MTYxMjYwMTA3NywianRpIjoiMmY3NjUzZjQtNjcxYS00MDI2LWJiNGQtZTU3MDI3NGUxOGVmIiwiZXhwIjoxNjE1MTkzMDc3LCJpZGVudGl0eSI6ImRldi50cmFja3NvZnRzb2x1dGlvbnNAYWFkaGFhcmFwaS5pbyIsImZyZXNoIjpmYWxzZSwidHlwZSI6ImFjY2VzcyIsInVzZXJfY2xhaW1zIjp7InNjb3BlcyI6WyJyZWFkIl19fQ.hRL-rTyiAnGQrTTJ6rZblQgQe3IpaTTp_dg_cPaMPN0','SurePassAuthenticationToken','','1','Sysadmin',now());

insert into `property` (`ID`, `PROP_NAME`, `CURRENT_VALUE`, `DESCRIPTION`, `ORIG_VALUE`, `EDITABLE`, `MODIFIED_BY`, `DATE_MODIFIED`)
values('24','SurePassUrl','https://sandbox.aadhaarkyc.io','SurePassUrl','','1','Sysadmin',now());


