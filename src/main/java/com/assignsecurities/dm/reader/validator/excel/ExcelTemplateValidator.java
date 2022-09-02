package com.assignsecurities.dm.reader.validator.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.ResourceBundleUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.MetaDataConfigService;
import com.assignsecurities.dm.MetaDataConfigServiceImpl;
import com.assignsecurities.dm.reader.excel.ExcelTemplateSheetHandler;
import com.assignsecurities.dm.reader.validator.TemplateValidator;
import com.assignsecurities.domain.dm.AttributeConfigBean;
import com.assignsecurities.domain.dm.DataColumnModel;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.excel.ExcelDataColumnModel;
import com.assignsecurities.domain.dm.excel.ExcelDataRowModel;

/**
 * @author Narendra Chouhan
 * 
 */
@Service
public class ExcelTemplateValidator implements TemplateValidator {
	private OPCPackage pkg;
	private XSSFReader xssfReader;
	private SharedStringsTable sst;
	private StylesTable styles;
	private UserLoginBean user;
	private static final Logger logger = LogManager.getLogger(ExcelTemplateValidator.class);
	
	private @Autowired
	AutowireCapableBeanFactory beanFactory;
	
	private MetaDataConfigService metaDataConfigService;
	
	@Autowired
	private ResourceBundleUtil resourceBundleUtil;
	
