package com.assignsecurities.dm.reader.excel;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.MetaDataConfigService;
import com.assignsecurities.dm.MetaDataConfigServiceImpl;
import com.assignsecurities.dm.PropertyMapperService;
import com.assignsecurities.dm.reader.DocumentParser;
import com.assignsecurities.domain.dm.AttributeConfigBean;
import com.assignsecurities.domain.dm.DataLoadObjectModel;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.excel.ExcelDataLoadObjectModel;
import com.assignsecurities.domain.dm.excel.ExcelDataRowModel;



/**
 * This class is responsible to read excel document.
 *
 */
public class XlsxExcelParser implements DocumentParser {

	int countrows = 0;

	private OPCPackage pkg;
	private XSSFReader xssfReader;
	private SharedStringsTable sst;
	private StylesTable styles;
	private UserLoginBean user;
    @SuppressWarnings("unused")
	private boolean isError = false;
    
	private @Autowired AutowireCapableBeanFactory beanFactory;
    
    private MetaDataConfigService metaDataConfigService;
    
	private static final Logger logger = LogManager.getLogger(XlsxExcelParser.class);

	public List<DataLoadObjectModel> readData(
			ObjectConfigBean objectConfigModel, Document doc,
			UserLoginBean user) throws ServiceException {
		this.user = user;
		List<DataLoadObjectModel> dataLoadObjectModels = new ArrayList<DataLoadObjectModel>();
		XMLReader parser;
		SheetHandler handler;
		try {
			metaDataConfigService = new MetaDataConfigServiceImpl();
			beanFactory.autowireBean(metaDataConfigService);
			pkg = OPCPackage.open(doc.getImputStream());
			xssfReader = new XSSFReader(pkg);
			styles = xssfReader.getStylesTable();
			sst = xssfReader.getSharedStringsTable();
			handler = new SheetHandler(styles, sst, objectConfigModel, null,
					null,true, user);
			parser = fetchSheetParser(handler);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		try {
			prepareDataLoadModel(objectConfigModel, parser,
					dataLoadObjectModels, true, null, null);
		} catch (SAXException e) {
			throw new ServiceException(e);
		}
		
		return dataLoadObjectModels;
	}
	/**
	 * This method is responsible to populate Data load model after reading
	 * excel.
	 * 
	 * @param objectConfigModel
	 * @param parser
	 * @param dataLoadObjectModels
	 * @param isMainSheet
	 * @param dataLoadObjectModel
	 * @param excelDataRowModelMap
	 * @throws ServiceException
	 * @throws SAXException
	 */
	private void prepareDataLoadModel(ObjectConfigBean objectConfigModel,
									  XMLReader parser, List<DataLoadObjectModel> dataLoadObjectModels,
									  boolean isMainSheet, DataLoadObjectModel dataLoadObjectModel,
									  Map<String, ExcelDataRowModel> excelDataRowModelMap)
			throws ServiceException, SAXException {
		try {
			//System.out.println("objectConfigModel--->"+objectConfigModel.getTabOrFileName());
			processOneSheet(parser, xssfReader,
					objectConfigModel);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
		SheetHandler handler = (SheetHandler) parser.getContentHandler();
		int counter = 0;
		Map<String, ExcelDataRowModel> dataRowModels = handler
				.getInputMetaDataMap();
		//Iterator<String> boModelItr = handler.getExcelDataRowKeys().iterator();
		Iterator<Object> boModelItr = handler.getBoModels().iterator();
		List<SheetHandler> childSheetHandlers = new ArrayList<SheetHandler>();
		AttributeConfigBean parentAttributeConfigModel = handler
				.getParentAttributeConfigModel();
		Set<Integer> uniqueSet = new HashSet<Integer>();
		while (boModelItr.hasNext()) {
			/*String modelKey = boModelItr.next();
			Object model = dataRowModels.get(modelKey).getRowBOModel();*/
			Object model = boModelItr.next();
			String primaryKey;
			String parentRefKey;
			if(objectConfigModel.getTabIndex()!=-1){				
				try {
					primaryKey = getKeyForRow(
							objectConfigModel,
							dataRowModels.get(handler.getExcelDataRowKeys().get(
									counter)),isMainSheet);
					parentRefKey = getParentRefKeyForRow(
							objectConfigModel,handler.getParentDataRowModels(),
							dataRowModels.get(handler.getExcelDataRowKeys().get(
									counter)),isMainSheet);
				} catch (Exception e) {
					throw new ServiceException(e);
				}
				String primaryKeyTemp=parentRefKey;
				//System.out.println("parentRefKey====>"+parentRefKey);
				if(primaryKeyTemp.indexOf(":")>0){
					primaryKeyTemp=parentRefKey.substring(0,parentRefKey.lastIndexOf(":"));
				}
				//System.out.println("primaryKeyTemp====>"+primaryKeyTemp);
				//System.out.println("dataRowModels====>"+dataRowModels);
				ExcelDataRowModel excelDataRow = dataRowModels.get(primaryKeyTemp);
				if(excelDataRow==null){
					//System.out.println("handler.getExcelDataRowKeys().get(counter):::"+handler.getExcelDataRowKeys().get(counter));
					parentRefKey=handler.getExcelDataRowKeys().get(counter);
					excelDataRow = dataRowModels.get(parentRefKey);
				}
				if (isMainSheet) {
					dataLoadObjectModel = new ExcelDataLoadObjectModel();
					dataLoadObjectModel.setBusinessObjectModel(model);
					Object action = excelDataRow.getColumnModels().get(0).getColValue();
					if(action!=null){
						dataLoadObjectModel.setAction((action.toString().trim()).toUpperCase());
					}
					dataLoadObjectModels.add(dataLoadObjectModel);
					dataLoadObjectModel.addDataRow(primaryKey, excelDataRow);
				} else {
					ExcelDataRowModel parentDataRowModel = excelDataRowModelMap
							.get(primaryKeyTemp);
					if(parentDataRowModel==null){
						primaryKeyTemp=parentRefKey;
						if(parentRefKey.indexOf("::")>0){
							primaryKeyTemp=parentRefKey.substring(0,parentRefKey.indexOf("::"));
						}
						 parentDataRowModel = excelDataRowModelMap
							.get(primaryKeyTemp);
					}
					//System.out.println("Child Excel data row excelDataRow====>"+excelDataRow);
					//System.out.println("parentDataRowModel====>"+parentDataRowModel);
					if(parentDataRowModel!=null){
						parentDataRowModel.addChildRow(primaryKey, excelDataRow);
						if(parentAttributeConfigModel.getCollectionDataType()==null){
							Object obj=null;
							try {
								//System.out.println("parentDataRowModel.getRowBOModel()=========>"+parentDataRowModel.getRowBOModel());
								//System.out.println("parentAttributeConfigModel=========>"+parentAttributeConfigModel);
								obj=PropertyUtils.getProperty(parentDataRowModel.getRowBOModel(), parentAttributeConfigModel.getName());
								//System.out.println("****------------->"+obj+"<------------****");
								
							} catch (Exception e) {
								logger.error("Invlid data for the property : "
										+ parentAttributeConfigModel.getName()
										+ " Expected only one object, but has more than one for the parent model : "
										+ parentDataRowModel.getRowBOModel(), e);
								//ignore
							}
							if(obj!=null){
								String errorMessage = "Invlid data for the property : "
										+ parentAttributeConfigModel.getName()
										+ " Expected only one object, but has more than one for the parent model : "
										+ parentDataRowModel.getRowBOModel();
								//System.out.println(errorMessage + "%%%%%%%%%%%--->"+excelDataRow.getColumnModels().get(0).getColIndex() + " colValue:"+excelDataRow.getColumnModels().get(0).getColValue());
								excelDataRow.getColumnModels().get(0).setErrorMessage(errorMessage);
							}
						}
						PropertyMapperService.getPropertyMapper()
								.updateParentObjectAttribute(
										objectConfigModel,
										parentAttributeConfigModel, model,
										parentDataRowModel);
					}
				}
			}
			for (AttributeConfigBean attributeConfigModel : objectConfigModel
					.getAttributeConfigModels()) {
				if (attributeConfigModel.getChildObjectId() != null && attributeConfigModel.getChildObjectId()>0) {
//					MetaDataConfigService metaDataConfigService = new MetaDataConfigServiceImpl();
					ObjectConfigBean childObjectConfigModel = metaDataConfigService
							.getSpecificObjectMetaDataFromCache(
									attributeConfigModel.getChildObjectId(),
									user);
					if (uniqueSet.add(childObjectConfigModel.getTabIndex())) {
						SheetHandler handler1 = new SheetHandler(styles, sst,
								childObjectConfigModel, dataRowModels,
								attributeConfigModel, false,user);
						childSheetHandlers.add(handler1);
					}
					/**/
				}
			}
			counter++;
		}
		if(handler.isError()){
			isError=true;
		}
		for (SheetHandler handler1 : childSheetHandlers) {
			parser = fetchSheetParser(handler1);
			prepareDataLoadModel(handler1.getObjDefModels(),  parser,
					dataLoadObjectModels, false, dataLoadObjectModel,
					handler1.getParentDataRowModels());
		}

	}
	/**
	 * 
	 * @param objectConfigModel
	 * @param model
	 * @param isMainSheet 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private String getKeyForRow(ObjectConfigBean objectConfigModel,
			ExcelDataRowModel model, boolean isMainSheet) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String key = "";
		List<Integer> pkIndex = objectConfigModel.getPrimaryKeyIndex();
		for (int index = 0; index < pkIndex.size(); index++) {
			Integer keyIndex=(isMainSheet)?(pkIndex.get(index)): (pkIndex.get(index)-1);
			if(keyIndex>1 && isMainSheet){
				keyIndex=keyIndex-1;
			}
			key += model.getColumnModels().get(keyIndex)
					.getColValue()
					+ ((index == pkIndex.size() - 1) ? "" : ":");
		}
		return key+":"+objectConfigModel.getTabIndex();
	}
	/**
	 * 
	 * @param objectConfigModel
	 * @param parentDataRowModelMap 
	 * @param model
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private String getParentRefKeyForRow(ObjectConfigBean objectConfigModel,
			Map<String, ExcelDataRowModel> parentDataRowModelMap, ExcelDataRowModel model, boolean isMainSheet) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		String key = "";
		int parentTabIndex =0 ;
		if(parentDataRowModelMap!=null && !parentDataRowModelMap.isEmpty()){
			ExcelDataRowModel parentExcelDataRowModel = parentDataRowModelMap.values().iterator().next();
			parentTabIndex=parentExcelDataRowModel.getTabIndex();
		}
		List<Integer> pkIndex = objectConfigModel.getParentRefKeyIndex();
		for (int index = 0; index < pkIndex.size(); index++) {
			Integer keyIndex=(isMainSheet)?(pkIndex.get(index)): (pkIndex.get(index)-1);
			if(keyIndex>1 && isMainSheet){
				keyIndex=keyIndex-1;
			}
			key += model.getColumnModels().get(keyIndex)
					.getColValue()
					+ ((index == pkIndex.size() - 1) ? "" : ":");
		}
		return key+":"+parentTabIndex;
	}

	/**
	 * Process specific sheet
	 * 
	 * @param parser
	 * @param r
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void processOneSheet(XMLReader parser, XSSFReader r,
			ObjectConfigBean objectConfigModel) throws Exception {
		int sheetSequence=objectConfigModel.getTabIndex();
		if(sheetSequence>=15){
			int i=0;
			i++;
		}
		if(sheetSequence==-1){
			String businessObjectClassName=objectConfigModel.getBusinessObjectClassName();
			Class objectClass = Class.forName(businessObjectClassName);
			Object obj = objectClass.newInstance();
			SheetHandler handler = (SheetHandler) parser.getContentHandler();
			handler.getBoModels().add(obj);
		}else{
			InputStream sheet = r.getSheet("rId" + sheetSequence);
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
		}
	} 

	/**
	 * Associate  ContentHandler class to parser
	 * 
	 * @param handler
	 * @return
	 * @throws SAXException
	 */
	public XMLReader fetchSheetParser(ContentHandler handler) throws SAXException {
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.apache.xerces.parsers.SAXParser");
		parser.setContentHandler(handler);
		return parser;
	}

	
}
