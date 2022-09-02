--liquibase formatted sql
--changeset {authorName}:{id}
insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('924','REFERAL_FRANCHISE_USER_REGISTRATION_SMS','REFERAL_FRANCHISE_USER_REGISTRATION_SMS','SMS','Dear {0}<br>Welcome to {1}<br>Your pin for login is {2}.');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('925','REFERAL_FRANCHISE_USER_ESIGNED_COMPLETED_SMS','REFERAL_FRANCHISE_USER_ESIGNED_COMPLETED_SMS','SMS','Dear {0}<br>Your pin for login is {1}.');

insert into `message_template` (`Id`, `code`, `NAME`, `TYPE`, `MESSAGE`) values('926','ADMIN_PENDING_ESIGNED_COMPLETED_SMS','ADMIN_PENDING_ESIGNED_COMPLETED_SMS','SMS','Dear {0}<br>Your pin for login is {1}.');