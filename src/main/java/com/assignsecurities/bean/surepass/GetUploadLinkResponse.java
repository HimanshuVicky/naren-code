package com.assignsecurities.bean.surepass;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUploadLinkResponse {
	public Data data;
	public int status_code;
	public String message_code;
	public String message;
	public boolean success;

	public class Data {
		public boolean link_generated;
		public String url;
		public Fields fields;
		public boolean isLink_generated() {
			return link_generated;
		}
		public void setLink_generated(boolean link_generated) {
			this.link_generated = link_generated;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public Fields getFields() {
			return fields;
		}
		public void setFields(Fields fields) {
			this.fields = fields;
		}
		
		
	}

	public class Fields {
		public String key;
		@JsonProperty("x-amz-algorithm")
		@SerializedName("x-amz-algorithm")
		public String xAmzAlgorithm;
		@JsonProperty("x-amz-credential")
		@SerializedName("x-amz-credential")
		public String xAmzCredential;
		@JsonProperty("x-amz-date")
		@SerializedName("x-amz-date")
		public String xAmzDate;
		public String policy;
		@JsonProperty("x-amz-signature")
		@SerializedName("x-amz-signature")
		public String xAmzSignature;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getxAmzAlgorithm() {
			return xAmzAlgorithm;
		}
		public void setxAmzAlgorithm(String xAmzAlgorithm) {
			this.xAmzAlgorithm = xAmzAlgorithm;
		}
		public String getxAmzCredential() {
			return xAmzCredential;
		}
		public void setxAmzCredential(String xAmzCredential) {
			this.xAmzCredential = xAmzCredential;
		}
		public String getxAmzDate() {
			return xAmzDate;
		}
		public void setxAmzDate(String xAmzDate) {
			this.xAmzDate = xAmzDate;
		}
		public String getPolicy() {
			return policy;
		}
		public void setPolicy(String policy) {
			this.policy = policy;
		}
		public String getxAmzSignature() {
			return xAmzSignature;
		}
		public void setxAmzSignature(String xAmzSignature) {
			this.xAmzSignature = xAmzSignature;
		}
		
		
	}

}
