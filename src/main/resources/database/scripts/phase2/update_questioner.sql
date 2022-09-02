--liquibase formatted sql
--changeset {authorName}:{id}

UPDATE questioner SET Question = 'Q.1 : Are you transferring shares in your name ?' where Question='Q.1 : Are you seeking shares in your name ?';