package com.assignsecurities.dm.processor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.app.util.ResourceBundleUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.*;
import com.assignsecurities.dm.writer.DownLoadDataWriter;
import com.assignsecurities.dm.writer.DownLoadDataWriterFactory;
import com.assignsecurities.domain.dm.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;



/**
 * 
 * @author Narendra chouhan
 * 
 */
public class DownloadDataProcessor {

	private @Autowired AutowireCapableBeanFactory beanFactory;

	private ObjectServiceImpl objectService;

	@Autowired
	private ResourceBundleUtil resourceBundleUtil;
	private static final Logger logger = LogManager.getLogger(DownloadDataProcessor.class);

	/**
	 * this is the main/important method of Self Sufficiency, this acutely work
	 * as a controller, controls the flow start form the reading the file,
	 * template validation, Basic validation, business validation and the
	 * business data processing
	 * 
	 *            imported file for the processing
	 * @throws ServiceException
	 *             for any error
	 */
	public void processData(ObjectExportModel exportModel, UserLoginBean user)
			throws ServiceException {
		// UserTransaction transaction = null;
		DataWriterModel dataWriterModel = null;
		try {
			SSFormater.init();
			MetaDataConfigService metaDataConfigService = getMetaDataConfigService();
			beanFactory.autowireBean(metaDataConfigService);
			ObjectModel objectModel = objectService.getObject(user,
					exportModel.getObjId());
			ObjectBean objectBean = getObjectBean(objectModel);
			Long parentSheetId = metaDataConfigService
					.getParentSheetConfigId(exportModel);

			ObjectConfigBean objectConfigModel = metaDataConfigService
					.getSpecificObjectMetaDataFromCache(parentSheetId, user);
			DownLoadBusinessDataProcessor downLoadBusinessDataProcessor = BusinessDataProcessorFactory
					.getDownloadDataProcessor(objectConfigModel);
			if(Objects.nonNull(downLoadBusinessDataProcessor)) {
				beanFactory.autowireBean(downLoadBusinessDataProcessor);
			}else {
				logger.error("Donwload Processor is not define.==>"+objectConfigModel.getDataDownLoadProcessorClassName());
				return;
			}
			List<Object> objects = downLoadBusinessDataProcessor
					.getBusinessDataToProcess(objectBean, objectConfigModel,
							user);
			Long startTime = System.currentTimeMillis();
			logger.info("objects.size()=>" + objects.size());
			
			if (objects != null && !objects.isEmpty()) {
				objectConfigModel
						.setInputFileFormat((exportModel.getFileFormat() == null) ? DMConstants.DOCUMENT_TYPE_FORMAT_XLSX
								: exportModel.getFileFormat());
				// getSelfSuffiencyService().deletePattialPartIds(exportModel,
				// user);
				int recCount = 0;
				Random randomGenerator = new Random();
				String fileName = "";
				if (objectConfigModel.getObjectName() == null) {
					fileName = "download" + randomGenerator.nextInt(100) + "-"
							+ System.currentTimeMillis();
				} else {
					fileName = objectConfigModel.getObjectName()
							+ randomGenerator.nextInt(100) + "-"
							+ System.currentTimeMillis();
				}
				// if(objectModel.getObjectPerExcel()==-1){
				dataWriterModel = DownLoadDataWriterFactory.getDataWriterModel(
						objectConfigModel, user,objectModel, resourceBundleUtil);
				((DownLoadDataWriter) dataWriterModel.getDataWriterObject())
						.setFileName(fileName);
				// }
				int size = objects.size() - 1;
				int objectCounter = 0;
				int filePartCounter = 0;

				for (int index = size; index >= 0; index--) {
					// transaction = JNDIService.getUserTransaction();
					// transaction.setTransactionTimeout(30*60*1000);
					// if (transaction.getStatus() == 0) {
					// transaction.commit();
					// }
					// transaction.setTransactionTimeout(30*60*1000);
					// transaction.begin();
					// if (/* objectModel.getObjectPerExcel()!=-1 &&
					// */objectCounter == 0) {
					// dataWriterModel = DownLoadDataWriterFactory
					// .getDataWriterModel(objectConfigModel, user,
					// resourceBundleUtil);
					// ((DownLoadDataWriter) dataWriterModel
					// .getDataWriterObject()).setFileName(fileName
					// + "_" + filePartCounter);
					// }
					Object obj = (Object) objects.remove(index);
					Long objId = null;

					if (objId instanceof Long) {
						objId = Long.valueOf(obj.toString());
					}

					obj = PropertyMapperService.getPropertyMapper()
							.disAssambleObject(obj, objectConfigModel, user,
									beanFactory);
					if (obj != null) {
						writeDataToFile(obj,objectModel, objectConfigModel, objectBean,
								dataWriterModel, recCount, user);
						recCount++;
						objectCounter++;
					}
					// LogService.logInfo("recCount===>"+recCount);
					// LogService.logInfo("objectCounter===>"+objectCounter);
					// if(objectModel.getObjectPerExcel()!=-1 &&
					// objectCounter>=objectModel.getObjectPerExcel()){
					// objectCounter=0;
					// ((DownLoadDataWriter)
					// dataWriterModel.getDataWriterObject())
					// .generateFile(objectModel.getFileName(), user,
					// objectModel, dataWriterModel);
					// dataWriterModel.distroyDataWriterObject(objectConfigModel);
					// dataWriterModel=null;
					// filePartCounter++;
					// }
					// if (objId != null) {
					// getSelfSuffiencyService()
					// .deleteTemporaryDownloadObject(objectModel,
					// user, objId);
					// }
				}
				if (/* objectModel.getObjectPerExcel()==-1 || */objectCounter > 0) {
					File file=null;
					try{
						file=((DownLoadDataWriter) dataWriterModel.getDataWriterObject())
								.generateFile(exportModel.getFileName(), user,
										objectBean, dataWriterModel);
						exportModel.setFile(FileUtils.readFileToByteArray(file));
						exportModel.setCompletionDate(new Date());
						exportModel.setStatusId(DMConstants.EXPORT_FILE_STATUS_COMPLETED);
						exportModel.setFileName(file.getName());
						objectService.updateExport(exportModel);
					}catch(Exception e){
						throw new ServiceException(e.getMessage(), e);
					}finally{
						file.delete();
						file=null;
					}
					
					dataWriterModel.distroyDataWriterObject();
					dataWriterModel = null;
				}
			} else {

				objectConfigModel
						.setInputFileFormat((exportModel.getFileFormat() == null) ? DMConstants.DOCUMENT_TYPE_FORMAT_XLSX
								: exportModel.getFileFormat());
				dataWriterModel = DownLoadDataWriterFactory
						.getDataWriterModel(objectConfigModel, user,objectModel,
								resourceBundleUtil);
				writeDataToFile(null,objectModel, objectConfigModel, objectBean,
						dataWriterModel, 0, user);
				File file=null;
				try{
					file=((DownLoadDataWriter) dataWriterModel.getDataWriterObject())
						.generateFile(exportModel.getFileName(), user,
								objectBean, dataWriterModel);
					exportModel.setFile(FileUtils.readFileToByteArray(file));
					exportModel.setCompletionDate(new Date());
					exportModel.setStatusId(DMConstants.EXPORT_FILE_STATUS_COMPLETED);
					exportModel.setFileName(file.getName());
					objectService.updateExport(exportModel);
				}catch(Exception e){
					throw new ServiceException(e.getMessage(), e);
				}finally{
					file.delete();
					file=null;
				}
				dataWriterModel.distroyDataWriterObject();
				dataWriterModel = null;
			}
			Long endTime = System.currentTimeMillis();
			logger.error( "Total time taken to Download:"
					+ (endTime - startTime) + " miliseconds");
		} catch (ServiceException pe) {
			// if(pe.getExceptionCode().equals(SPMErrorCode.EXCEL_MAX_ROW_LIMIT_EXCEED)){
			// LogService.logError(this, "Excceded the Eacel allowable range, "
			// +
			// "Change the configuration and download again using the script:" +
			// " e.g. UPDATE SPM_SS_DL_OBJ_DEF SET OBJECT_PER_FILE=10 WHERE CODE='MA';",
			// pe);
			// getSelfSuffiencyService().updateFailedStatus(objectModel, user);
			// objectModel.setStatus(""+SPMSysCode.EXPORT_FILE_STATUS_FAILED);
			// }else{
			// LogService.logError(this, pe.getMessage(), pe);
			// throw new CommandException(pe.getMessage(), pe);
			// }
			logger.error( pe.getMessage(), pe);
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		} finally {
			if(Objects.nonNull(dataWriterModel)) {
				try {
					dataWriterModel.distroyDataWriterObject();
				} catch (IOException e) {
					//Ignore
				}
			}
			SSFormater.destory();
		}
	}

