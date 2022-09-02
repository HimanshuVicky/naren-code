--liquibase formatted sql
--changeset {authorName}:{id}

insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 3','Close','View','View','View','View','View','View',0,'Case Closed','1','28');