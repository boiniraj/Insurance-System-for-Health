package com.nt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.bindings.ChildInputs;
import com.nt.bindings.CitizenAppRegistrationInputs;
import com.nt.bindings.DcSummaryReport;
import com.nt.bindings.EducationInputs;
import com.nt.bindings.IncomeInputs;
import com.nt.bindings.PlanSelectionInputs;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.DcChildEntity;
import com.nt.entity.DcEducationEntity;
import com.nt.entity.DcIncomeEntity;
import com.nt.entity.PlanEntity;
import com.nt.repository.ICaseRepository;
import com.nt.repository.IChildRepository;
import com.nt.repository.ICitizenRegistrationRepositoty;
import com.nt.repository.IEducationRepository;
import com.nt.repository.IIncomeRepository;
import com.nt.repository.IPlanEntityRepository;

@Service
public class DcMgmtServiceClass implements IDcMgmtService{
	@Autowired
   private ICitizenRegistrationRepositoty citizenRepository;
	@Autowired
	private ICaseRepository caseRepository;
	@Autowired
	private IPlanEntityRepository planRepository;
	@Autowired
	private IIncomeRepository incomeRepository;
	@Autowired
	private IEducationRepository educationRepository;
	@Autowired
	private IChildRepository childRepository;
	
	
	
	@Override
	public Integer generateCaseNo(Integer appId) {
	Optional<CitizenAppRegistrationEntity> optcitizenapp= citizenRepository.findById(appId);
	if(optcitizenapp.isPresent())
	{
		DcCaseEntity caseEntity=new DcCaseEntity();
		caseEntity.setAppId(appId);
		return caseRepository.save(caseEntity).getCaseNo();//Save the Object
	}
	return 0;
	}//Generate CaseNo
	
	@Override
	public List<String> showAllPlanNames() {
	     List<PlanEntity> planentity=planRepository.findAll();
	     List<String> planNames=planentity.stream().map(plan-> plan.getPlanName()).toList();
		return planNames;
	}//show plans

	@Override
	public Integer savePlanSelection(PlanSelectionInputs plan) {
		    Optional<DcCaseEntity> optCaseEntity= caseRepository.findById(plan.getCaseNo());
		  if( optCaseEntity.isPresent())
		  {
		    DcCaseEntity caseEntiity= optCaseEntity.get();
		    caseEntiity.setPlanId(plan.getPlanId());
		    caseRepository.save(caseEntiity);
		    return caseEntiity.getCaseNo();
		  }
		   return 0;
	}//SavePlan

	@Override
	public Integer saveIncome(IncomeInputs income) {
		DcIncomeEntity incomeEntity=new DcIncomeEntity();
		BeanUtils.copyProperties(income, incomeEntity);
		incomeRepository.save(incomeEntity);
		return income.getCaseNo();
	}//saveIncome

	@Override
	public Integer saveEduction(EducationInputs education) {
		DcEducationEntity educationEntity=new DcEducationEntity();
		BeanUtils.copyProperties(education, educationEntity);
		educationRepository.save(educationEntity);
		return education.getCaseNo();
	}//save education

	@Override
	public Integer saveChild(List<ChildInputs> child) {
		
		child.forEach(childdata->{
			DcChildEntity childEntity=new DcChildEntity();
			BeanUtils.copyProperties(childdata, childEntity);
		childRepository.save(childEntity);
		});
		return child.get(0).getCaseNo();
	}//save child 

	@Override
	public DcSummaryReport citizenReport(Integer caseNo) {
		 //get the all details using case no
		DcIncomeEntity income = incomeRepository.findByCaseNo(caseNo);
		List<DcChildEntity >child=childRepository.findByCaseNo(caseNo);
		DcEducationEntity education= educationRepository.findByCaseNo(caseNo);
		Optional<DcCaseEntity> optCase= caseRepository.findById(caseNo);
		
		//get PlanName
		String planName=null;
		Integer appId=null;
		if(optCase.isPresent())
		{
			DcCaseEntity caseEntity=optCase.get();
			Integer planId=caseEntity.getPlanId();
			appId=caseEntity.getAppId();
			Optional<PlanEntity> planentity=planRepository.findById(planId);
			if(planentity.isPresent())
			{
			 planName=planentity.get().getPlanName();			
			}
		}
		
		Optional<CitizenAppRegistrationEntity> citizen=citizenRepository.findById(appId);
		CitizenAppRegistrationEntity citizenEntity=null;
		if(citizen.isPresent())
			citizenEntity=citizen.get();
		
		
			IncomeInputs incomeinputs= new IncomeInputs();
			BeanUtils.copyProperties(income, incomeinputs);
			
			EducationInputs eduinputs = new EducationInputs();
			BeanUtils.copyProperties(education, eduinputs);
			
			
			 List<ChildInputs >childdata=new ArrayList();
			 child.forEach(childpro->{
				 ChildInputs listchild=new ChildInputs();
				 BeanUtils.copyProperties(childpro, listchild); 
				 childdata.add(listchild);
			 });
			 
		
		CitizenAppRegistrationInputs citizeninputs=new CitizenAppRegistrationInputs();
		BeanUtils.copyProperties(citizenEntity, citizeninputs);
		
		DcSummaryReport report=new DcSummaryReport();
		report.setChilddata(childdata);
		report.setCitizeninputs(citizeninputs);
		report.setEduinputs(eduinputs);
		report.setIncomeinputs(incomeinputs);
		report.setPlanName(planName);
		return report;
	}

}
