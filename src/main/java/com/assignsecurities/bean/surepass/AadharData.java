package com.assignsecurities.bean.surepass;

import java.util.List;

import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AadharData {
	public Data data;
	public int status_code;
	public boolean success;
	public Object message;
	public String message_code;
	
	public class FullName {
		public String value;
		public double confidence;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
	}

	public class Gender {
		public String value;
		public double confidence;
	}

	public class MotherName {
		public String value;
		public double confidence;
	}

	public class Dob {
		public String value;
		public double confidence;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
	}

	public class AadhaarNumber {
		public String value;
		public double confidence;
		public boolean is_masked;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public double getConfidence() {
			return confidence;
		}
		public void setConfidence(double confidence) {
			this.confidence = confidence;
		}
		public boolean isIs_masked() {
			return is_masked;
		}
		public void setIs_masked(boolean is_masked) {
			this.is_masked = is_masked;
		}
		
	}

	public class OcrField {
		public String document_type;
		public FullName full_name;
		public Gender gender;
		public MotherName mother_name;
		public Dob dob;
		public AadhaarNumber aadhaar_number;
		public Object image_url;
		public String uniqueness_id;
		public String getDocument_type() {
			return document_type;
		}
		public void setDocument_type(String document_type) {
			this.document_type = document_type;
		}
		public FullName getFull_name() {
			return full_name;
		}
		public void setFull_name(FullName full_name) {
			this.full_name = full_name;
		}
		public Gender getGender() {
			return gender;
		}
		public void setGender(Gender gender) {
			this.gender = gender;
		}
		public MotherName getMother_name() {
			return mother_name;
		}
		public void setMother_name(MotherName mother_name) {
			this.mother_name = mother_name;
		}
		public Dob getDob() {
			return dob;
		}
		public void setDob(Dob dob) {
			this.dob = dob;
		}
		public AadhaarNumber getAadhaar_number() {
			return aadhaar_number;
		}
		public void setAadhaar_number(AadhaarNumber aadhaar_number) {
			this.aadhaar_number = aadhaar_number;
		}
		public Object getImage_url() {
			return image_url;
		}
		public void setImage_url(Object image_url) {
			this.image_url = image_url;
		}
		public String getUniqueness_id() {
			return uniqueness_id;
		}
		public void setUniqueness_id(String uniqueness_id) {
			this.uniqueness_id = uniqueness_id;
		}
		
		
	}

	public class Data {
		public String client_id;
		public List<OcrField> ocr_fields;
		public String getClient_id() {
			return client_id;
		}
		public void setClient_id(String client_id) {
			this.client_id = client_id;
		}
		public List<OcrField> getOcr_fields() {
			return ocr_fields;
		}
		public void setOcr_fields(List<OcrField> ocr_fields) {
			this.ocr_fields = ocr_fields;
		}
		
	}
	public String toString() {
	    return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
}
