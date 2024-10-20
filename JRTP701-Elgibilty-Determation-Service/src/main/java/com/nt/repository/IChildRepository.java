package com.nt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.DcChildEntity;

public interface IChildRepository extends JpaRepository<DcChildEntity,Integer> {
	public List< DcChildEntity> findByCaseNo(int caseNo);

}
