--liquibase formatted sql
--changeset {authorName}:{id}
update rtatemplates set TemplateType='RTA Letter' where TemplateType='RTA Latter';