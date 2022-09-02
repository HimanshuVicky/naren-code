package com.assignsecurities.app.util;

/**
 * hold all constant value required in SelfSufficiency module
 * 
 * 
 */
public interface DMConstants {
	/**
	 * Define Object ID
	 */
	Long MA_OBJECT_ID_EXCEL = 1L;
	Long BT_OBJECT_ID_EXCEL = 2L;
	String ARRAY_TYPE_OBJECT = "ARRAY";
	String MAP_TYPE_OBJECT = "MAP";
	String SET_TYPE_OBJECT = "SET";
	String LIST_TYPE_OBJECT = "LIST";
	String VECTOR_TYPE_OBJECT = "VECTOR";

	String OBJECT_TYPE_MA = "1";
	String OBJECT_TYPE_BT = "2";

	String DOCUMENT_TYPE_EXCEL = "EXCEL";

	String VALIDATION_TYPE_UNKNOWN = "UNKNOWN";
	
	String VALIDATION_TYPE_BUSINESS = "Business";

	String VALIDATION_TYPE_BASIC = "Basic";
	String ACTION_TEXT = "ACTION";
	
	
	String DOCUMENT_TYPE_FORMAT_XLSX = "xlsx";
	String DOCUMENT_TYPE_FORMAT_XML = "xml";
	
	String ACTION_EDIT = "EDIT";
	
	String ACTION_ADD = "ADD";
	String FAIL_STATUS = "FAIL";
	String SUCESS_STATUS = "SUCCESS";
	
    String DOWNLOAD_TEMPLATE = "DOWNLOAD_TEMPLATE";
    
    Integer OBJECT_ID_RECEIPT = 3;
    Integer OBJECT_ID_EXPENSESHEET = 4;
    Integer OBJECT_ID_TIMESHEET = 5;
    
    String TYPE_OF_OBJ_RECEIPT = "receipts";
    String TYPE_OF_OBJ_EXPENSESHEET = "expensesheet";
    String TYPE_OF_OBJ_TIMESHEET = "timesheet";
    
    String FOR_SSDL = "ForSSDL";
    String SS_DL_IMPORT_ID = "SS_DL_IMPORT_ID";
    
    String HASH_ALL = "#ALL";
	String BUSINESS_ERROR = "BUSINESS_ERROR";
	
	 String HASH_DEFAULT = "#Default";

	 String HASH_REMAINING = "#Remaining";
	 
	 Long HASH_ALL_NUMBER = -1l;
	 Long HASH_DEFAULT_NUMBER = -2l;

	 Long HASH_REMAINING_NUMBER = -3l;
	 
	 String YES_KEY = "Y";
	 String NO_KEY = "N";
	 
	 public static final String LOGGED_IN_USER = "LoggedInUser";
		
		/**
		 * Login.
		 */
		public static final String LOGIN = "login";
		
		/**
		 * error.
		 */
		public static final String ERROR = "error";
		
		/**
		 * error.
		 */
		public static final String INDEX = "index";
		
		public static final int PHONE_FIELD_LENGTH = 20;

		public static final int NUM_RECORDS_PER_PAGE = 30;
		
		
		 // DM
	    public static final String IS_MAIN_OBJECT_Y = "Y";
	    public static final String IS_MAIN_OBJECT_N = "N";
	    public static final String ADD_OBJECT_ACTION = "ADD";
	    public static final String EDIT_OBJECT_ACTION = "EDIT";
	    
	    // Self-sufficiency file status constants
	    public static final long FILE_STATUS_SUCCESSFUL = 90793;
	    public static final long FILE_STATUS_UNSUCCESSFUL = 90794;
	    public static final long FILE_STATUS_PARTIAL_SUCCESSFUL = 90795;
	    public static final long FILE_STATUS_SCHEDULED = 90796;
	    public static final long FILE_STATUS_IN_PROCESS = 90792;
	    public static final long FILE_STATUS_FAILD_DUPLICATE = 90791;
	    
	    // DM file status constants for Recent Exported Files section
	    public static final long EXPORT_FILE_STATUS_COMPLETED = 90797;
	    public static final long EXPORT_FILE_STATUS_FAILED = 90798;
	    public static final long EXPORT_FILE_STATUS_IN_PROGRESS = 90799;
	    public static final long EXPORT_FILE_STATUS_SCHEDULED = 90804;
	    
	    public static final long FILE_STATUS_INVALID_TEMPLATE = 90805;
}