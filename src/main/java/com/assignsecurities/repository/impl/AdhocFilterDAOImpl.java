package com.assignsecurities.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.domain.dm.AdhocFilterModel;
import com.assignsecurities.domain.dm.KeyValueModel;



@Repository
public class AdhocFilterDAOImpl  {

	private static final Logger logger = LogManager.getLogger(AdhocFilterDAOImpl.class);
//	@Autowired
//	private SessionFactory sessionFactory;

	public AdhocFilterModel getAdhocFilter(String code, UserLoginBean user) {

//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
//				AdhocFilterModel.class);
//		criteria.add(Restrictions.eq("code", code));
//		AdhocFilterModel adhocFilter = (AdhocFilterModel) criteria
//				.uniqueResult();
//		return adhocFilter;
		return null;
	}

	public List<KeyValueModel> getIdCode(AdhocFilterModel adhocFilterModel,
			UserLoginBean user) {
		Map<Long, String> idCode = new HashMap<Long, String>();
		String sql = adhocFilterModel.getQuery();
		//TODO for testing adding this SQL
//		sql= "SELECT CAST(id as CHAR(150)) as idKey ,LOGIN as idValue  FROM qas_users";
		logger.info("sql===="+sql);
//		sql=sql.toLowerCase();
//		if(sql.contains("@Org_Group_Id@")){
//			sql=sql.replaceAll("@Org_Group_Id@", user.getBranchId().toString());
//		}
		logger.info("Replace===="+sql);
//		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
//		query.setResultTransformer(new AliasToBeanResultTransformer(KeyValueModel.class));;
////				.addEntity(new AliasToBeanResultTransformer(KeyValueModel.class));
////				.setParameter("stockCode", "7277");
//		List<KeyValueModel> result = (List<KeyValueModel>)query.list();
//		System.out.println("Retrun successfull====");
//		return result;
		return null;
	}
}
