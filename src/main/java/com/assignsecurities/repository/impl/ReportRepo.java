package com.assignsecurities.repository.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.assignsecurities.app.AppConstant;
import com.assignsecurities.app.util.ArgumentHelper;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.ApplicationUserModel;
import com.assignsecurities.domain.CaseReportModel;
import com.assignsecurities.domain.MyActionReferralModel;
import com.assignsecurities.domain.ReportModel;
import com.assignsecurities.domain.ScriptSearchLogModel;
import com.assignsecurities.domain.SearchReferralModel;
import com.assignsecurities.domain.SearchUserModel;

@Repository
public class ReportRepo {
	@Autowired
	NamedParameterJdbcTemplate template;
	
	@Autowired
	ApplicationUserRepo applicationUserRepo;
	
	private static String SCRIPT_LOG_REPORT_BASE_SQL = "select sl.*,ul.MobileNo,CONCAT(au.`FirstName`, ' ', au.`LastName`) as SearchByName,a.City from scriptsearchlog sl "
			+ "inner join  userlogin ul on sl.SearchBy=ul.Id "
			+ "inner join applicationuser au on au.id=ul.ApplicationUserId "
			+ "inner join address a on a.id = au.AddressId";
	
	private static String CASE_REPORT_BASE_SQL = "select  c.id,c.ReferenceNumber,CONCAT(c.`FirstName`, ' ', c.`LastName`) ClientName,s.CompanyName,ROUND(sum(s.MarketPrice),2) MarketPrice,\n" + 
			"  ROUND(cf.FeeValue,2) 'AgreementAmount',cf.FeeType\n" + 
			" from `case` c\n" + 
			" inner join casescript sc on sc.CaseId=c.id\n" + 
			" inner join script s on s.id=sc.ScriptId\n" + 
			" inner Join casefee cf on cf.CaseId=c.id and cf.FeeFor='Agreement Fees'\n" + 
			" inner join statusmaster sm on sm.Status=c.Status\n" + 
			" inner join  userlogin ul on c.UserId=ul.Id \n" + 
			" inner join applicationuser au on au.id=ul.ApplicationUserId \n" + 
			" inner join address a on a.id = au.AddressId  ";
	
	private static String CASE_USER_BASE_SQL ="select distinct au.UserId id,CONCAT(au.`FirstName`, ' ', au.`LastName`) 'ClientName',au.MobileNo 'ContactNumber',\n" + 
			"CASE WHEN a.id >0 THEN \"Y\"   ELSE \"N\" END ApplicationYes,\n" + 
			"CASE WHEN c.id >0 THEN \"Y\"   ELSE \"N\" END CaseYes, nonCustomerPin Pin, CASE WHEN IFNULL(f.Name,'')='' THEN \n" + 
			"			  ((select name from VW_Franchise_Name au1 where (auRef.UserId = au1.UserId Or au1.UserId=0)  order by id desc limit 1))\n" + 
			"		ELSE \n" + 
			"			f.Name \n" + 
			"		END franchiseName\n" + 
			"\n" + 
			",ad.City,au.UserType,auRef.ReferralName\n" + 
			"from VW_Application_Detls au\n" + 
			"inner join address ad on ad.id = au.AddressId\n" + 
			"left Join VW_Referral_Detls auRef on  (auRef.UserId = au.UserId)\n" + 
			"left join application a  on a.UserId=au.UserId\n" + 
			"left join `case` c  on c.UserId=au.UserId left join franchise f on f.Id=au.ReferalFranchiseId ";
	
