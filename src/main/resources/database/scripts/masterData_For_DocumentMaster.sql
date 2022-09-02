--liquibase formatted sql
--changeset {authorName}:{id}
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Aadhar','Aadhar','Uploaded','1','','','');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('PAN','PAN','Uploaded','2','','','');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Cancelled Cheque','Cancelled Cheque','Uploaded','3','','','');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Passport Photo','Passport Photo','Uploaded','4','','','');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('ITR / 6 Months Statement','ITR / 6 Months Statement','Uploaded','5','','','');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Courier Receipt','Courier Receipt','Uploaded','6','','','');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Share Certificate','Shares in Possession','Uploaded','7','Add more button','To Be Uploaded by Franchisee / Client','To Database Template');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Warrant copy','Warrant In Possession','Uploaded','8','Add more button','','To Database Template');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Death Certificate 1','Name and Relation to capture','Uploaded','23','Mandatory if Succession','To Be Uploaded by Franchisee / Client','To Database Template');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Death Certificate 2','Name and Relation to capture','Uploaded','24','1st Response Result','','');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Ration Card','Ration Card','Uploaded','25','','To Be Uploaded by Franchisee / Client','');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('FIR Copy','System Generated','Generated','26','Format will be given','To Be Uploaded by Franchisee / Client','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('DIS Copy','Our Fee Slip mentionng the value','Uploaded','27','Ashika Slip','To Be Uploaded by Franchisee / Client','2nd phase');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('CML Copy','Details of Ashika Account','Uploaded','28','','To Be Uploaded by Franchisee / Client','To be uploaded at the time of IEPF upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Indemnity Bond','System Generated','Generated','29','Payments Involved','Ref number of fees and then process','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Name Change Affidavit','System Generated','Generated','30','Payments Involved','Ref number of fees and then process','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Address Change Affidavit','System Generated','Generated','31','Payments Involved','Ref number of fees and then process','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Specimen Signature Affidavit','System Generated','Generated','32','Payments Involved','Ref number of fees and then process','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Duplicate Share Certitficate Affidavit','System Generated','Generated','33','Payments Involved','Ref number of fees and then process','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Transmission Of Shares Affidavit','System Generated','Generated','34','Payments Involved','Ref number of fees and then process','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Lega Heir Certificate','Advocate / Cleint','Uploaded','35','Payments Involved','Ref number of fees and then process','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Succession Certificate','Advocate / Cleint','Uploaded','36','Payments Involved','Ref number of fees and then process','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Paper Advertisment','Adhyata / Cleint','Generated','37','Payments Involved','Ref number of fees and then process','2nd Response upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Entitlement Letter','Sent by RTA in 2nd Response','Uploaded','38','IEPF Upload','Admin Upload','To be uploaded at the time of IEPF upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('Advance Receipt','IEPF Generated / To upload','Uploaded','39','IEPF Upload','Admin Upload','To be uploaded at the time of IEPF upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('IEPF Indeminity ','IEPF Generated / To upload','Uploaded','40','IEPF Upload / Payments Involved','Admin Upload','To be uploaded at the time of IEPF upload');
insert  into `documentmaster`(`Particulars`,`Type`,`UploadedOrGenerated`,TempOrder,`Action`,`Process`,`Link`) 
values ('IEPF Form','IEPF Generated / To upload','Uploaded','41','IEPF Upload','Admin Upload','To be uploaded at the time of IEPF upload');

