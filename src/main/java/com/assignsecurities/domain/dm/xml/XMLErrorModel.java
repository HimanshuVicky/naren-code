package com.assignsecurities.domain.dm.xml;

import java.util.List;

public class XMLErrorModel {
	private String dataValue;
	private List<String> errorMessages;
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

}
