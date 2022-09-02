package com.assignsecurities.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.*;



@Repository
public class EmailReposatory {
	@Autowired
	private NamedParameterJdbcTemplate template;

	@SuppressWarnings("unchecked")
	public void addEmail(EmailModel emailModel, UserLoginBean userLoginBean) {
		EmailBodyModel emailBody = emailModel.getEmailBody();
		List<EmailToModel> emailTos = emailModel.getEmailTos();

		if (Objects.isNull(emailBody) || Objects.isNull(emailBody.geteMailBoday())) {
			throw new ServiceException("Please provide the email boday.");
		}
		if (Objects.isNull(emailTos) || emailTos.isEmpty()) {
			throw new ServiceException("Please provide to email Id.");
		}

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();

		String query = "insert into `email` (`From_EMail`, `SUBJECT`, `comments`,  `CREATED_BY`, `MODIFIED_BY`) "
				+ " values(:fromEmail,:subject,:comment,:createdBy,:modifiedBy)";
		sqlParameterSource.addValue("fromEmail", emailModel.getFromEmail());
		sqlParameterSource.addValue("subject", emailModel.getSubject());
		sqlParameterSource.addValue("comment", emailModel.getComments());
		String createdBy = AppConstant.SYS_USER;
		if (userLoginBean.getMobileNo() != null)
		{
			createdBy = userLoginBean.getMobileNo();
		}
		sqlParameterSource.addValue("createdBy",
				Objects.isNull(emailModel.getCreatedBy()) ? createdBy : emailModel.getCreatedBy());
		sqlParameterSource.addValue("modifiedBy",
				Objects.isNull(emailModel.getModifiedBy()) ? createdBy : emailModel.getModifiedBy());
		template.update(query, sqlParameterSource, keyHolder);
		Long emaildIdKey = keyHolder.getKey().longValue();

		sqlParameterSource = new MapSqlParameterSource();
		query = "insert into `email_body` (`EMail_Id`, `EMail_body`) values(:emaildIdKey,:emailBody)";
		sqlParameterSource.addValue("emaildIdKey", emaildIdKey);
		sqlParameterSource.addValue("emailBody", emailBody.geteMailBoday());
		template.update(query, sqlParameterSource);

		query = "insert into `email_to` (`EMail_Id`, `EMail`) values(:emaildIdKey,:email)";
		List<Map<String, Object>> batchValueTos = new ArrayList<>(emailTos.size());
		emailTos.stream().forEach(emailTo -> {
			batchValueTos.add(new MapSqlParameterSource("emaildIdKey", emaildIdKey)
					.addValue("email", emailTo.getEmail()).getValues());
		});
		template.batchUpdate(query, batchValueTos.toArray(new Map[emailTos.size()]));

		List<EmailCcModel> emailCcs = emailModel.getEmailCcs();

		if (Objects.nonNull(emailCcs) && !emailCcs.isEmpty()) {
			query = "insert into `email_cc` (`EMail_Id`, `EMail`) values(:emaildIdKey,:email)";
			List<Map<String, Object>> batchValuesCcs = new ArrayList<>(emailCcs.size());
			emailCcs.stream().forEach(emailcc -> {
				batchValuesCcs.add(new MapSqlParameterSource("emaildIdKey", emaildIdKey)
						.addValue("email", emailcc.getEmail()).getValues());
			});
			template.batchUpdate(query, batchValuesCcs.toArray(new Map[emailCcs.size()]));
		}

		List<EmailBccModel> emailBccs = emailModel.getEmailBccs();
		if (Objects.nonNull(emailBccs) && !emailBccs.isEmpty()) {
			query = "insert into `email_bcc` (`EMail_Id`, `EMail`) values(:emaildIdKey,:email)";
			List<Map<String, Object>> batchValuesBccs = new ArrayList<>(emailBccs.size());
			emailBccs.stream().forEach(emailBcc -> {
				batchValuesBccs.add(new MapSqlParameterSource("emaildIdKey", emaildIdKey)
						.addValue("email", emailBcc.getEmail()).getValues());
			});
			template.batchUpdate(query, batchValuesBccs.toArray(new Map[emailBccs.size()]));
		}
		
		
		List<EmailAttachmentModel> emailAttachmentModels = emailModel.getAttachmentModels();
		if (Objects.nonNull(emailAttachmentModels) && !emailAttachmentModels.isEmpty()) {
			query = "insert into `email_attachment` (`EMail_Id`, `AttachmentDocId`) values(:emaildIdKey,:attachmentDocId)";
			List<Map<String, Object>> batchValuesBccs = new ArrayList<>(emailAttachmentModels.size());
			emailAttachmentModels.stream().forEach(attachmentDocIds -> {
				batchValuesBccs.add(new MapSqlParameterSource("emaildIdKey", emaildIdKey)
						.addValue("attachmentDocId", attachmentDocIds.getAttachmentDocId()).getValues());
			});
			template.batchUpdate(query, batchValuesBccs.toArray(new Map[emailAttachmentModels.size()]));
		}
	}

