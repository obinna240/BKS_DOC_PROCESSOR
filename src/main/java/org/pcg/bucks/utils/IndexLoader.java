package org.pcg.bucks.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

public class IndexLoader
{
	MongoOperations mongoOps;
	private ApplicationContext context;
	//GridFsOperations gridOperations;
	
	

	public IndexLoader()
	{
		
		context = new FileSystemXmlApplicationContext("bean.xml");
							
		mongoOps = (MongoOperations) context.getBean("mongoTemplate");
		//gridOperations = (GridFsOperations) context.getBean("gridFsTemplate");
	              
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

	/**
	public GridFsOperations getGridOperations() {
		return gridOperations;
	}

	public void setGridOperations(GridFsOperations gridOperations) {
		this.gridOperations = gridOperations;
	}
	*/

}
