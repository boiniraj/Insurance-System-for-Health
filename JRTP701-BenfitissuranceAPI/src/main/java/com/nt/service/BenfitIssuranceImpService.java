package com.nt.service;

import java.util.Date;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class BenfitIssuranceImpService implements IBenfitIssuranceService{
	@Autowired
	private JobLauncher luncher; 
	@Autowired
	private Job job;

	@Override
	public JobExecution sendAmtToBenfisury() throws Exception {
		JobParameter<Date> parameter=new JobParameter<Date>(new Date(),Date.class);
		Map<String,JobParameter<?>> map=Map.of("param",parameter);
		JobParameters params=new JobParameters(map);
		JobExecution execution=luncher.run(job,params);
		System.out.println("Job Execute Status::"+execution.getExitStatus());
		return execution;
	}

}
