package com.nt.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.nt.bindings.ElgibiltyDetails;
import com.nt.entity.ElgibilityDetailsEntity;
@Component
public class EdProcessor implements ItemProcessor<ElgibilityDetailsEntity, ElgibiltyDetails> {

	@Override
	public ElgibiltyDetails process(ElgibilityDetailsEntity item) throws Exception {
		if(item.getPlanStatus().equalsIgnoreCase("Approved"))
		{
			ElgibiltyDetails elgDetails=new ElgibiltyDetails();
			BeanUtils.copyProperties(item, elgDetails);
			return elgDetails;		
		}
		return null;
	}
}// process the data to ElgibilityDetailsEntity to model object and return model class or binding class object
