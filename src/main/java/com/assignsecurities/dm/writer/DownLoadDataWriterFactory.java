package com.assignsecurities.dm.writer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.app.util.ResourceBundleUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.writer.excel.ExcelDataWriter;
import com.assignsecurities.dm.writer.xml.XMLDataWriter;
import com.assignsecurities.domain.dm.DataWriterModel;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.ObjectModel;
import com.assignsecurities.domain.dm.excel.ExcelDataWriterModel;
import com.assignsecurities.domain.dm.xml.XMLDataWriterModel;



/**
 * Factory class responsible to provide DataWriterModel to support download.
 * This will do all required initialization base on the type of data file and it give it back to caller. 
 * This Writer model contains all necessary information require for download data. 
 * 
 */
public class DownLoadDataWriterFactory {
	private static final Map<String, DataWriterModel> downLoadDataWriterMap = new HashMap<String, DataWriterModel>();

	/**
	 * get DataWriterModel to download Objects
	 * 
	 * @param configModel
	 * @return
	 */
	public static DataWriterModel getDataWriterModel(
			ObjectConfigBean configModel, UserLoginBean user, ObjectModel objectModel, ResourceBundleUtil resourceBundleUtil) throws ServiceException {
		DataWriterModel dataWriterModel = downLoadDataWriterMap.remove(configModel
				.getInputFileFormat());
		if (dataWriterModel != null) {
			dataWriterModel.setDataWriterObject(new ExcelDataWriter(resourceBundleUtil));
			return dataWriterModel;
		}	
		if (DMConstants.DOCUMENT_TYPE_FORMAT_XLSX
				.equalsIgnoreCase(configModel.getInputFileFormat())) {
			dataWriterModel = new ExcelDataWriterModel();
			try {
//				Long object_id =null;
				//TODO
//				Long object_id = ObjectFactory.get().getObjectIdFromObjectCode(configModel.getObjectName());
				//TODO
//				ObjectTemplateModel objectTemplateModel=ObjectFactory.get().getObjectTemplateModel(object_id,SelfSufficiencyConstants.DOCUMENT_TYPE_FORMAT_XLSX); 
				InputStream ios =new ByteArrayInputStream( objectModel.getObjectTemplateModel().getTemplate());
				dataWriterModel
						.initDataWriterObject(configModel,ios);
				dataWriterModel.setDataWriterObject(new ExcelDataWriter(resourceBundleUtil));
//			} catch (IOException e) {
//				throw new ServiceException(e);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			downLoadDataWriterMap.put(configModel.getInputFileFormat(),
					dataWriterModel);
		}else if (DMConstants.DOCUMENT_TYPE_FORMAT_XML
				.equalsIgnoreCase(configModel.getInputFileFormat())) {
			dataWriterModel = new XMLDataWriterModel();
			try {
//				Long object_id =null;
//				Long object_id = ObjectFactory.get().getObjectIdFromObjectCode(configModel.getObjectName());
				//ObjectTemplateModel objectTemplateModel=ObjectFactory.get().getObjectTemplateModel(object_id,SelfSufficiencyConstants.DOCUMENT_TYPE_FORMAT_XML); 
				InputStream ios =new ByteArrayInputStream( objectModel.getObjectTemplateModel().getTemplate());
				dataWriterModel
						.initDataWriterObject(configModel,ios);
				dataWriterModel.setDataWriterObject(new XMLDataWriter(resourceBundleUtil));
			} catch (IOException e) {
				throw new ServiceException(e);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			downLoadDataWriterMap.put(configModel.getInputFileFormat(),
					dataWriterModel);
		}
		return dataWriterModel;
	}
}