	public String validate(Document doc, ObjectConfigBean objectConfigModel,
						   UserLoginBean user) throws ServiceException {
		this.user = user;
//		String ssTemplateMessage=resourceBundleUtil.getLabel("message.dm.template.unknownError", user.getLocale());
		String ssTemplateMessage="message.dm.template.unknownError";
		try{
			metaDataConfigService = new MetaDataConfigServiceImpl();
			beanFactory.autowireBean(metaDataConfigService);
			Map<String, ExcelDataRowModel> excelDataRowModelMap = new HashMap<String, ExcelDataRowModel>();
			XMLReader parser;
			ExcelTemplateSheetHandler handler;
			
			try {
//				File file = new File("C:/Support/eSupport/Student_TestData.xlsx");
//				InputStream inputStream= new FileInputStream(file);
				pkg = OPCPackage.open(doc.getImputStream());
				xssfReader = new XSSFReader(pkg);
				styles = xssfReader.getStylesTable();
				sst = xssfReader.getSharedStringsTable();
				handler = new ExcelTemplateSheetHandler(styles, sst,
						objectConfigModel);
	
				parser = fetchSheetParser(sst, handler);
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(),e);
				e.printStackTrace();
				return ssTemplateMessage;
			}
			try {
				readHeader(objectConfigModel, parser, true, excelDataRowModelMap);
			} catch (SAXException e) {
				logger.error(e.getLocalizedMessage(),e);
				return ssTemplateMessage;
			}
			ExcelDataRowModel excelDataRowModel = excelDataRowModelMap
			.get(String.valueOf(objectConfigModel
					.getTabIndex()));
			logger.debug("excelDataRowModelMap-->"+excelDataRowModelMap);
			boolean isValid=validateSheet(excelDataRowModel, objectConfigModel);
			
			if(!isValid){
				//TODO need to update this into DB.
				logger.debug(ssTemplateMessage);
				return ssTemplateMessage;
			}
			
			//Validate parent.
			for (AttributeConfigBean attributeConfigModel : objectConfigModel
					.getAttributeConfigModels()) {
				if (attributeConfigModel.getChildObjectId() != null && attributeConfigModel.getChildObjectId()>0) {
					ObjectConfigBean childObjectConfigModel = metaDataConfigService
							.getSpecificObjectMetaDataFromCache(
									attributeConfigModel.getChildObjectId(), user);
					excelDataRowModel = excelDataRowModelMap
							.get(String.valueOf(childObjectConfigModel
									.getTabIndex()));
					isValid=validateSheet(excelDataRowModel, childObjectConfigModel);
					if(!isValid){
						//TODO need to update this into DB.
						logger.debug("Other Tabs Tab ::"+" tab Index ::" + childObjectConfigModel
								.getTabIndex() +" ===  "+ssTemplateMessage);
						return ssTemplateMessage;
					}
				}
			}
			logger.debug("Template is valid");
		}catch (IllegalArgumentException ex){
			logger.error("Unknown error comes during template validation",ex);
			return ssTemplateMessage;
		}catch(Exception e){
			logger.error("Unknown error comes during template validation",e);
			return ssTemplateMessage;
		}
		return null;
	}
	private boolean validateSheet(ExcelDataRowModel excelDataRowModel,ObjectConfigBean sheetObjectConfigModel){
		boolean isValid=true;
		List<AttributeConfigBean> attributeConfigModels = sheetObjectConfigModel
		.getAttributeConfigModels();
		List<DataColumnModel> columnModels = excelDataRowModel.getColumnModels();
		ExcelDataColumnModel excelDataColumnModel=null;
		int curColIndex=1;
		
		for (AttributeConfigBean attributeConfigModel : attributeConfigModels) {
			excelDataColumnModel=getExcelDataColumnModel(curColIndex,columnModels);
			if(attributeConfigModel.getHeaderName() == null
					|| attributeConfigModel.getHeaderName().equals("")){
				break;
			}
//			System.out.println("************************************************");
//			System.out.println("attributeConfigModel.getHeaderName()-->"+attributeConfigModel.getHeaderName());
			
			if(excelDataColumnModel==null){
				isValid=false;
				break;
			}
//			System.out.println("excelDataColumnModel.getColName()-->"+excelDataColumnModel.getColValue());
//			System.out.println("************************************************");
			if (attributeConfigModel.getHeaderName() != null
					&& !attributeConfigModel.getHeaderName().equals(
							excelDataColumnModel.getColValue())) {
				isValid=false;
				break;
			}
			curColIndex++;
		}
		return isValid;
	}
	private ExcelDataColumnModel getExcelDataColumnModel(int curColIndex,
			List<DataColumnModel> columnModels) {
		for(DataColumnModel dataColumnModel : columnModels){
			if(dataColumnModel.getColIndex()==curColIndex){
				return (ExcelDataColumnModel)dataColumnModel;
			}
		}
		logger.debug("************************************************");
		logger.debug("columnModels-->"+columnModels);
		logger.debug("curColIndex-->"+curColIndex);
		logger.debug("************************************************");
		return null;
	}
	/**
	 * This method is responsible to populate Data load model after reading
	 * excel.
	 * 
	 * @param objectConfigModel
	 * @param parser
	 * @param isMainSheet
	 * @param excelDataRowModelMap
	 * @throws ServiceException
	 * @throws SAXException
	 */
	private void readHeader(ObjectConfigBean objectConfigModel,
			XMLReader parser, boolean isMainSheet,
			Map<String, ExcelDataRowModel> excelDataRowModelMap)
			throws ServiceException, SAXException {
		try {
			processOneSheet(parser, xssfReader,
					objectConfigModel.getTabIndex(), objectConfigModel);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		ExcelTemplateSheetHandler handler = (ExcelTemplateSheetHandler) parser
				.getContentHandler();
		List<ExcelTemplateSheetHandler> childSheetHandlers = new ArrayList<ExcelTemplateSheetHandler>();
		Set uniqueSet = new HashSet();
		logger.debug("objectConfigModel.getTabIndex()-->"+objectConfigModel.getTabIndex());
		excelDataRowModelMap.put(String
				.valueOf(objectConfigModel.getTabIndex()), handler
				.getDataRowModel());

		for (AttributeConfigBean attributeConfigModel : objectConfigModel
				.getAttributeConfigModels()) {
			if (attributeConfigModel.getChildObjectId() != null && attributeConfigModel.getChildObjectId()>0) {
//				MetaDataConfigService metaDataConfigService = new MetaDataConfigServiceImpl();
				ObjectConfigBean childObjectConfigModel = metaDataConfigService
						.getSpecificObjectMetaDataFromCache(
								attributeConfigModel.getChildObjectId(), user);
				if (uniqueSet.add(childObjectConfigModel.getTabIndex())) {
					ExcelTemplateSheetHandler handler1 = new ExcelTemplateSheetHandler(
							styles, sst, childObjectConfigModel);
					childSheetHandlers.add(handler1);
				}
			}
		}
		for (ExcelTemplateSheetHandler handler1 : childSheetHandlers) {
			parser = fetchSheetParser(sst, handler1);
			readHeader(handler1.getObjDefModels(), parser, false,
					excelDataRowModelMap);
		}

	}

	/**
	 * Process specific sheet
	 * 
	 * @param parser
	 * @param r
	 * @param sheetSequence
	 * @param objectConfigModel
	 * @throws Exception
	 */
	public void processOneSheet(XMLReader parser, XSSFReader r,
			int sheetSequence, ObjectConfigBean objectConfigModel)
			throws Exception {

		InputStream sheet = r.getSheet("rId" + sheetSequence);
		InputSource sheetSource = new InputSource(sheet);
		parser.parse(sheetSource);
		sheet.close();
	}

	/**
	 * Set ContentHandler class
	 * 
	 * @param sst
	 * @param handler
	 * @return
	 * @throws SAXException
	 */
	public XMLReader fetchSheetParser(SharedStringsTable sst,
			ContentHandler handler) throws SAXException {
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.apache.xerces.parsers.SAXParser");

		parser.setContentHandler(handler);

		return parser;
	}
}
