package com.nt.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.bindings.ElgibilityDetailsOutput;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.CoTriggersEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.DcChildEntity;
import com.nt.entity.DcEducationEntity;
import com.nt.entity.DcIncomeEntity;
import com.nt.entity.ElgibilityDetailsEntity;
import com.nt.entity.PlanEntity;
import com.nt.repository.ICaseRepository;
import com.nt.repository.IChildRepository;
import com.nt.repository.ICitizenRegistrationRepositoty;
import com.nt.repository.ICoTriggersRepository;
import com.nt.repository.IEducationRepository;
import com.nt.repository.IElgibilityDeterminationRepository;
import com.nt.repository.IIncomeRepository;
import com.nt.repository.IPlanEntityRepository;
@Service
public class ElgibilityImplClass implements IElgibilityService {
	@Autowired
	private IPlanEntityRepository planRepo;
	@Autowired
	private ICaseRepository caseRepo; 
	@Autowired
	private ICitizenRegistrationRepositoty citizenRepo;
	@Autowired
	private IIncomeRepository incomeRepo;
	@Autowired
	private IChildRepository childRepo;
	@Autowired
	private IEducationRepository educationRepo;
	@Autowired
	private  IElgibilityDeterminationRepository elgibilityRepo;
	@Autowired
	private ICoTriggersRepository coTriggerRepo;
	
	

	@Override
	public ElgibilityDetailsOutput elgibilityCtizen(Integer caseNo) {
		
		Optional<DcCaseEntity> optCase= caseRepo.findById(caseNo);
		Integer planId=null;
		Integer appId=null;
		if(optCase.isPresent())
		{
			DcCaseEntity caseEntity=optCase.get();
			 planId=caseEntity.getPlanId();
			 appId=caseEntity.getAppId();
			}   //get planId and appId
		
		String planName=null;
	  Optional<PlanEntity> optPlan=planRepo.findById(planId);
	  if(optPlan.isPresent())
	        {
		               PlanEntity planEntity=optPlan.get();
                       planName=planEntity.getPlanName();
	        }//get planName
	  
	  String citizenfullName=null;
	  int citizenAge=0;
	 long  holderSsn= 0;
	 long accno=0;
	 String bankName=null;
	 
	  Optional<CitizenAppRegistrationEntity> optCitizen = citizenRepo.findById(appId);
	  if(optCitizen.isPresent()){
		  CitizenAppRegistrationEntity citizenEntity=optCitizen.get();
		  citizenfullName=citizenEntity.getFullName();
		  holderSsn=citizenEntity.getSsn();
		      accno=  citizenEntity.getAccountNo();
		      bankName=citizenEntity.getBankName();
		                     

		  LocalDate citizenDob=citizenEntity.getDob();
		  LocalDate sysDate=LocalDate.now();
		  citizenAge= Period.between(citizenDob, sysDate).getYears();  
		  
		  	  }//get citizen fullName and DOB
	  
	  
	  ElgibilityDetailsOutput elgibilityOutput=applyPlanConditions(planName,caseNo,citizenAge,bankName,accno);
	  elgibilityOutput.setHolderName(citizenfullName);
	  ElgibilityDetailsEntity elgEntity=new ElgibilityDetailsEntity ();
	  BeanUtils.copyProperties(elgibilityOutput, elgEntity);
	  elgEntity.setHolderSsn(holderSsn);
	  elgEntity.setCaseNo(caseNo);
	  elgEntity.setAccNo(accno);
	  elgEntity.setBankName(bankName);
	 
	  
	  elgibilityRepo.save(elgEntity);
	  
	  	CoTriggersEntity coTrigger=new CoTriggersEntity();
	  	coTrigger.setCaseNo(caseNo);
	  	coTrigger.setTriggerStatus("Pending");
	  	 coTriggerRepo.save(coTrigger);
	  	
		return elgibilityOutput ;
	}//end method
	
