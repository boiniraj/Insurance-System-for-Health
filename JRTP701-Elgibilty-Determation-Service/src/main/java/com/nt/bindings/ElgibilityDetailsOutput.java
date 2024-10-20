package com.nt.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ElgibilityDetailsOutput {
	
	private Integer caseNo;
	private String  holderName;
	private String planName;
	private String planStatus;
	private LocalDate startDate;
	private LocalDate endDate;
	private Double benficiaryAmt;
	private String denailReson;
	private String bankName;
	private Long accNo;
}
