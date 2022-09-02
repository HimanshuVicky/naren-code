package com.assignsecurities.app.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JTableJSONResponse<T> {
	
    @JsonProperty("Result")
    private String result;
    
    @JsonProperty("Record")
    private Object record;

    @JsonProperty("Records")
    private List<T> records;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("TotalRecordCount")
    private int totalRecordCount;

    public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}

	/**
	 * @return the record
	 */
	public Object getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(Object record) {
		this.record = record;
	}

	public JTableJSONResponse(String result, List<T> records, int totalRecordCount) {
        super();
        this.result = result;
        this.records = records;
        this.totalRecordCount = totalRecordCount;
    }
	public JTableJSONResponse(String result, String message) {
        super();
        this.result = result;
        this.message = message;       
    }
	public JTableJSONResponse(String result, Object record) {
        super();
        this.result = result;
        this.record = record;       
    }
	
	
}