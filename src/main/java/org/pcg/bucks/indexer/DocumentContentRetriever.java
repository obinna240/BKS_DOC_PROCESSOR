package org.pcg.bucks.indexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import org.pcg.bucks.careact.documentTypes.DocumentNotes;
import org.pcg.bucks.utils.IndexLoader;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import com.mongodb.gridfs.GridFSDBFile;

public class DocumentContentRetriever 
{
	private final static Logger LOGGER = Logger.getLogger(DocumentContentRetriever.class);
	private IndexLoader indexLoader;
	static Properties properties = null;
	InputStream input = null;
	private String indexUrl1;
	private String indexUrl2;
	
	public DocumentContentRetriever()
	{
		try
		{
			setIndexLoader(new IndexLoader());
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
	
	public List<DocumentNotes> getListOfDocumentNotes(Integer from, Integer to)
	{
		MongoOperations mongoOps = indexLoader.getMongoOps();
		
		List<DocumentNotes> listOfDocumentNotes=null;
		
		listOfDocumentNotes = mongoOps.find(new Query(Criteria.where("docNumber").gte(from).lte(to)),DocumentNotes.class);
		
		return listOfDocumentNotes;
	}
	
	/**
	public List<GridFSDBFile> getImages(String id) throws IOException
	{
		List<GridFSDBFile> result = indexLoader.getGridOperations().find(new Query().addCriteria(Criteria.where("metadata.image_doc_id").is(id)));
		GridFsOperations gs = indexLoader.getGridOperations();
		Query q = new Query().addCriteria(Criteria.where("metadata.image_doc_id").is("FullBody_"+id));
		result = gs.find(q);
		
		return result;
		
	}
	*/
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

	public IndexLoader getIndexLoader() {
		return indexLoader;
	}

	public void setIndexLoader(IndexLoader indexLoader) {
		this.indexLoader = indexLoader;
	}
	
}