	private ObjectBean getObjectBean(ObjectModel objectModel) {
		ObjectBean objectBean = new ObjectBean();
		BeanUtils
				.copyProperties(objectModel, objectBean, "objectTemplateModel");
		if (objectModel.getObjectTemplateModel() != null) {
			ObjectTemplateBean objectTemplateBean = new ObjectTemplateBean();
			BeanUtils.copyProperties(objectModel.getObjectTemplateModel(),
					objectTemplateBean);
			objectBean.setObjectTemplateBean(objectTemplateBean);
		}
		return objectBean;
	}

	/**
	 * write data to file
	 * 
	 * @param obj
	 * @param objectConfigModel
	 * @param objectModel
	 * @param dataWriterModel
	 * @param recCount
	 * @param user
	 * @throws ServiceException
	 */
	private void writeDataToFile(Object obj,ObjectModel objectModel,
			ObjectConfigBean objectConfigModel, ObjectBean objectBean,
			DataWriterModel dataWriterModel, int recCount, UserLoginBean user)
			throws ServiceException {
		((DownLoadDataWriter) dataWriterModel.getDataWriterObject())
				.writeDataToFile(obj,objectModel, objectConfigModel, objectBean,
						dataWriterModel, recCount, true, user);

	}

	private MetaDataConfigService getMetaDataConfigService()
			throws ServiceException {
		return new MetaDataConfigServiceImpl();
	}

}
