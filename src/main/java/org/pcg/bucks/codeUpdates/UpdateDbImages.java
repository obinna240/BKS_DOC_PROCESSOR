package org.pcg.bucks.codeUpdates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.pcg.bucks.utils.MSWordImageExtractor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import gate.util.DocumentProcessor;

public class UpdateDbImages 
{
	public static void main(String[] args)
	{
		ApplicationContext context = new FileSystemXmlApplicationContext("DocumentTypeBean.xml");
		
		
		GridFsOperations gridOperations = (GridFsOperations) context.getBean("gridFsTemplate");
		
		String fileUrl = "C:/Users/oonyimadu/Documents/Bucks_ss_files/Transition protocol_v1_20150730.docx";
		String docLocation = "Transition protocol_v1_20150730.docx";
		String docTitle = "Strengthening Transitions Arrangements Multi-Agency Protocol Update";
		String id = "41";
		String docCategory = "Policy";
		String dateOfPub = "30-07-2015";
		String[] listAuth = {"R Woodward","Alison Bulman"};
		String owner = "Bucks";
		List<String> authos = Arrays.asList(listAuth);
		
		
		MSWordImageExtractor extractor = new MSWordImageExtractor();
		extractor.extractImage(fileUrl);
		String tfile = MSWordImageExtractor.tempfile;
		File ifile = new File(tfile);
		File[] lfiles = ifile.listFiles();
		
		if(ArrayUtils.isNotEmpty(lfiles))
		{
			for(File ff: lfiles)
			{
				DBObject metaData = new BasicDBObject();
				
				metaData.put("docTitle", docTitle);
				metaData.put("docCategory", docCategory);
				metaData.put("imageLocation", docLocation);
				metaData.put("image_doc_id", "FullBody_"+id);
				metaData.put("dateOfPub", dateOfPub);
				metaData.put("authors", authos);
				metaData.put("owner", owner);
				
				FileInputStream inpStream = null;
				try 
				{
					inpStream = FileUtils.openInputStream(ff);
					
					gridOperations.store(inpStream, ff.getName(), "image", metaData);
					
				} catch (IOException e)
				{
					e.printStackTrace();
				} 
				finally
				{
					try {
						inpStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
