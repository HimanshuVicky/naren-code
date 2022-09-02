--liquibase formatted sql
--changeset {authorName}:{id}

CREATE OR REPLACE VIEW `VW_CaseAgreementFeeValue` AS 
select
	c.id CaseId,
	case
		when cfaf.FeeType = 'FixValue' then cfaf.FeeValue
		else (
		select
			sum(s.MarketPrice)*cfaf.FeeValue/100
			from script s
		inner join casescript cs on
			s.Id = cs.ScriptId
		where
			cs.CaseId = c.id)
	end FeeAmt
from
	`case` c
inner join casefee cfaf on
	cfaf.CaseId = c.Id
	and FeeFor = 'Agreement Fees';