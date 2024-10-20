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
@Table(name="JRTP701_PLAN_ENTITY")
public class PlanEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer PlanId;
	@Column(length=40)
	private String PlanName;
	@Column
	private LocalDate startDate;
	@Column
	private LocalDate endDate;
	@Column
	private String description;
	@Column(length=20)
	private String activeSw;
}
