package com.assignsecurities.bean.surepass;

import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpResponse {
	public Data data;
    public int status_code;
    public String message_code;
    public String message;
    public boolean success;
    
    public class Data{
        public String client_id;
        public boolean otp_sent;
        public boolean if_number;
        public boolean valid_aadhaar;
		public String getClient_id() {
			return client_id;
		}
		public void setClient_id(String client_id) {
			this.client_id = client_id;
		}
		public boolean isOtp_sent() {
			return otp_sent;
		}
		public void setOtp_sent(boolean otp_sent) {
			this.otp_sent = otp_sent;
		}
		public boolean isIf_number() {
			return if_number;
		}
		public void setIf_number(boolean if_number) {
			this.if_number = if_number;
		}
		public boolean isValid_aadhaar() {
			return valid_aadhaar;
		}
		public void setValid_aadhaar(boolean valid_aadhaar) {
			this.valid_aadhaar = valid_aadhaar;
		}
    }
    
    public String toString() {
	    return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
}
