--liquibase formatted sql
--changeset {authorName}:{id}

alter table `scriptsearchlog` Add Column TotalScriptCost double;
