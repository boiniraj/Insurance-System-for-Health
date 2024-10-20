package com.nt.service;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nt.bindings.CoSummary;
import com.nt.entity.CitizenAppRegistrationEntity;
import com.nt.entity.CoTriggersEntity;
import com.nt.entity.DcCaseEntity;
import com.nt.entity.ElgibilityDetailsEntity;
import com.nt.repository.ICaseRepository;
import com.nt.repository.ICitizenRegistrationRepositoty;
import com.nt.repository.ICoTriggersRepository;
import com.nt.repository.IElgibilityDeterminationRepository;
import com.nt.utils.EmailUtils;

@Service
public class CoSummaryTriggersImpl implements CoSummaryTriggers {
	@Autowired
	private ICaseRepository caseRepo;
	@Autowired
	private ICitizenRegistrationRepositoty citizenRepo;
	@Autowired
	private ICoTriggersRepository coTriggersRepo;
	@Autowired
	private IElgibilityDeterminationRepository elgibilityRepo;
	@Autowired
	private EmailUtils emailUtils;
	int succesTriggers=0;
	int pendingTriggers=0;
	
	@Value("${muti.property}")
	private int mutiProperty;
	
	@Override
	public CoSummary coTriggers()  throws Exception {
		
		CitizenAppRegistrationEntity citizenEntity=null;
		ElgibilityDetailsEntity elgibleEntity=null;
		
		
		//get All pending Triggers
		List<CoTriggersEntity> coEntity= coTriggersRepo.findByTriggerStatus("pending");
		CoSummary coSum=new CoSummary();
		coSum.setTotalTriggers(coEntity.size());
		//process the triggers in multithread env.. using Exucutor Framework
		ExecutorService executorService=Executors.newFixedThreadPool(mutiProperty);
		ExecutorCompletionService<Object> pool=new ExecutorCompletionService<Object>(executorService);
		
		
		
		//process each pending trigger
		for(CoTriggersEntity coTriggers:coEntity)
		{
			
			
			/*elgibleEntity=elgibilityRepo.findByCaseNo(coTriggers.getCaseNo()); //get
			appId based on CaseNo Optional<DcCaseEntity>
			 optCase=caseRepo.findById(coTriggers.getCaseNo()); if(optCase.isPresent()) {
			 DcCaseEntity caseEntity=optCase.get(); int appId=caseEntity.getAppId();
			 Optional<CitizenAppRegistrationEntity>
			 optCitizen=citizenRepo.findById(appId); if(optCitizen.isPresent()) {
			citizenEntity= optCitizen.get(); } } try {
			catch(Exception e) { pendingTriggers++; e.printStackTrace(); }
			 generatePdfAndSendMail(elgibleEntity,citizenEntity) ; succesTriggers ++;
			 }*/
			/*try {
				processTriggers(coSum,coTriggers);
				succesTriggers++;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				 pendingTriggers++;
			}*/
			pool.submit(()->{
			try {
				processTriggers(coSum,coTriggers);
				succesTriggers++;
				
				
			}catch(Exception e)
			{
				e.printStackTrace();
				 pendingTriggers++;
			}
				return null;
			});
		                                  
	         }// end for
				/*// Shutdown the executor service
				executorService.shutdown();
				try {
				    // Wait for all tasks to complete
				    while (!executorService.isTerminated()) {
				        // This can be adjusted as needed
				        Thread.sleep(100);
				    }
				} catch (InterruptedException e) {
				    Thread.currentThread().interrupt(); // Restore interrupted status
				    e.printStackTrace();
				}
				*/
	coSum.setPendingTriggers(pendingTriggers);
	coSum.setSuccesTriggers(succesTriggers);

	return coSum;

	}

