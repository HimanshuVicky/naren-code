package com.assignsecurities.bean.surepass;

public class ChequeDetails {
	public Object message;
	public boolean success;
	public int status_code;
	public Data data;
	public String message_code;

	public class Micr {
		public String value;
		public int confidence;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public int getConfidence() {
			return confidence;
		}

		public void setConfidence(int confidence) {
			this.confidence = confidence;
		}

	}

	public class AccountNumber {
		public String value;
		public int confidence;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public int getConfidence() {
			return confidence;
		}

		public void setConfidence(int confidence) {
			this.confidence = confidence;
		}

	}

	public class IfscCode {
		public String value;
		public int confidence;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public int getConfidence() {
			return confidence;
		}

		public void setConfidence(int confidence) {
			this.confidence = confidence;
		}

	}

	public class Data {
		public String client_id;
		public Micr micr;
		public AccountNumber account_number;
		public IfscCode ifsc_code;

		public String getClient_id() {
			return client_id;
		}

		public void setClient_id(String client_id) {
			this.client_id = client_id;
		}

		public Micr getMicr() {
			return micr;
		}

		public void setMicr(Micr micr) {
			this.micr = micr;
		}

		public AccountNumber getAccount_number() {
			return account_number;
		}

		public void setAccount_number(AccountNumber account_number) {
			this.account_number = account_number;
		}

		public IfscCode getIfsc_code() {
			return ifsc_code;
		}

		public void setIfsc_code(IfscCode ifsc_code) {
			this.ifsc_code = ifsc_code;
		}

	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getMessage_code() {
		return message_code;
	}

	public void setMessage_code(String message_code) {
		this.message_code = message_code;
	}

}
