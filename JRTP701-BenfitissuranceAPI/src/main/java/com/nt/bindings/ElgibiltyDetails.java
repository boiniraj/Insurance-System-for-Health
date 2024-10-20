package com.nt.bindings;


import lombok.Data;

@Data
public class ElgibiltyDetails {
	private Integer caseNo;
	
	private String  holderName;
	
	private Long holderSsn;
	
	private String planName;
	
	private String planStatus;
	
	private Double benficiaryAmt;
	private String bankName;
	private Long accNo;

}
