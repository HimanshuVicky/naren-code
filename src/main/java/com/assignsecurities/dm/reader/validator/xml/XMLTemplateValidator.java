package com.assignsecurities.dm.reader.validator.xml;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.rmi.RemoteException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.assignsecurities.app.exception.ServiceException;
import com.assignsecurities.app.util.ResourceBundleUtil;
import com.assignsecurities.bean.UserLoginBean;
import com.assignsecurities.dm.reader.validator.TemplateValidator;
import com.assignsecurities.dm.writer.xml.XMLDataWriter;
import com.assignsecurities.domain.dm.Document;
import com.assignsecurities.domain.dm.ObjectConfigBean;
import com.assignsecurities.domain.dm.ObjectTemplateBean;



/**
 * This is used to validate xml structure. In case of syntax error this will
 * return error message.
 * 
 * 
 */
public class XMLTemplateValidator implements TemplateValidator {
	private StringBuilder errorMessage = new StringBuilder();
	
	private ResourceBundleUtil resourceBundleUtil;

	public XMLTemplateValidator(ResourceBundleUtil resourceBundleUtil){
		
	}
	public String validate(Document doc, ObjectConfigBean objectConfigModel, UserLoginBean user) throws ServiceException {
		// SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// SAXParser parser = factory.newSAXParser();
			// XMLReader xmlReader = parser.getXMLReader();
			// ErrorHandler errorHandler = new XMLErrorHandler();
			// xmlReader.setErrorHandler(errorHandler);
			// parser.parse(doc.getImputStream(), new DefaultHandler());
			XMLDataWriter dataWriter = new XMLDataWriter(resourceBundleUtil);
			// System.out.println(dataWriter.getXSD(objectConfigModel, "", true,
			// user));
			InputStream inputStreamXSD = new ByteArrayInputStream(
					dataWriter.getXSD(objectConfigModel, "", true, user).getBytes());
			// InputStream inputStreamXSD =
			// getTheInternalTemplateInputStream(doc,user);
			validateAgainstXSD(doc.getImputStream(), inputStreamXSD);
		} catch (Exception e) {
			errorMessage.append("Error:" + e.getMessage());
		}
		return (errorMessage.length() > 0) ? errorMessage.toString() : null;
	}

	public boolean validateAgainstXSD(InputStream xml, InputStream xsd) {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.XML_NS_URI);
			Schema schema = factory.newSchema(new StreamSource(xsd));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xml));
			return true;
		} catch (Exception ex) {
			errorMessage.append("Error:" + ex.getMessage());
			return false;
		}
	}

	/**
	 * This method used to get the template and gives the input stream
	 * 
	 * @param doc
	 *            current doc object
	 * @return input stream
	 * @throws RemoteException
	 */
	public InputStream getTheInternalTemplateInputStream(Document doc, UserLoginBean user) throws ServiceException {
		// doc.getFileType();
		ObjectTemplateBean objectTemplateModel = null;
		// objectTemplateModel = ObjectFactory.get()
		// .getObjectInternalTemplateModel(doc.getObjectId(),SelfSufficiencyConstants.DOCUMENT_TYPE_FORMAT_XML);

		// InputStream ios = FileUploadDAOFactory.getDAO().getFileStream(user,
		// objectTemplateModel.getFileID());

		return null;
	}

	public static void main(String[] args) {
		try {
			FileInputStream fileInputStreamxml = new FileInputStream("c:\\masterAgreement.xml");
			FileInputStream fileInputStreamXSD = new FileInputStream("c:\\ma.xsd");
			XMLTemplateValidator templateValidator = new XMLTemplateValidator(new ResourceBundleUtil());
			templateValidator.validateAgainstXSD(fileInputStreamxml, fileInputStreamXSD);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class XMLErrorHandler implements ErrorHandler {
		public void warning(SAXParseException e) throws SAXException {
			errorMessage.append("Warning:" + e.getMessage());
		}

		public void error(SAXParseException e) throws SAXException {
			errorMessage.append("Error:" + e.getMessage());
		}

		public void fatalError(SAXParseException e) throws SAXException {
			errorMessage.append("FatalError:" + e.getMessage());
		}
	}

}
