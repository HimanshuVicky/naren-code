--liquibase formatted sql
--changeset {authorName}:{id}

ALTER TABLE `casescript`
ADD `PrimaryHolderGender` varchar(2) NULL AFTER `PrimaryCaseHolder`;

ALTER TABLE `casescript`
ADD `SecondayHolderGender` varchar(2) NULL AFTER `SecondayCaseHolder`;

ALTER TABLE `casescript`
ADD `PrimaryHolderAge` int(10) NULL AFTER `PrimaryHolderGender`;

ALTER TABLE `casescript`
ADD `SecondayHolderAge` int(10) NULL AFTER `SecondayHolderGender`;


ALTER TABLE `casescript`
ADD `PrimaryHolderFatherHusbandName` varchar(250) NULL AFTER `PrimaryHolderAge`;

ALTER TABLE `casescript`
ADD `SecondayHolderFatherHusbandName` varchar(250) NULL AFTER `SecondayHolderAge`;





