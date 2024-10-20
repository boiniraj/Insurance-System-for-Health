package com.nt.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nt.bindings.CitizenAppRegistrationInputs;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.repository.CitizenRegistrationRepositoty;
@Service
public class CitizenAppRegisterImplclass implements ICitizenApplicationRegisterService {
 @Autowired
	private CitizenRegistrationRepositoty repository;
 @Autowired
 private RestTemplate template;
 @Value("${ar.ssn-web.url}")
 private String endUrl;
 @Value("${ar.state}")
 private String targetClass;
	
	@Override
	public Integer registerCitizenApplication(CitizenAppRegistrationInputs inputs)
	{
		ResponseEntity<String> response=template.exchange(endUrl,HttpMethod.GET,null,String.class,inputs.getSsn());
		String stateName=response.getBody();
	     if(stateName.equalsIgnoreCase(targetClass)) {
		CitizenAppRegistrationEntity master=new CitizenAppRegistrationEntity();
		BeanUtils.copyProperties(inputs, master);
		master.setStateName(stateName);
		        int appId=repository.save(master).getAppId();       
		return appId;
	}
	     return 0;
	}

	@Override
	public String deleterUser(Integer id) {
		Optional<CitizenAppRegistrationEntity> master =repository.findById(id);
		if(master.isPresent())
		{
		       repository.deleteById(id);
		       return +id+ ":delete succussfully" ;
		}
		return "delete not ";
		
	}

	
}
	





