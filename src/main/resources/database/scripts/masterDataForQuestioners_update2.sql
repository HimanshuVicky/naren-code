--liquibase formatted sql
--changeset {authorName}:{id}
UPDATE questioner SET Question= 'Q.4 : Does the name of the share holder differ on Share Certificate and PAN/Death Certificate ?' WHERE id = 6;
UPDATE questioner SET Question= 'Q.6 : Have you mistakenly missed ALL your dividends FROM the company IN the LAST 7 years ?' WHERE id = 8;
