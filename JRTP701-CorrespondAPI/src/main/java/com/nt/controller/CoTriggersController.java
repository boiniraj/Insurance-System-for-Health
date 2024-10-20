package com.nt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.bindings.CoSummary;
import com.nt.service.CoSummaryTriggers;

@RestController
@RequestMapping("/Triggers-api")
public class CoTriggersController {
	@Autowired
	private CoSummaryTriggers service;
	@PutMapping("/send-emails")
	public ResponseEntity<CoSummary> sendEmails() throws Exception
	{
		CoSummary summaryDetails=service.coTriggers();
		return new ResponseEntity<CoSummary>(summaryDetails,HttpStatus.CREATED);
	}

}