	private CitizenAppRegistrationEntity processTriggers(CoSummary coSum, CoTriggersEntity coTriggers) throws Exception{
		CitizenAppRegistrationEntity citizenEntity = null;
		ElgibilityDetailsEntity elgibleEntity=elgibilityRepo.findByCaseNo(coTriggers.getCaseNo());
		Optional<DcCaseEntity> optCase=caseRepo.findById(coTriggers.getCaseNo()); 
		if(optCase.isPresent()) {
		  DcCaseEntity caseEntity=optCase.get(); 
	       Integer appId=caseEntity.getAppId();
		 Optional<CitizenAppRegistrationEntity>optCitizen=citizenRepo.findById(appId);
		 if(optCitizen.isPresent()) {
		  citizenEntity= optCitizen.get(); 
		 }}
		 
		     generatePdfAndSendMail(elgibleEntity,citizenEntity) ; 
		     return citizenEntity;
		    
	}
	

	// helper method generatePdf and Email
	private void generatePdfAndSendMail(ElgibilityDetailsEntity elgibleEntity,
			CitizenAppRegistrationEntity citizenEntity) throws Exception {
		// Create Document Pdf
		Document pdf = new Document(PageSize.A4);
		// Create pdf file write to the pdf content
		File file = new File(elgibleEntity.getCaseNo() + ".pdf");
		FileOutputStream fos = new FileOutputStream(file);
		// Write content to pdf
		PdfWriter.getInstance(pdf, fos);
		pdf.open();
		// cerate font
		Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
		font.setSize(20);
		font.setColor(Color.blue);

		// create paragraph
		Paragraph para = new Paragraph("Plan Approved And Denail Report", font);

		para.setAlignment(Paragraph.ALIGN_TOP);
		// add para to pdf
		pdf.add(para);

		// display search results through table
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100);
		table.setWidths(new float[] { 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f });
		table.setSpacingAfter(5.0f);

		// prepare heading Rows cells in Table
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.lightGray);
		cell.setPadding(6);
		Font cellfont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		cellfont.setColor(Color.green);

		cell.setPhrase(new Phrase("TraceID", cellfont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("CaseNo", cellfont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("HolderName", cellfont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("HolderSSN", cellfont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("PlanName", cellfont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("PlanStatus", cellfont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("PlanStartDate", cellfont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("PlanEndDate", cellfont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("BenfitAmt", cellfont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("DeniolReson", cellfont));
		table.addCell(cell);
		// add data cells to pdfTable
		table.addCell(String.valueOf(elgibleEntity.getEdTrackId()));
		table.addCell(String.valueOf(elgibleEntity.getCaseNo()));
		table.addCell(elgibleEntity.getHolderName());
		table.addCell(String.valueOf(elgibleEntity.getHolderSsn()));
		table.addCell(elgibleEntity.getPlanName());
		table.addCell(elgibleEntity.getPlanStatus());
		table.addCell(String.valueOf(elgibleEntity.getStartDate()));
		table.addCell(String.valueOf(elgibleEntity.getEndDate()));
		table.addCell(String.valueOf(elgibleEntity.getBenficiaryAmt()));
		table.addCell(elgibleEntity.getDenailReson());
		// add table to the pdf
		pdf.add(table);
		// close the pdf
		pdf.close();
		// send the generated pdf as to the email
		String subject = "Plan Approved Or Denail Reson Status";
		String body = "hello mr/ms" + elgibleEntity.getHolderName()+ "This mail is details of paln Approved or denails ";
		emailUtils.sendEmailMessage(citizenEntity.getEmail(), subject, body, file);
		// update coTriggers Table
		updateCoTriggers(elgibleEntity.getCaseNo(), file);

	}

	public void updateCoTriggers(Integer caseNo, File file) throws Exception {
		// Check Triggers are avalaible or not based on caseNo
		CoTriggersEntity coEntity = coTriggersRepo.findByCaseNo(caseNo);
		byte[] pdfContent = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		fis.read(pdfContent);
		if (coEntity != null) {
			coEntity.setCoNoticePdf(pdfContent);
			coEntity.setTriggerStatus("completed");
			coTriggersRepo.save(coEntity);
		}
		fis.close();
	}
}
