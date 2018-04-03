package org.pcg.bucks.utils;

import gate.util.DocumentProcessor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

public class Loader 
{
	DocumentProcessor processor;
	GridFsOperations gridOperations;
	

	MongoOperations mongoOps;
	private ApplicationContext context;
	String docType;
	
	public Loader(String docType)
	{
		this.docType = docType;
		if(StringUtils.equalsIgnoreCase(docType, "care act"))
		{
			context = new FileSystemXmlApplicationContext("CareActBean.xml");
			
			processor = (DocumentProcessor) context.getBean("processor");
		}
		else if(StringUtils.equalsIgnoreCase(docType, "Other")) //make changes and edit
		{
			context = new FileSystemXmlApplicationContext("DocumentTypeBean.xml");
			//mongoOps = (MongoOperations) context.getBean("mongoTemplate");
			processor = (DocumentProcessor) context.getBean("processor");
			
			gridOperations = (GridFsOperations) context.getBean("gridFsTemplate");
                    
		}
		mongoOps = (MongoOperations) context.getBean("mongoTemplate");
	}

	public DocumentProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(DocumentProcessor processor) {
		this.processor = processor;
	}

	public MongoOperations getMongoOps() {
		return mongoOps;
	}

	public void setMongoOps(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}
	
	public GridFsOperations getGridOperations() {
		return gridOperations;
	}

	public void setGridOperations(GridFsOperations gridOperations) {
		this.gridOperations = gridOperations;
	}
}
