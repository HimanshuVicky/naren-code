package com.assignsecurities.dm.reader;

import java.util.HashMap;
import java.util.Map;

import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.dm.reader.excel.XlsxExcelParser;
import com.assignsecurities.dm.reader.xml.XMLDataReader;



/**
 * 
 *
 */
public class DocumentParserFactory {
	 @SuppressWarnings("unused")
	private static final String EXCEL_FILE_TYPE = "xls";
	 private static  final String XEXCEL_FILE_TYPE = "xlsx";
	@SuppressWarnings("unchecked")
	private static final Map<String,Class> documentParserMap = new HashMap<String, Class>();
	static{
		documentParserMap.put(XEXCEL_FILE_TYPE, XlsxExcelParser.class);
		documentParserMap.put(DMConstants.DOCUMENT_TYPE_FORMAT_XML, XMLDataReader.class);
	}
	
	@SuppressWarnings("unchecked")
	public static DocumentParser getDocumentParser(String fileType) throws IllegalAccessException, InstantiationException{
		Class cls = documentParserMap.get(fileType);
		if(cls!=null){
			return (DocumentParser) cls.newInstance();
		}
		return null;		
	}

}
