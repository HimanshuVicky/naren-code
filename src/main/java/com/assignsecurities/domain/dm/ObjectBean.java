package com.assignsecurities.domain.dm;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ObjectBean  implements Serializable{
	
	 public final static String OBJECT_ID = "id";
	 public final static String OBJECT_NAME = "name";
	 public final static String FILE_NAME = "fileName";
	 public final static String OBJECT_CODE = "code";
	 public final static String IMPORT_DATE = "importDate";
	 public final static String IMPORTED_BY = "importedBy";
	 public final static String STATUS = "status";
	 public final static String TYPE_OF_OBJ = "typeOfObj";
	 public final static String FILE_ID = "fileId";
	 public final static String COMPLETION_DATE = "completionDate";
	 public final static String OBJECT_PER_EXCEL = "objectPerExcel";
	 

	 public final static String SERACK_KEY = "searchKey";
	 public final static String SERACH_BY = "searchBy";
	 public final static String STATUS_FILTER = "statusFilter";
	 public final static String SHOW_MY_JOB_FILTER = "showMyJobsKey";

	private long id;
	
	private String name;
	
	private String fileName;
	
	private String code;
	
	private Date importDate;
	
	private String importedBy;
	private Long importedByUserId;
	private String status;
	private String typeOfObj;
	private long fileId;
	private long reTryFileId;
	private long objectId;
	private Date completionDate;
	private boolean showRefreshIcon;
	private long typeId;
	private String fileFormat;
	//s_ghule
	private String searchKey;
	private String searchBy;
	private String statusFilter;
	private boolean showMyJobsKey;
	private int parentSheetId;
	private int objectPerExcel=-1;
	
	private long errorRecCount=-1;
	private long totalRecCount=-1;
	
	private long objectDetailId;
	
	private String displayStatistics;
	
	private List<ExportFilePartBean> exportFilePartModels;
	
	private List<Long> searchIds;
	
	private String searchTypeOf;
	
	private ObjectTemplateBean objectTemplateBean;
	
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public boolean getShowMyJobsKey() {
		return showMyJobsKey;
	}
	public void setShowMyJobsKey(boolean showMyJobsKey) {
		this.showMyJobsKey = showMyJobsKey;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		if(name==null && typeOfObj!=null){
			name=typeOfObj;
		}
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the importDate
	 */
	public Date getImportDate() {
		return importDate;
	}
	/**
	 * @param importDate the importDate to set
	 */
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	/**
	 * @return the importedBy
	 */
	public String getImportedBy() {
		return importedBy;
	}
	/**
	 * @param importedBy the importedBy to set
	 */
	public void setImportedBy(String importedBy) {
		this.importedBy = importedBy;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the typeOfObj
	 */
	public String getTypeOfObj() {
		if(typeOfObj==null && name!=null){
			typeOfObj=name;
		}
		return typeOfObj;
	}
	/**
	 * @param typeOfObj the typeOfObj to set
	 */
	public void setTypeOfObj(String typeOfObj) {
		this.typeOfObj = typeOfObj;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	/**
	 * @return the reTryFileId
	 */
	public long getReTryFileId() {
		return reTryFileId;
	}
	/**
	 * @param reTryFileId the reTryFileId to set
	 */
	public void setReTryFileId(long reTryFileId) {
		this.reTryFileId = reTryFileId;
	}
	/**
	 * @return the objectId
	 */
	public long getObjectId() {
		return objectId;
	}
	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	/**
	 * @param searchKey the searchKey to set
	 */
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	/**
	 * @return the serach_key
	 */
	public String getSearchKey() {
		return searchKey;
	}
	/**
	 * @param searchBy the searchBy to set
	 */
	public void setSearchBy(String searchBy) {
		this.searchBy =searchBy;
	}
	/**
	 * @return the searchBy
	 */
	public String getSearchBy() {
		return searchBy;
	}
	/**
	 * @param statusFilter the statusFilter to set
	 */
	public void setStatusFilter(String statusFilter) {
		this.statusFilter = statusFilter;
	}
	/**
	 * @return the statusFilter
	 */
	public String getStatusFilter() {
		return statusFilter;
	}
	
	
	
	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {
		return completionDate;
	}
	/**
	 * @param completionDate the completionDate to set
	 */
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	public boolean isShowRefreshIcon() {
		return showRefreshIcon;
	}
	public void setShowRefreshIcon(boolean showRefreshIcon) {
		this.showRefreshIcon = showRefreshIcon;
	}
	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
	/**
	 * @return the parentSheetId
	 */
	public int getParentSheetId() {
		return parentSheetId;
	}
	/**
	 * @param parentSheetId the parentSheetId to set
	 */
	public void setParentSheetId(int parentSheetId) {
		this.parentSheetId = parentSheetId;
	}
	/**
	 * @return the errorRecCount
	 */
	public long getErrorRecCount() {
		return errorRecCount;
	}
	/**
	 * @param errorRecCount the errorRecCount to set
	 */
	public void setErrorRecCount(long errorRecCount) {
		this.errorRecCount = errorRecCount;
	}
	/**
	 * @return the totalRecCount
	 */
	public long getTotalRecCount() {
		return totalRecCount;
	}
	/**
	 * @param totalRecCount the totalRecCount to set
	 */
	public void setTotalRecCount(long totalRecCount) {
		this.totalRecCount = totalRecCount;
	}
	/**
	 * @return the displayStatistics
	 */
	public String getDisplayStatistics() {
		return displayStatistics;
	}
	/**
	 * @param displayStatistics the displayStatistics to set
	 */
	public void setDisplayStatistics(String displayStatistics) {
		this.displayStatistics = displayStatistics;
	}
	/**
	 * Return file extension
	 * @return
	 */
	public String getFileExt(){
		if(this.getFileFormat()==null || this.getFileFormat().trim().length()<=0){
			return ".xlsx";
		}
		return "."+this.getFileFormat();
	}
	public Long getImportedByUserId() {
		return importedByUserId;
	}
	public void setImportedByUserId(Long importedByUserId) {
		this.importedByUserId = importedByUserId;
	}
	/**
	 * @return the objectPerExcel
	 */
	public int getObjectPerExcel() {
		return objectPerExcel;
	}
	/**
	 * @param objectPerExcel the objectPerExcel to set
	 */
	public void setObjectPerExcel(int objectPerExcel) {
		this.objectPerExcel = objectPerExcel;
	}
	/**
	 * @return the exportFilePartModels
	 */
	public List<ExportFilePartBean> getExportFilePartModels() {
		return exportFilePartModels;
	}
	/**
	 * @param exportFilePartModels the exportFilePartModels to set
	 */
	public void setExportFilePartModels(
			List<ExportFilePartBean> exportFilePartModels) {
		this.exportFilePartModels = exportFilePartModels;
	}
	/**
	 * @return the objectDetailId
	 */
	public long getObjectDetailId() {
		return objectDetailId;
	}
	/**
	 * @param objectDetailId the objectDetailId to set
	 */
	public void setObjectDetailId(long objectDetailId) {
		this.objectDetailId = objectDetailId;
	}
	/**
	 * @return the searchIds
	 */
	public List<Long> getSearchIds() {
		return searchIds;
	}
	/**
	 * @param searchIds the searchIds to set
	 */
	public void setSearchIds(List<Long> searchIds) {
		this.searchIds = searchIds;
	}
	/**
	 * @return the searchTypeOf
	 */
	public String getSearchTypeOf() {
		return searchTypeOf;
	}
	/**
	 * @param searchTypeOf the searchTypeOf to set
	 */
	public void setSearchTypeOf(String searchTypeOf) {
		this.searchTypeOf = searchTypeOf;
	}
	/**
	 * @return the objectTemplateBean
	 */
	public ObjectTemplateBean getObjectTemplateBean() {
		return objectTemplateBean;
	}
	/**
	 * @param objectTemplateBean the objectTemplateBean to set
	 */
	public void setObjectTemplateBean(ObjectTemplateBean objectTemplateBean) {
		this.objectTemplateBean = objectTemplateBean;
	}
	
}
