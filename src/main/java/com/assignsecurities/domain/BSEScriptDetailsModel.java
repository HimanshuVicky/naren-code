package com.assignsecurities.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BSEScriptDetailsModel {
	private Long id;
	private String scCode;
	private String scName;
	private String scGroup;
	private String scType;
	private double open;
	private double high;
	private double low;
	private double close;
	private double last;
	private double prevClose;
	private double noTrades;
	private double noOfShrs;
	private double netTurnOv;
	private String tdcloindi;
	private String isnCode;
	private LocalDateTime tradingDate;
	private String filler2;
	private String filler3;
}
