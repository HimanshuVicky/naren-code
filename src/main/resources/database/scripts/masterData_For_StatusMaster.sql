--liquibase formatted sql
--changeset {authorName}:{id}

insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Application ','Waiting Validation ','View','Validate','Validate','NA','NA','NA',1,'Application Validated','1','1');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Application ','Waiting Fees Allocation','View','View','Fee Allocate','NA','NA','NA',1,'Application Fee Alocated','1','2');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Application ','Waiting For Processing','Process','Remind','Remind','NA','NA','NA',1,'Application Processed','1','3');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Application ','Rejected Application ','View','Update','Update','NA','NA','NA',1,'Application Rejected','1','4');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Application ','In Progress','NA','NA','NA','NA','NA','NA',1,'Application In Progress','1','5');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1','Waiting Submission','View','View','View','NA','NA','NA',1,'Subnmitted','1','6');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1','Waiting Processing Fee','Update','Update','Update','NA','NA','NA',1,'Processing Fee Paid','1','7');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1','Waiting Processing Fee Confirmation','View','View','Update','NA','NA','NA',1,'Processing Fee Confirmed','1','8');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1','Waiting Customer Aadhar','Update','View','View','NA','NA','NA',1,'Customer eAdhar Done','1','9');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1','Waiting Admin Aadhar','View','NA','Update','NA','NA','NA',1,'Admin eAdhar Done','1','10');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1','Waiting Franchise Assigment','View','View','Update','NA','NA','NA',0,'Franchise Assigned','1','11');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1','Waiting RTA Letter 1 Generation','View','View','Generate','Generate','Generate','NA',0,'RTA letter 1 Generated','1','12');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1','Waiting Signed Documents Upload','Update','Update','Update','Update','Update','NA',0,'Signed Docs Uploaded','1','13');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1','Waiting RTA Response','Update','Update','Update','Update','Update','NA',1,'RTA Response Uploaded','1','13');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting Post Response Updation ','View','Update','Update','Update','Update','View',1,'Post Response Updation Completed','1','15');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting Required Document List','View','Update','Update','Update','Update','View',0,'Required Document list completed','1','16');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting Fees Schedule','View','Update','View','View','View','View',0,'Fees Schedule updated','1','17');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting Witness Info','Update','Update','Update','Update','Update','View',0,'Witness Info Verified','1','18');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting  Doc Upload','Update','Update','Update','Update','Update','Update',0,'Required Documents Uploaded','1','19');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting Additional Fees','Update','Update','Update','Update','Update','Update',0,'Additional Fees Paid','1','20');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting Fees Confirmation','Update','Update','Update','Update','Update','View',0,'Additional Fees Confirmed','1','21');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting  Doc Generation','View','Update','Update','Update','Update','View',0,'Doument Generation completed','1','22');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting RTA Letter 2 Generation','View','Update','Update','Update','Update','View',1,'RTA Letter 2 Generated','1','23');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting Documents Upload','Update','View','View','View','View','View',0,'Signed Documents 2 Uploaded','1','24');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 2','Waiting RTA Response','Update','View','View','View','View','View',1,'RTA Response 2 Uploaded','1','25');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 3','Waiting IEPF Form Upload','NA','Update','Update','Update','Update','View',0,'IEPF Docs Uploaded','1','26');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 3','Waiting Final Payment','Update','Update','Update','Update','Update','View',0,'Final payment Received','1','27');
insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 3','Waiting Closure','View','Update','Update','Update','Update','View',0,'Case Closed','1','28');