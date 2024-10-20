package com.nt.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ChildInputs {
	private Integer ssn;
	private LocalDate dob;
	private Integer caseNo;
}
