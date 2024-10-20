package com.nt.controller;

import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.service.IBenfitIssuranceService;

@RestController
@RequestMapping("/Bi-api")
public class BenfitIssuranceController {
	@Autowired
	private IBenfitIssuranceService service;
	@GetMapping("/send")
	public ResponseEntity<String> sendAmt() throws Exception
	{
		 JobExecution exe=service.sendAmtToBenfisury();
		return new ResponseEntity<String>(exe.getExitStatus().getExitDescription(),HttpStatus.OK);
	}

}
