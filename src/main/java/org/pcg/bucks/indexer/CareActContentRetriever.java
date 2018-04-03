package org.pcg.bucks.indexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.pcg.bucks.careact.documentTypes.CareActContent;
import org.pcg.bucks.careact.documentTypes.CareActItalicizedBody;
import org.pcg.bucks.careact.documentTypes.CareActSchedule;
import org.pcg.bucks.utils.IndexLoader;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;



public class CareActContentRetriever 
{
	private final static Logger LOGGER = Logger.getLogger(CareActContentRetriever.class);
	private IndexLoader indexLoader;
	static Properties properties = null;
	InputStream input = null;
	private String indexUrl1;
	private String indexUrl2;
	
	/**
	 * 
	 */
	public CareActContentRetriever()
	{
		try
		{
			indexLoader = new IndexLoader();
			properties = new Properties();
			input = new FileInputStream("config.properties");
			properties.load(input);
			
			setIndexUrl1(properties.getProperty("indexUrl1"));
			setIndexUrl2(properties.getProperty("indexUrl2"));
		}
		catch(IOException exception)
		{
			LOGGER.info(exception.getMessage());
		}
		finally
		{
			if (input != null) 
			{
				try 
				{
					input.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return List<CareActSchedule>
	 */
	public List<CareActSchedule> getListOfCareActSchedule(Integer from, Integer to)
	{
		MongoOperations mongoOps = indexLoader.getMongoOps();
		
		List<CareActSchedule> listOfCareActSchedules=null;
		
		listOfCareActSchedules = mongoOps.find(new Query(Criteria.where("docNumber").gte(from).lte(to)),CareActSchedule.class);
		
		return listOfCareActSchedules;
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return List<CareActItalicizedBody>
	 */
	public List<CareActItalicizedBody> getListOfCareActItalicizedBody(Integer from, Integer to)
	{
		
		MongoOperations mongoOps = indexLoader.getMongoOps();
		
		List<CareActItalicizedBody> listOfCareActItalicizedBody=null;
		
		listOfCareActItalicizedBody = mongoOps.find(new Query(Criteria.where("docNumber").gte(from).lte(to)),CareActItalicizedBody.class);
		
		return listOfCareActItalicizedBody;
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @return List<CareActContent>
	 */
	public List<CareActContent> getListOfCareActContent(Integer from, Integer to)
	{
		
		MongoOperations mongoOps = indexLoader.getMongoOps();
		
		List<CareActContent> listOfCareActContent=null;
		
		listOfCareActContent = mongoOps.find(new Query(Criteria.where("docNumber").gte(from).lte(to)),CareActContent.class);
		
		return listOfCareActContent;
	}

	public String getIndexUrl1() {
		return indexUrl1;
	}

	public void setIndexUrl1(String indexUrl1) {
		this.indexUrl1 = indexUrl1;
	}

	public String getIndexUrl2() {
		return indexUrl2;
	}

	public void setIndexUrl2(String indexUrl2) {
		this.indexUrl2 = indexUrl2;
	}
	
}


	
	
	
	
	