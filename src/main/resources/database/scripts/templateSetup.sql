--liquibase formatted sql
--changeset {authorName}:{id}

delete from rta_templates_group;
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, MUMBAI','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, AHMEDABAD3','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, KOLKATA','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, DELHI1','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, VADODARA1','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, DELHI3','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, PUNE','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, AHMEDABAD1','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, COIMBATORE','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, DELHI2','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, AHMEDABAD2','LINK INTIME INDIA PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('LINK INTIME INDIA PVT. LTD, VADODARA2','LINK INTIME INDIA PVT. LTD');

insert into rta_templates_group (RtaName,RtaGroup) values ('BIGSHARE SERVICES PVT. LTD, AHMEDABAD','BIGSHARE SERVICES PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('BIGSHARE SERVICES PVT. LTD, DELHI','BIGSHARE SERVICES PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('BIGSHARE SERVICES PVT. LTD, GUJARAT','BIGSHARE SERVICES PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('BIGSHARE SERVICES PVT. LTD, HYDERABAD','BIGSHARE SERVICES PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('BIGSHARE SERVICES PVT. LTD, MUMBAI1','BIGSHARE SERVICES PVT. LTD');
insert into rta_templates_group (RtaName,RtaGroup) values ('BIGSHARE SERVICES PVT. LTD, MUMBAI2','BIGSHARE SERVICES PVT. LTD');

insert into rta_templates_group (RtaName,RtaGroup) values ('TSR DARASHAW LTD, MUMBAI','TSR DARASHAW LTD');

insert into rta_templates_group (RtaName,RtaGroup) values ('Kfintech Pvt Ltd','Kfintech Pvt Ltd');

delete from generated_documents_required where DocumentMasterId in (select Id from documentmaster where Particulars in('FIR Copy','Indemnity Bond','Name Change Affidavit','Address Change Affidavit',
'Specimen Signature Affidavit','Duplicate Share Certitficate Affidavit','Transmission Of Shares Affidavit','Paper Advertisment','Noc'));


delete from documentmaster where Particulars in('FIR Copy','Indemnity Bond','Name Change Affidavit','Address Change Affidavit',
'Specimen Signature Affidavit','Duplicate Share Certitficate Affidavit','Transmission Of Shares Affidavit','Paper Advertisment','Noc');

update documentmaster set RtaGroup='LINK INTIME INDIA PVT. LTD';

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Bond - For Issue of Duplicate Share Certificate','Indemnity Bond - For Issue of Duplicate Share Certificate','Generated','1','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Affidavit Duplicate Share Certificate','Affidavit Duplicate Share Certificate','Generated','2','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Affidavit for Duplicate cum transmission','Affidavit for Duplicate cum transmission','Generated','3','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Affidavit Name deletion cum duplicate','Affidavit Name deletion cum duplicate','Generated','4','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Change of Signature','Change of Signature','Generated','5','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Correction Name ,Address, Bank details','Correction Name ,Address, Bank details','Generated','6','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Bond for Duplicate cum Transmission','Indemnity Bond for Duplicate cum Transmission','Generated','7','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Bond Name Deletion Cum Duplicate','Indemnity Bond Name Deletion Cum Duplicate','Generated','8','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Transmission without succession','Indemnity Transmission without succession','Generated','9','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Specimen Signature new bankers verification','Specimen Signature new bankers verification','Generated','10','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Transmission Application','Transmission Application','Generated','11','','','','LINK INTIME INDIA PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('NOC for Transmission','NOC for Transmission','Generated','12','','','','LINK INTIME INDIA PVT. LTD');


delete from rtatemplates;
insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Indemnity Bond - For Issue of Duplicate Share Certificate','/Linkintime/Indemnity Bond - For Issue of Duplicate Share Certificate.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Affidavit Duplicate Share Certificate','/Linkintime/Affidavit Duplicate Share Certificate.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Affidavit for Duplicate cum transmission','/Linkintime/Affidavit for Duplicate cum transmission.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Affidavit Name deletion cum duplicate','/Linkintime/Affidavit Name deletion cum duplicate.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Change of Signature','/Linkintime/Change of Signature.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Correction Name ,Address, Bank details','/Linkintime/Correction Name ,Address, Bank details.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Indemnity Bond for Duplicate cum Transmission','/Linkintime/Indemnity Bond for Duplicate cum Transmission.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Indemnity Bond Name Deletion Cum Duplicate','/Linkintime/Indemnity Bond Name Deletion Cum Duplicate.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Indemnity Transmission without succession','/Linkintime/Indemnity Transmission without succession.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Specimen Signature new bankers verification','/Linkintime/Specimen Signature new bankers verification.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','Transmission Application','/Linkintime/Transmission Application.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('LINK INTIME INDIA PVT. LTD','NOC for Transmission','/Linkintime/NOC for Transmission.docx',1,0);
update rtatemplates set TemplateName='/Linkintime/Affidavit Duplicate Share Certificate.docx'
where TemplateType='Affidavit Duplicate Share Certificate';



#--BIGSHARE 
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Bond - For Issue of Duplicate Share Certificate','Indemnity Bond - For Issue of Duplicate Share Certificate','Generated','1','','','',
'BIGSHARE SERVICES PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Surety Duplicate cum Transmission Forms','Indemnity Surety Duplicate cum Transmission Forms','Generated','1','','','',
'BIGSHARE SERVICES PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Affidavit for change in signature','Affidavit for change in signature','Generated','1','','','',
'BIGSHARE SERVICES PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Affidavit For Duplicate  Share Certificate Issuance','Affidavit For Duplicate  Share Certificate Issuance','Generated','1','','','',
'BIGSHARE SERVICES PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Affidavit for Transmission and Duplicate','Affidavit for Transmission and Duplicate','Generated','1','','','',
'BIGSHARE SERVICES PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Bond without succession','Indemnity Bond without succession','Generated','1','','','',
'BIGSHARE SERVICES PVT. LTD');


insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Name Deletion','Name Deletion','Generated','1','','','','BIGSHARE SERVICES PVT. LTD');


insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Title Claim','Title Claim','Generated','1','','','','BIGSHARE SERVICES PVT. LTD');


insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Verification of Signature from Bank on Letter head','Verification of Signature from Bank on Letter head','Generated','1','','','',
'BIGSHARE SERVICES PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Advt format','Advt format','Generated','1','','','','BIGSHARE SERVICES PVT. LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('NOC for Transmission','NOC for Transmission','Generated','1','','','','BIGSHARE SERVICES PVT. LTD');



insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','Indemnity Bond - For Issue of Duplicate Share Certificate','/Bigshare/Indemnity Bond - For Issue of Duplicate Share Certificate.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','Indemnity Surety Duplicate cum Transmission Forms','/Bigshare/Indemnity Surety Duplicate cum Transmission Forms.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','Affidavit for change in signature','/Bigshare/Affidavit for change in signature.docx',1,0);


insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','Affidavit For Duplicate  Share Certificate Issuance','/Bigshare/Affidavit For Duplicate  Share Certificate Issuance.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','Affidavit for Transmission and Duplicate','/Bigshare/Affidavit for Transmission and Duplicate.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','Indemnity Bond without succession','/Bigshare/Indemnity Bond without succession.docx',1,0);


insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','Name Deletion','/Bigshare/Name Deletion.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','Title Claim','/Bigshare/Title Claim.docx',1,0);


insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','Advt format','/Bigshare/Advt format.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('BIGSHARE SERVICES PVT. LTD','NOC for Transmission','/Bigshare/NOC for Transmission.docx',1,0);

#--Kfintech 
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Specimen Signature','Specimen Signature','Generated','1','','','','Kfintech Pvt Ltd');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Bond','Indemnity Bond','Generated','1','','','','Kfintech Pvt Ltd');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Affidavit','Affidavit','Generated','1','','','','Kfintech Pvt Ltd');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Change of Address','Change of Address','Generated','1','','','','Kfintech Pvt Ltd');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Change of Signature','Change of Signature','Generated','1','','','','Kfintech Pvt Ltd');


insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Surety Draft','Surety Draft','Generated','1','','','','Kfintech Pvt Ltd');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('PUBLIC NOTICE','PUBLIC NOTICE','Generated','1','','','','Kfintech Pvt Ltd');



insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('Kfintech Pvt Ltd','Specimen Signature','/Kfintech/Specimen Signature.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('Kfintech Pvt Ltd','Indemnity Bond','/Kfintech/Indemnity Bond.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('Kfintech Pvt Ltd','Affidavit','/Kfintech/Affidavit.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('Kfintech Pvt Ltd','Change of Address','/Kfintech/Change of Address.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('Kfintech Pvt Ltd','Change of Signature','/Kfintech/Change of Signature.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('Kfintech Pvt Ltd','Surety Draft','/Kfintech/Surety Draft.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('Kfintech Pvt Ltd','PUBLIC NOTICE','/Kfintech/PUBLIC NOTICE.docx',1,0);

#-TSR DARASHAW LTD

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Name Change Deletion Signature','Name Change Deletion Signature','Generated','1','','','','TSR DARASHAW LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Affidavit','Affidavit','Generated','1','','','','TSR DARASHAW LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Affidavit Surety format','Affidavit Surety format','Generated','1','','','','TSR DARASHAW LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Bond','Indemnity Bond','Generated','1','','','','TSR DARASHAW LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Indemnity Bond for Suerty','Indemnity Bond for Suerty','Generated','1','','','','TSR DARASHAW LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('NOC','NOC','Generated','1','','','','TSR DARASHAW LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('QUESTIONNAIRE FORM','QUESTIONNAIRE FORM','Generated','1','','','','TSR DARASHAW LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Specimen Signature','Specimen Signature','Generated','1','','','','TSR DARASHAW LTD');

insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`,`RtaGroup`) 
values ('Title claim form','Title claim form','Generated','1','','','','TSR DARASHAW LTD');






insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('TSR DARASHAW LTD','Name Change Deletion Signature','/Tsr/Name Change Deletion Signature.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('TSR DARASHAW LTD','Affidavit','/Tsr/Affidavit.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('TSR DARASHAW LTD','Affidavit Surety format','/Tsr/Affidavit Surety format.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('TSR DARASHAW LTD','Indemnity Bond','/Tsr/Indemnity Bond.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('TSR DARASHAW LTD','Indemnity Bond for Suerty','/Tsr/Indemnity Bond for Suerty.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('TSR DARASHAW LTD','NOC','/Tsr/NOC.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('TSR DARASHAW LTD','QUESTIONNAIRE FORM','/Tsr/QUESTIONNAIRE FORM.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('TSR DARASHAW LTD','Specimen Signature','/Tsr/Specimen Signature.docx',1,0);

insert  into `rtatemplates`(Name,TemplateType,TemplateName,TempOrder,IsFeeRequired) 
values('TSR DARASHAW LTD','Title claim form','/Tsr/Title claim form.docx',1,0);

