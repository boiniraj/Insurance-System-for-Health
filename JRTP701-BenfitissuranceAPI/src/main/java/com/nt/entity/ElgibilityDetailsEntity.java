package com.nt.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="JRTP701_ELIGIBILITY_DETAIRMATIONS")
public class ElgibilityDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer edTrackId;
	@Column
	private Integer caseNo;
	@Column(length=30)
	private String  holderName;
	@Column
	private Long holderSsn;
	@Column(length=30)
	private String planName;
	@Column(length=30)
	private String planStatus;
	@Column
	private LocalDate startDate;
	@Column
	private LocalDate endDate;
	@Column
	private Double benficiaryAmt;
	@Column(length=20)
	private String bankName;
	@Column
	private Long accNo;
	@Column(length=30)
	private String denailReson;

}
