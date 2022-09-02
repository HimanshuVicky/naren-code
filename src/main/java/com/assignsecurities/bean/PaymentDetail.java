package com.assignsecurities.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PaymentDetail {
	private String email;
    private String name;
    private String phone;
    private String productInfo;
    private String amount;
    private String txnId;
    private String hash;
    private String sUrl;
    private String fUrl;
    private String key;
}
