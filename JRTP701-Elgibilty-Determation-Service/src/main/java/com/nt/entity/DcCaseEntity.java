package com.nt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="JRTP701_CASE_ENTITY")
public class DcCaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer caseNo;
	@Column
	private Integer appId;
	@Column
	private Integer PlanId;

}
