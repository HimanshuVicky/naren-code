package com.assignsecurities.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NSEScriptDetailsModel {
	private Long id;
	private String symbol;
	private String series;
	private double open;
	private double high;
	private double low;
	private double close;
	private double last;
	private double prevClose;
	private double totTrdQty;
	private double totTrdVal;
	private LocalDateTime timeStamp;
	private double totalTrades;
	private String isIn;
}