	private static String REFERAL_BASE_SQL ="	select au.UserId,CONCAT(au.`FirstName`, ' ', au.`LastName`) 'ClientName',au.MobileNo 'ContactNumber',\n" + 
			"		CASE WHEN ((select IFNULL(a.id,0) from application a where a.UserId=au.UserId limit 1) >0 and IFNULL(c.id,0)<=0) THEN \"Application Created\"  \n" + 
			"		ELSE \n" + 
			"			CASE WHEN c.id >0 THEN \"Case Created\"   ELSE \"Registered\" END\n" + 
			"		END State, \n" + 
			"		ad.City, au.DateCreated,\n" + 
			"		auRef.ReferralName,\n" + 
			"		CASE WHEN IFNULL(f.Name,'')='' THEN \n" + 
			"			  ((select name from VW_Franchise_Name au1 where (auRef.UserId = au1.UserId Or au1.UserId=0)  order by id desc limit 1))\n" + 
			"		ELSE \n" + 
			"			f.Name \n" + 
			"		END FranchiseName, \n" + 
			"		ROUND(sum(( (select FeeVAlue from casefee where FeeFor='Processing Fees' and CaseId=c.id) * ccd.ReferralProcFeeComm /100)),2) 'Processing Commision Earned by RP',\n" + 
			"		ROUND(sum(( cav.FeeAmt * ccd.ReferralAgreFeeComm /100)),2) 'Agreement Commision Earned by RP',\n" + 
			"		ROUND(sum(( cav.FeeAmt * ccd.ReferralDocumentProcFeeComm /100)),2) 'Document Commission Earned by Frencise'\n" + 
			"	from VW_Application_Detls au \n" + 
			"	inner join address ad on ad.id = au.AddressId \n" + 
			"	inner Join VW_Referral_Detls auRef on  (auRef.UserId = au.UserId)\n" + 
			"	left join `case` c  on c.UserId=au.UserId\n" + 
			"	left join VW_CaseAgreementFeeValue cav on cav.CaseId =c.Id \n" + 
			"	left join casecommissiondtl ccd on ccd.CaseId =c.Id \n" + 
			"	left join franchise f on f.Id=au.ReferalFranchiseId\n" + 
			"	where au.UserType ='User' \n" ;
	 
		private static String REFERAL_GROUP_BY_SQL	=	" group by au.UserId,'ClientName', 'ContactNumber', State, ad.City, au.DateCreated, ReferralName,	FranchiseName ";
		
		
		private static String MY_ACTION_BASE_SQL ="select rcd.*, au.UserId ReferralUserId,f2.Name ReferralName,au.MobileNo ContactNumber,au.UserType from referralscommissiondtl rcd\n" + 
				"inner join VW_Application_Detls au on au.FranchiseId =rcd.FranchiseId and au.UserType ='Franchise'\n" + 
				"inner join franchise f2 on f2.Id = rcd.FranchiseId  where rcd.esignagreementstatus =1 " + 
				"union \n" + 
				"select rcd.*, au.UserId ReferralUserId,CONCAT(au.`FirstName`, ' ', au.`LastName`) ReferralName,au.MobileNo ContactNumber,au.UserType from referralscommissiondtl rcd\n" + 
				"inner join VW_Application_Detls au on au.UserId =rcd.UserId  where rcd.esignagreementstatus =1";

