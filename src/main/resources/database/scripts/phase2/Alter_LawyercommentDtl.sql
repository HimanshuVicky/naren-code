--liquibase formatted sql
--changeset {authorName}:{id}

alter table lawyerCaseCommentsDtl Add Column lawyerName varchar(100);