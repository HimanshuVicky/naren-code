package com.assignsecurities.domain.dm.xml;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.assignsecurities.domain.dm.DataWriterModel;
import com.assignsecurities.domain.dm.ObjectConfigBean;


public class XMLDataWriterModel implements DataWriterModel {

	private Object dataWriterObject = null;
	private InputStream inp = null;
	private StringBuilder xml = null;

	// public final ExcelDataWriter dataWriter = new ExcelDataWriter();

	public StringBuilder getXml() {
		return xml;
	}

	public void setXml(StringBuilder xml) {
		this.xml = xml;
	}

	// CreationHelper createHelper = wb.getCreationHelper();
	
	public void initDataWriterObject(ObjectConfigBean configModel,
									 InputStream fileIoInputStream) throws IOException,
			InvalidFormatException {
		xml = new StringBuilder();
	}

	
	public Object getDataWriterObject() {
		return dataWriterObject;
	}

	
	public void setDataWriterObject(Object dataWriterObject) {
		this.dataWriterObject = dataWriterObject;
	}

	
	public void distroyDataWriterObject()
			throws IOException {
		try {
			if (xml != null) {
				xml.delete(0, xml.length());
				xml = null;
			}
		} catch (Exception e) {
			// ignoure exception
		}
		if (inp != null) {
			inp.close();
		}

	}

}
