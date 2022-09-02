package com.assignsecurities.domain.dm;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * 
 *
 */
public interface DataWriterModel {

	public void initDataWriterObject(ObjectConfigBean configModel,
			InputStream fileIoInputStream) throws IOException,
			InvalidFormatException;

	public void distroyDataWriterObject()
			throws IOException;

	public Object getDataWriterObject();

	public void setDataWriterObject(Object dataWriterObject);

}
