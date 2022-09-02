--liquibase formatted sql
--changeset {authorName}:{id}

update statusmaster set Status='Waiting RTA Response Stage2'
where Stage='Stage 2' and Status='Waiting RTA Response';

insert  into `statusmaster`(`Stage`,`Status`,`Customer`,`CustomerCare`,`Admin` ,`FranchiseOwner`,`Franchise User`,`Lawyer`,`AutoUpdate`,`Flag`,`IsActive`,TempOrder)
values('Stage 1 ','Waiting RTA Response Veirifcation','View','Update','Update','Update','Update','NA',1,'Waiting RTA Response Veirifcation','1','29');
