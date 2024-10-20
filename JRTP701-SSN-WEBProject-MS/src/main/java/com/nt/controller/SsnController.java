package com.nt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ssn-api")
public class SsnController
{
	@GetMapping("/find/{ssn}")
	public ResponseEntity<String> getSsnId (@PathVariable Integer ssn)
	{
		if(String.valueOf(ssn).length()!=9)
			return new ResponseEntity<String>("Invalid ssn",HttpStatus.BAD_REQUEST);
		
		int stateCode=ssn%100;
		String stateName=null;
		if(stateCode==01)
			stateName="WahingtonDc";
			else if(stateCode==02)
				 stateName="Ohio";
			else if(stateCode==03)
				stateName="Texas";
			else if(stateCode==4)
				stateName="California";
			else if(stateCode==5)
				stateName="florida";
			else 
				stateName="invalid ssn";
		return new ResponseEntity<String>(stateName,HttpStatus.OK);
	}
}
