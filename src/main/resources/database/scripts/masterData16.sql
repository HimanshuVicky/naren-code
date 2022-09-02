--liquibase formatted sql
--changeset {authorName}:{id}

delete from statemaster where State_Code='9' and State_Union_Territory='Rajasthan';