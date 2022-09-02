package com.assignsecurities.bean.surepass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsignInitializeData {
	 public Data data;
	    public int status_code;
	    public String message_code;
	    public String message;
	    public boolean success;
	    
	    public class Data{
	        public String client_id;
	        public Object group_id;
	        public String token;
	        public String url;
			public String getClient_id() {
				return client_id;
			}
			public void setClient_id(String client_id) {
				this.client_id = client_id;
			}
			public Object getGroup_id() {
				return group_id;
			}
			public void setGroup_id(Object group_id) {
				this.group_id = group_id;
			}
			public String getToken() {
				return token;
			}
			public void setToken(String token) {
				this.token = token;
			}
			public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}
	        
	    }
}
