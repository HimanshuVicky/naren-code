package com.assignsecurities.bean.surepass;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalPanData {
	public Data data;
    public int status_code;
    public String message_code;
    public Object message;
    public boolean success;
    
    public class PanNumber{
        public String value;
        public int confidence;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
        
    }

    public class FullName{
        public String value;
        public int confidence;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
        
    }

    public class FatherName{
        public String value;
        public int confidence;
    }

    public class Dob{
        public String value;
        public int confidence;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
        
    }

    public class OcrField{
        public String document_type;
        public PanNumber pan_number;
        public FullName full_name;
        public FatherName father_name;
        public Dob dob;
		public String getDocument_type() {
			return document_type;
		}
		public void setDocument_type(String document_type) {
			this.document_type = document_type;
		}
		public PanNumber getPan_number() {
			return pan_number;
		}
		public void setPan_number(PanNumber pan_number) {
			this.pan_number = pan_number;
		}
		public FullName getFull_name() {
			return full_name;
		}
		public void setFull_name(FullName full_name) {
			this.full_name = full_name;
		}
		public FatherName getFather_name() {
			return father_name;
		}
		public void setFather_name(FatherName father_name) {
			this.father_name = father_name;
		}
		public Dob getDob() {
			return dob;
		}
		public void setDob(Dob dob) {
			this.dob = dob;
		}
        
        
    }

    public class Data{
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
}