	public List<ScriptSearchLogModel> getScriptLogsReport(ReportModel searchModel, UserLoginBean userLoginBean) {
		String sql = SCRIPT_LOG_REPORT_BASE_SQL;
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		
		if (!AppConstant.USER_TYPE_ADMIN.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			sql = sql + " inner Join VW_Referral_Detls auRef on  (auRef.UserId = ul.Id) and auRef.UserId=:userId";
			sqlParameterSource.addValue("userId", userLoginBean.getId());
		}
		if (ArgumentHelper.isPositive(searchModel.getUserId())) {
			sql = sql + " where sl.SearchBy =:userId ";
			sqlParameterSource.addValue("userId", searchModel.getUserId());
		}

		if (ArgumentHelper.isValid(searchModel.getCity()) && !searchModel.getCity().equalsIgnoreCase("All")) {
			if (sql.contains("where")) {
				sql = sql + " and a.City =:city ";
			} else {
				sql = sql + " where a.City =:city ";
			}
			sqlParameterSource.addValue("city", searchModel.getCity());
		}

		if (Objects.nonNull(searchModel.getFromDate())) {
			if (sql.contains("where")) {
				sql = sql + " and sl.CreatedDate >=:fromDate ";
			} else {
				sql = sql + " where sl.CreatedDate >=:fromDate ";
			}
			sql = sql + " and sl.CreatedDate <=:toDate ";
			sqlParameterSource.addValue("fromDate", searchModel.getFromDate());
			sqlParameterSource.addValue("toDate", searchModel.getToDate());
		}
		sql = sql + " order by sl.CreatedDate desc";
		try {
			return template.query(sql, sqlParameterSource, mapObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	private RowMapper<ScriptSearchLogModel> mapObject() {
		return (result, i) -> {
			return ScriptSearchLogModel.builder().id(result.getLong("id")).firstName(result.getString("FirstName"))
					.middleName(result.getString("MiddleName")).lastName(result.getString("LastName"))
					.createdDate(Objects.isNull(result.getDate("CreatedDate")) ? null
							: result.getTimestamp("CreatedDate").toLocalDateTime())
					.searchBy(result.getLong("SearchBy")).searchByName(result.getString("SearchByName"))
					.mobileNo(result.getString("MobileNo")).city(result.getString("City")).totalScriptCost(result.getDouble("TotalScriptCost")) .build();
		};
	}
	public List<CaseReportModel> getCaseReport(ReportModel model, UserLoginBean userLoginBean) {
		String sql = CASE_REPORT_BASE_SQL;
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();

//		where sm.Stage ='Stage 1'
//				 and c.Status ='Waiting Franchise Assigment'
//				and a.City ='Pune' 
//				and c.FranchiseId=2
//				and c.AssignLawyerId=2
//				and s.id=69

		if (ArgumentHelper.isValid(model.getStage()) && !model.getStage().equalsIgnoreCase("All")) {
			if (sql.contains("where")) {
				sql = sql + " and sm.Stage =:stage ";
			} else {
				sql = sql + " where sm.Stage =:stage ";
			}
			sqlParameterSource.addValue("stage", model.getStage());
		}
		
		if (ArgumentHelper.isValid(model.getStatus()) && !model.getStatus().equalsIgnoreCase("All")) {
			if (sql.contains("where")) {
				sql = sql + " and c.Status =:status ";
			} else {
				sql = sql + " where c.Status =:status ";
			}
			sqlParameterSource.addValue("status", model.getStatus());
		}

		if (ArgumentHelper.isValid(model.getCity()) && !model.getCity().equalsIgnoreCase("All")) {
			if (sql.contains("where")) {
				sql = sql + " and a.City =:city ";
			} else {
				sql = sql + " where a.City =:city ";
			}
			sqlParameterSource.addValue("city", model.getCity());
		}
		if (ArgumentHelper.isPositive(model.getFranchiseId())) {
			if (sql.contains("where")) {
				sql = sql + " and c.FranchiseId =:franchiseId ";
			} else {
				sql = sql + " where c.FranchiseId =:franchiseId ";
			}
			sqlParameterSource.addValue("franchiseId", model.getFranchiseId());
		}
		
		if (ArgumentHelper.isPositive(model.getLawyerUserId())) {
			if (sql.contains("where")) {
				sql = sql + " and c.AssignLawyerId =:lawyerUserId ";
			} else {
				sql = sql + " where c.AssignLawyerId =:lawyerUserId ";
			}
			sqlParameterSource.addValue("lawyerUserId", model.getLawyerUserId());
		}
		
		if (ArgumentHelper.isValid(model.getCompanyName()) && !model.getCompanyName().equalsIgnoreCase("All")) {
			if (sql.contains("where")) {
				sql = sql + " and s.CompanyName  like :companyName ";
			} else {
				sql = sql + " where s.CompanyName like  :companyName ";
			}
			sqlParameterSource.addValue("companyName", "%"+model.getCompanyName()+"%");
		}

		if (Objects.nonNull(model.getFromDate())) {
			if (sql.contains("where")) {
				sql = sql + " and c.CreatedDate >=:fromDate ";
			} else {
				sql = sql + " where c.CreatedDate >=:fromDate ";
			}
			sql = sql + " and c.CreatedDate <=:toDate ";
			sqlParameterSource.addValue("fromDate", model.getFromDate());
			sqlParameterSource.addValue("toDate", model.getToDate());
		}
		if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
				|| AppConstant.USER_TYPE_FRANCHISE_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			if (sql.contains("where")) {
				sql = sql + "  and c.FranchiseId =:franchiseId ";
			} else {
				sql = sql + "  where c.FranchiseId =:franchiseId ";
			}
			sqlParameterSource.addValue("franchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
		}
		sql = sql + "group by  c.id,c.ReferenceNumber,c.`FirstName`, c.`LastName`, cf.FeeValue,cf.FeeType ";
		sql = sql + " order by c.CreatedDate desc";
		try {
			return template.query(sql, sqlParameterSource, mapCaseReportObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	private RowMapper<CaseReportModel> mapCaseReportObject() {
		return (result, i) -> {
			return CaseReportModel.builder().caseId(result.getLong("id")).clientName(result.getString("ClientName"))
					.scriptName(result.getString("CompanyName")).value(result.getDouble("MarketPrice"))
					.agreementValue(result.getDouble("AgreementAmount")).feeType(result.getString("feeType")).build();
		};
	}
	
	public List<SearchUserModel> getUserReport(ReportModel searchModel, UserLoginBean userLoginBean) {
		String sql = CASE_USER_BASE_SQL;
		sql = sql + " where au.UserType ='User' ";
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();

		if (ArgumentHelper.isPositive(searchModel.getUserId())) {
			sql = sql + " and au.ApplicationUserId =:userId ";
			sqlParameterSource.addValue("userId", searchModel.getUserId());
		}

		if (ArgumentHelper.isValid(searchModel.getCity()) && !searchModel.getCity().equalsIgnoreCase("All")) {
			if (sql.contains("where")) {
				sql = sql + " and ad.City =:city ";
			} else {
				sql = sql + " where ad.City =:city ";
			}
			sqlParameterSource.addValue("city", searchModel.getCity());
		}

		if (Objects.nonNull(searchModel.getFromDate())) {
			if (sql.contains("where")) {
				sql = sql + " and au.DateCreated >=:fromDate ";
			} else {
				sql = sql + " where au.DateCreated >=:fromDate ";
			}
			sql = sql + " and au.DateCreated <=:toDate ";
			sqlParameterSource.addValue("fromDate", searchModel.getFromDate());
			sqlParameterSource.addValue("toDate", searchModel.getToDate());
		}
		if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())
				|| AppConstant.USER_TYPE_FRANCHISE_USER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			if (sql.contains("where")) {
				sql = sql + "  and au.FranchiseId =:franchiseId ";
			} else {
				sql = sql + "  where au.FranchiseId =:franchiseId ";
			}
			sql = sql + " and au.UserType in('Franchise','FranchiseUser') ";
			sqlParameterSource.addValue("franchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
		}
		sql = sql + " order by au.DateCreated desc";
//		System.out.println("getUserReport:::=>"+sql);
		try {
			return template.query(sql, sqlParameterSource, mapUserReportObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	
	private RowMapper<SearchUserModel> mapUserReportObject() {
		return (result, i) -> {
			return SearchUserModel.builder().id(result.getLong("id")).clientName(result.getString("ClientName"))
					.contactNumber(result.getString("ContactNumber")).applicationYes(result.getString("ApplicationYes"))
					.caseYes(result.getString("CaseYes")).pin(result.getString("Pin")).franchiseName(result.getString("franchiseName"))
					.city(result.getString("City")).userType(result.getString("UserType")).referralName(result.getString("ReferralName")).build();
		};
	}
	
	
	public List<SearchReferralModel> getReferralReport(ReportModel searchModel, UserLoginBean userLoginBean) {
		String sql = REFERAL_BASE_SQL;
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();

		if (AppConstant.USER_TYPE_REFERRAL_PARTNER.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			sql = sql + " and auRef.ReferalUserId =:userId ";
			sqlParameterSource.addValue("userId", userLoginBean.getId());
		}else if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
//			sql = sql + "  and auRef.FranchiseId =:franchiseId ";
//			sqlParameterSource.addValue("userId", userLoginBean.getApplicationUserBean().getFranchiseId());
			sql = sql + " and (auRef.ReferalFranchiseId =:referalFranchiseId 	 )";
			sqlParameterSource.addValue("referalFranchiseId", userLoginBean.getApplicationUserBean().getFranchiseId());
		}
		
		if (ArgumentHelper.isValid(searchModel.getStage()) && !"all".equalsIgnoreCase(searchModel.getStage())) {
			sql = sql + " and  c.Status in(select Status from statusmaster where Stage=:stage)";
			sqlParameterSource.addValue("stage", searchModel.getStage());
		}
		
		
		if (ArgumentHelper.isPositive(searchModel.getReferralUserId())) {
//			sql = sql + " and ccd.ReferralUserId =:referralUserId ";
			ApplicationUserModel appUser=  applicationUserRepo.getApplicationUserByUserId(searchModel.getReferralUserId());
			 if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(appUser.getUserType())){
				 sql = sql + " and auRef.ReferalFranchiseId in(select FranchiseId from VW_Application_Detls where UserId =:referralUserId) and auRef.ReferalType='RF' ";
			 }else {
				 sql = sql + " and auRef.ReferalUserId in(:referralUserId) and auRef.ReferalType='RP' ";
			 }
//			sqlParameterSource.addValue("referralUserId", searchModel.getReferralUserId());
			sqlParameterSource.addValue("referralUserId", searchModel.getReferralUserId());
		}

		if (ArgumentHelper.isValid(searchModel.getCity()) && !searchModel.getCity().equalsIgnoreCase("All")) {
			sql = sql + " and ad.City =:city ";
			sqlParameterSource.addValue("city", searchModel.getCity());
		}

		if (Objects.nonNull(searchModel.getFromDate())) {
			sql = sql + " and au.DateCreated >=:fromDate ";
			sql = sql + " and au.DateCreated <=:toDate ";
			sqlParameterSource.addValue("fromDate", searchModel.getFromDate());
			sqlParameterSource.addValue("toDate", searchModel.getToDate());
		}

		sql = sql + REFERAL_GROUP_BY_SQL;
		
		String franchaiseSQl ="";
		
		if (AppConstant.USER_TYPE_FRANCHISE.equalsIgnoreCase(userLoginBean.getApplicationUserBean().getUserType())) {
			franchaiseSQl = franchaiseSQl + "select * from ( ";
			franchaiseSQl = franchaiseSQl + sql;
			franchaiseSQl = franchaiseSQl + " union ";
			franchaiseSQl = franchaiseSQl + sql.replace("(auRef.ReferalFranchiseId", "(c.FranchiseId");
			franchaiseSQl = franchaiseSQl + "  ) temp  order by DateCreated desc ";
			sql =franchaiseSQl;
		}else {
			sql = sql + " order by au.DateCreated desc";
		}
		try {
			return template.query(sql, sqlParameterSource, mapReferralReportObject());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	

	private RowMapper<SearchReferralModel> mapReferralReportObject() {
		return (result, i) -> {
			return SearchReferralModel.builder().id(result.getLong("UserId")).clientName(result.getString("ClientName"))
					.contactNumber(result.getString("ContactNumber")).state(result.getString("State"))
					.city(result.getString("City")).referralPartner(result.getString("ReferralName")).franchiseName(result.getString("FranchiseName"))
					.registeredDate(getRegDate(Objects.isNull(result.getDate("DateCreated")) ? null : result.getTimestamp("DateCreated").toLocalDateTime()))
					.processingFee(result.getDouble("Processing Commision Earned by RP")).aggrementFee(result.getDouble("Agreement Commision Earned by RP"))
					.documentationFee(result.getDouble("Document Commission Earned by Frencise"))
					.build();
		};
	}
	
	private String getRegDate(LocalDateTime registeredDate) {
		if(Objects.nonNull(registeredDate)) {
			return DateTimeFormatter.ofPattern(AppConstant.DD_MMM_YYYY).format(registeredDate);
		}
		return null;
	}
	
	
	public List<MyActionReferralModel> getMyActionReport(UserLoginBean userLoginBean) {
		String sql = MY_ACTION_BASE_SQL;
		MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
		try {
			return template.query(sql, sqlParameterSource, mapMyActionReferralModel());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}
	

	private RowMapper<MyActionReferralModel> mapMyActionReferralModel() {
		return (result, i) -> {
			return MyActionReferralModel.builder().clientName(result.getString("ReferralName"))
					.contactNumber(result.getString("ContactNumber")).franchiseId(result.getLong("FranchiseId")).userId(result.getLong("UserId"))
					.referralUserId(result.getLong("ReferralUserId")).feePaid(result.getBoolean("isRegfeereceived")?"Yes":"No")
					.feeAmount(result.getDouble("RegFee")).userType(result.getString("UserType")).status("Awaiting Admin eAadhar")
					.feeReferenceKey(result.getString("FeeReferenceKey"))
					.build();
		};
	}
	
	
}
