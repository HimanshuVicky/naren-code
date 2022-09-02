--liquibase formatted sql
--changeset {authorName}:{id}
delete from applicationscriptques;
delete from questioner;

insert into `questioner` (ID,Question,IsActive) values('1','Q.1 : Are you seeking shares in your name ?','1');
insert into `questioner` (ID,Question,IsActive) values('2','Q.1.1 : Are you the legal heir to the shareholder ?','1');
insert into `questioner` (ID,Question,IsActive) values('3','Q.1.2 : Are your shares worth less than 2 lakh Rupees ?','1');
insert into `questioner` (ID,Question,IsActive) values('4','Q.2 : Are their other joint holders ?','1');
insert into `questioner` (ID,Question,IsActive) values('5','Q.3 : Have you updated your KYC with RTA in recent past ?','1');
insert into `questioner` (ID,Question,IsActive) values('6','Q.4 : Does the name of the share holder differ on','1');
insert into `questioner` (ID,Question,IsActive) values('7','Q.5 : Have you lost your share certificate(s) ?','1');
insert into `questioner` (ID,Question,IsActive) values('8','Q.6 : Have you mistakenly all your dividends from the company in last 7 years ?','1');
