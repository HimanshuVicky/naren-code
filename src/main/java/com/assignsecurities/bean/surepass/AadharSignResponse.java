package com.assignsecurities.bean.surepass;

public class AadharSignResponse {
    public Data data;
    public int status_code;
    public boolean success;
    public String message;
    public String message_code;
    
    
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage_code() {
		return message_code;
	}

	public void setMessage_code(String message_code) {
		this.message_code = message_code;
	}

	public class CertificateDetails{
	    public String name;
	    public String country;
	    public String state;
	    public String pin_code;
	    public String aaadhar_last_four_digits;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getPin_code() {
			return pin_code;
		}
		public void setPin_code(String pin_code) {
			this.pin_code = pin_code;
		}
		public String getAaadhar_last_four_digits() {
			return aaadhar_last_four_digits;
		}
		public void setAaadhar_last_four_digits(String aaadhar_last_four_digits) {
			this.aaadhar_last_four_digits = aaadhar_last_four_digits;
		}
	    
	    
	}

	public class NameMatch{
	    public String name;
	    public boolean should_name_match;
	    public boolean name_matched;
	    public String name_match_score;
	    public CertificateDetails certificate_details;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isShould_name_match() {
			return should_name_match;
		}
		public void setShould_name_match(boolean should_name_match) {
			this.should_name_match = should_name_match;
		}
		public boolean isName_matched() {
			return name_matched;
		}
		public void setName_matched(boolean name_matched) {
			this.name_matched = name_matched;
		}
		public String getName_match_score() {
			return name_match_score;
		}
		public void setName_match_score(String name_match_score) {
			this.name_match_score = name_match_score;
		}
		public CertificateDetails getCertificate_details() {
			return certificate_details;
		}
		public void setCertificate_details(CertificateDetails certificate_details) {
			this.certificate_details = certificate_details;
		}
	    
	    
	}

	public class Reports{
	    public NameMatch name_match;

		public NameMatch getName_match() {
			return name_match;
		}

		public void setName_match(NameMatch name_match) {
			this.name_match = name_match;
		}
	    
	    
	}

	public class Data{
	    public String client_id;
	    public Reports reports;
		public String getClient_id() {
			return client_id;
		}
		public void setClient_id(String client_id) {
			this.client_id = client_id;
		}
		public Reports getReports() {
			return reports;
		}
		public void setReports(Reports reports) {
			this.reports = reports;
		}
	    
	    
	}
}
