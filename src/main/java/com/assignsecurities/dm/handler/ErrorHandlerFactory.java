package com.assignsecurities.dm.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.assignsecurities.app.util.DMConstants;
import com.assignsecurities.dm.handler.excel.ExcelErrorHandler;
import com.assignsecurities.dm.reader.xml.XMLErrorHandler;

/**
 * This class is responsible to provide error handler class based on file type.
 * whenever any error comes processor calls this class to provide error handler object.And this
 * error handler object knows how to handle all different kind of error.
 *
 */
@Service
public class ErrorHandlerFactory {
	private static @Autowired AutowireCapableBeanFactory beanFactory;
	private static final Map<String, Class> errorHandlerMap = new HashMap<String, Class>();
	static {
		errorHandlerMap.put(DMConstants.DOCUMENT_TYPE_FORMAT_XLSX,
				ExcelErrorHandler.class);
		errorHandlerMap.put(DMConstants.DOCUMENT_TYPE_FORMAT_XML,
				XMLErrorHandler.class);
	}

	@SuppressWarnings("unchecked")
	public static ErrorHandler getErrorHandler(String fileType)
			throws IllegalAccessException, InstantiationException {
		Class cls = errorHandlerMap.get(fileType);
		if (cls != null) {
			ErrorHandler errorHandler=(ErrorHandler) cls.newInstance();
//			beanFactory.autowireBean(errorHandler);
			return errorHandler;
		}
		return null;
	}
}