	public List<EmailModel> getAllPendingEmails(UserLogin userLoginBean) {
		try {
			String query = "select e.id,From_EMail,SUBJECT,comments,eb.EMail_body from email e inner join email_body eb on e.id= eb.EMail_Id \r\n"
					+ "where Status_Id =:statusId order by CREATED_BY desc";

			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("statusId", AppConstant.EMAIL_STATUS_PENDING);
			return template.query(query, sqlParameterSource, mapEmails());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public List<EmailAttachmentModel> getEmailAttachments(Long emailId, UserLogin userLoginBean) {
		try {
			String query = "select * from email_attachment where EMail_Id =:emailId";

			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
			sqlParameterSource.addValue("emailId", emailId);
			return template.query(query, sqlParameterSource, mapEmailAttachments());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	private RowMapper<EmailAttachmentModel> mapEmailAttachments() {
		return (rs, i) -> {
			EmailAttachmentModel emailModel = new EmailAttachmentModel();
			emailModel.setId(rs.getLong("Id"));
			emailModel.setEmailId(rs.getLong("EMail_Id"));
			emailModel.setAttachmentDocId(rs.getLong("AttachmentDocId"));
			return emailModel;
		};
	}

	private RowMapper<EmailModel> mapEmails() {
		return (rs, i) -> {
			EmailModel emailModel = new EmailModel();
			Long emailId = rs.getLong("Id");
			emailModel.setId(emailId);
			emailModel.setFromEmail(rs.getString("From_EMail"));
			emailModel.setSubject(rs.getString("SUBJECT"));
			emailModel.setComments(rs.getString("comments"));
			EmailBodyModel emailBody = new EmailBodyModel();
			emailBody.seteMailBoday(rs.getBytes("EMail_body"));
			emailModel.setEmailBody(emailBody);
			emailModel.setEmailTos(pupulateEmailTos(emailId));
			emailModel.setEmailCcs(pupulateEmailCcs(emailId));
			emailModel.setEmailBccs(pupulateEmailBccs(emailId));
			return emailModel;
		};
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<EmailBccModel> pupulateEmailBccs(Long emailId) {
		String query = "select * from email_bcc where EMail_Id=:emailId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("emailId", emailId);
		try {
			List<EmailBccModel> emailBccModels = template.query(query, sqlParameterSource,
					new BeanPropertyRowMapper(EmailBccModel.class));
			return emailBccModels;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<EmailCcModel> pupulateEmailCcs(Long emailId) {
		String query = "select * from email_cc where EMail_Id=:emailId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("emailId", emailId);
		try {
			List<EmailCcModel> emailCcModels = template.query(query, sqlParameterSource,
					new BeanPropertyRowMapper(EmailCcModel.class));
			return emailCcModels;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<EmailToModel> pupulateEmailTos(Long emailId) {
		String query = "select * from email_to where EMail_Id=:emailId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("emailId", emailId);
		try {
			List<EmailToModel> emailToModels = template.query(query, sqlParameterSource,
					new BeanPropertyRowMapper(EmailToModel.class));
			return emailToModels;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	public void updateMailStatus(EmailModel emailModel) {
		String orgformSQL = "update email set Status_Id=:status where id=:id";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		sqlParameterSource.addValue("status", emailModel.getStatusId());
		sqlParameterSource.addValue("id", emailModel.getId());
		template.update(orgformSQL, sqlParameterSource);
	}
	
	
	public int deleteByDocId(Long attachmentDocId) {
		if(Objects.isNull( attachmentDocId) || attachmentDocId<1) {
			return 0;
		}
		String query = "delete FROM email_attachment WHERE AttachmentDocId=:attachmentDocId";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("attachmentDocId", attachmentDocId);
		return template.update(query, sqlParameterSource);
	}
	
	public int deleteByDocIds(List<Long> attachmentDocIds) {
		
		String query = "delete FROM email_attachment WHERE AttachmentDocId in(:attachmentDocIds)";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("attachmentDocIds", attachmentDocIds);
		return template.update(query, sqlParameterSource);
	}
}
