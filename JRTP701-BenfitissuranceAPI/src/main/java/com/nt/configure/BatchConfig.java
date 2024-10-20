package com.nt.configure;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.nt.bindings.ElgibiltyDetails;
import com.nt.entity.ElgibilityDetailsEntity;
import com.nt.processor.EdProcessor;
import com.nt.repository.IElgibilityDeterminationRepository;

@Configuration
public class BatchConfig  {
	@Autowired
	private EdProcessor process;
	@Autowired
	private IElgibilityDeterminationRepository elgRepo;
	@Bean(name="reader")
	public RepositoryItemReader<ElgibilityDetailsEntity> Createreader() throws Exception
	{
		return new RepositoryItemReaderBuilder<ElgibilityDetailsEntity>()
		.name("repo-reader")
		.repository(elgRepo)
		.methodName("findAll")
		.sorts(Map.of("caseNo",Sort.Direction.ASC))
		.build();
	}//read the data from entity class
	
	@Bean(name="writer")
	public FlatFileItemWriter<ElgibiltyDetails> createWriter() throws Exception
	{
		return  ((FlatFileItemWriterBuilder<ElgibiltyDetails>) new FlatFileItemWriterBuilder<ElgibiltyDetails>())
				.name("file-writer")
				.resource(new FileSystemResource("benfitury-list.csv"))
				.lineSeparator("\r\n")
				.delimited().delimiter(",")
				.names("caseNo", "holderName", "holderSsn", "planName", "planStatus", "benficiaryAmt", "bankName", "accNo")
				.headerCallback(writer -> writer.write("CaseNo,HolderName,HolderSSN,PlanName,PlanStatus,BeneficiaryAmt,BankName,AccountNo"))
				.build();	
	}// write the data to model or binding class
	
	@Bean(name="step1")
	public Step createStep1(JobRepository jobRepository,PlatformTransactionManager trmanager) throws Exception
	{
		return new StepBuilder("step1" ,jobRepository)
				.<ElgibilityDetailsEntity, ElgibiltyDetails> chunk(3,trmanager)
				.reader( Createreader())
				.processor(process)
				.writer(createWriter())
				.build();
	}
	
	@Bean(name="job1")
	public Job createJob(JobRepository jobRepository, Step step1)
	{
		return new JobBuilder("job1",jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(step1)
				.build();
	}
}

