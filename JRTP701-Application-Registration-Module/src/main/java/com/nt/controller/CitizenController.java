package com.nt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.bindings.CitizenAppRegistrationInputs;
import com.nt.service.CitizenAppRegisterImplclass;

@RestController
@RequestMapping("/citizen-api")
public class CitizenController 
{
	@Autowired
	private CitizenAppRegisterImplclass service;
	@PostMapping("/save")
	public ResponseEntity<String> registerCitizen(@RequestBody CitizenAppRegistrationInputs inputs)
	{ 
		try {
		 int appId=service.registerCitizenApplication(inputs);
		 if(appId>0)
		 {
			 return new ResponseEntity<String>("Citizen registered with Id"+appId,HttpStatus.CREATED);
		 }
		 else {
			 return new ResponseEntity<String>("invalid ssn the candiate must belongs to Ohio state::",HttpStatus.OK);
		 }
		}//end try
		catch(Exception e)
		{
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}//end catch
		
	}//save
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<String> deleteCitizen(@PathVariable Integer id)
	{ 
		try {
	           service.deleterUser(id);
			 return new ResponseEntity<String>("Citizen Deleted with Id"+id,HttpStatus.CREATED); 
		}//end try
		catch(Exception e)
		{
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}//end catch
		
	}

}
