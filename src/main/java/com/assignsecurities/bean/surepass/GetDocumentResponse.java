package com.assignsecurities.bean.surepass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetDocumentResponse {
	private Data data;
	private float status_code;
	private String message_code;
	private String message;
	private boolean success;

	public class Data {
		private String url;

		// Getter Methods

		public String getUrl() {
			return url;
		}

		// Setter Methods

		public void setUrl(String url) {
			this.url = url;
		}
	}
}
