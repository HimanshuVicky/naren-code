package com.assignsecurities.dm.processor;

import java.util.HashMap;
import java.util.Map;

import com.assignsecurities.app.util.StringUtil;
import com.assignsecurities.domain.dm.ObjectConfigBean;


public class BusinessDataProcessorFactory {
	private static final Map<String, Class> businessDataProcessorMap = new HashMap<String, Class>();

	private static final Map<String, Class> downLoadDataProcessorMap = new HashMap<String, Class>();
	
	/**
	 * 
	 * @param objectConfigModel
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static BusinessDataProcessor getBusinessDataProcessor(
			ObjectConfigBean objectConfigModel) throws IllegalAccessException,
			InstantiationException {
		String templateProcessorKey = String.valueOf(objectConfigModel.getId());
		if (businessDataProcessorMap.containsKey(templateProcessorKey
				.toUpperCase())) {
			Class cls = businessDataProcessorMap.get(templateProcessorKey);
			if (cls != null) {
				return (BusinessDataProcessor) cls.newInstance();
			}
		}else if(StringUtil.isValidString(objectConfigModel.getBusinessProcesserClassName())){
			try {
				Class cls = Class.forName(objectConfigModel.getBusinessProcesserClassName());
				businessDataProcessorMap.put(String.valueOf(objectConfigModel.getId()),cls);
				return (BusinessDataProcessor) cls.newInstance();
			} catch (ClassNotFoundException e) {
				return null;
			}
				
	}
		return null;
	}
	/**
	 * Return data Load Business data processor instance
	 * @param objectConfigModel
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static DownLoadBusinessDataProcessor getDownloadDataProcessor(
			ObjectConfigBean objectConfigModel) throws IllegalAccessException,
			InstantiationException {
		if (downLoadDataProcessorMap.containsKey(String.valueOf(objectConfigModel.getId()))) {
			Class cls = downLoadDataProcessorMap.get(String.valueOf(objectConfigModel.getId()));
			if (cls != null) {
				return (DownLoadBusinessDataProcessor) cls.newInstance();
			}
		}else if(StringUtil.isValidString(objectConfigModel.getDataDownLoadProcessorClassName())){
				try {
					Class cls = Class.forName(objectConfigModel.getDataDownLoadProcessorClassName());
					downLoadDataProcessorMap.put(String.valueOf(objectConfigModel.getId()),cls);
					return (DownLoadBusinessDataProcessor) cls.newInstance();
				} catch (ClassNotFoundException e) {
					return null;
				}
					
		}
		return new DefaultBusinessDataProcessor();
	}
}
