package org.pcg.bucks.codeUpdates;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.pcg.bucks.careact.documentTypes.DocumentNotes;

import org.pcg.bucks.utils.IndexLoader;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * 
 * @author oonyimadu
 *
 */
public class UpdateDbDates 
{
	private IndexLoader indexLoader;
	private final static Logger LOGGER = Logger.getLogger(UpdateDbDates.class);
	
	public UpdateDbDates ()
	{
		
		indexLoader= new IndexLoader();
		
	}
	
	/**
	 * 
	 * @param docNumber
	 * @param dateOfPublication
	 * @param version
	 * @throws ParseException
	 */
	public void indexListOfDocumentNotes(Integer docNumber, String dateOfPublication, String version) throws ParseException
	{
		MongoOperations mongoOps = indexLoader.getMongoOps();
						
		//listOfDocumentNotes = mongoOps.find(new Query(Criteria.where("docNumber").gte(from).lte(to)),DocumentNotes.class);
		List<DocumentNotes> listOfDocumentNotes=  mongoOps.find(new Query(Criteria.where("docNumber").is(docNumber)),DocumentNotes.class);
		
		if(CollectionUtils.isNotEmpty(listOfDocumentNotes))
		{
			DocumentNotes dNote = listOfDocumentNotes.get(0);
						
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dateOfDoc = formatter.parse(dateOfPublication);
			
			dNote.setDateOfPublicationAsDate(dateOfDoc);
			dNote.setVersion(version);
			dNote.setDateOfPublication(dateOfPublication);
			
			mongoOps.save(dNote);
		}
				
	}
	
	/**
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException
	{
		UpdateDbDates uu = new UpdateDbDates();
		uu.indexListOfDocumentNotes(0, "2015-01-08", "1"); 
	}
}