     //private method helper method
	  private ElgibilityDetailsOutput applyPlanConditions(String planName,	Integer citizenAge,Integer caseNo, String bankName, Long accno)
	     {
		  
		  ElgibilityDetailsOutput elgibilityOutput=new ElgibilityDetailsOutput();
		    elgibilityOutput.setPlanName(planName);
		    
		    DcIncomeEntity incomeEntity = incomeRepo.findByCaseNo(caseNo);
		    double holderIncome=0;
		    double holderPropIncome=0;
		    if (incomeEntity != null) {
		         holderIncome = incomeEntity.getEmpIncome();
		         holderPropIncome = incomeEntity.getPropertyIncome();
		        // Continue with the logic
		    } else {
		        System.out.println("Null pointer Exception");
		    }

			/*
			 * DcIncomeEntity incomeEntity=incomeRepo.findByCaseNo(caseNo);
			 * 
			 * Double holderIncome= incomeEntity.getEmpIncome(); Double
			 * holderpropIncome=incomeEntity.getPropertyIncome();
			 */
		     
		    
	         
	         if(planName.equalsIgnoreCase("SNAP"))
	         {
	        	 if(holderIncome<=100000.0)
	        	    {
	        		 elgibilityOutput.setPlanStatus("Approved");
	        		 elgibilityOutput.setBenficiaryAmt(200.0);
	        	       }
	        	 else {
	        		 elgibilityOutput.setPlanStatus("denaied");
		        	 elgibilityOutput.setDenailReson("citizen income is higher");
		            } 
	            }//end snap
	         
	         else if(planName.equalsIgnoreCase("CCAP"))
	          {
	        	 boolean kidsCountCondition=false;
	        	 boolean KidsAgeContition=true;
	        	 
	        	 List<DcChildEntity> childEntity= childRepo.findByCaseNo(caseNo);
	        	   if(!childEntity.isEmpty())
	        	   {
	        		   kidsCountCondition=true;
	        		   
	        		   for(DcChildEntity child:childEntity)
	        		      {
	        			             int childAge=Period.between(child.getDob(),LocalDate.now()).getYears();
	        			             if(childAge>16)
	        			             {
	        			            	 KidsAgeContition=false;
	        			            	 break;        			            	 
	        			             }//if        			            	 
	        		   }//end for loop
	        	   }//if
	        	   if(holderIncome<=100000.0 && kidsCountCondition && KidsAgeContition)
	        	   {
	        		   elgibilityOutput.setPlanStatus("Approved");
	        		   elgibilityOutput.setBenficiaryAmt(200.0);
	        	   }
	        	   else {
	        		     elgibilityOutput.setPlanStatus("denaied");
			        	 elgibilityOutput.setDenailReson("CCAP Rules are not satisfied");
	        	      }
	            }//ccap
	         
	         else if(planName.equalsIgnoreCase("MEDAID"))
	         {
	        	                   if(holderIncome<=100000.0&&  holderPropIncome==0.0)
	        	                   {
	        	                	   elgibilityOutput.setPlanStatus("Approved");
	        	                	   elgibilityOutput.setBenficiaryAmt(200.0);	   
	        	                   }
	        	                   else {
	        		        		     elgibilityOutput.setPlanStatus("denaied");
	        				        	 elgibilityOutput.setDenailReson("MEDAID Rules are not satisfied");
	        		        	      }       	 
	         }//medaied
	         
	         else if(planName.equalsIgnoreCase("MEDCARE"))
	         {
	        	 if(citizenAge>=65)
	        	 {
	        		 elgibilityOutput.setPlanStatus("APPROVED");
	        		 elgibilityOutput.setBenficiaryAmt(200.0);	 
	        	 }
	        	 else {
        		     elgibilityOutput.setPlanStatus("denaied");
		        	 elgibilityOutput.setDenailReson("MEDCARE Rules are not satisfied");
        	      } 
	         }//MEDCARE
	         
	         else if(planName.equalsIgnoreCase("CAJW"))
	         {
	        	     DcEducationEntity eduEntity=educationRepo.findByCaseNo(caseNo);
	        	    int passoutYear= eduEntity.getPassOutYear();
	        	    if(holderIncome==0 && passoutYear < LocalDate.now().getYear())
	        	    {
	        	    	elgibilityOutput.setPlanStatus("APPROVED");
	        	    	elgibilityOutput.setBenficiaryAmt(300.0);
	        	    }
	        	    else {
	        		     elgibilityOutput.setPlanStatus("denaied");
			        	 elgibilityOutput.setDenailReson("CAJW Rules are not satisfied");
	        	      } 
	        	 
	         }//CAJW
	         if(  elgibilityOutput.getPlanStatus().equalsIgnoreCase("APPROVED"))
	         {
	        	 elgibilityOutput.setStartDate(LocalDate.now());
	        	 elgibilityOutput.setEndDate(LocalDate.now().plusYears(5)); 
	        	 elgibilityOutput.setAccNo(accno);
	        	 elgibilityOutput.setBankName(bankName);
	        	 
	        	 
	        	 
	         }
				return elgibilityOutput;
			}
}
