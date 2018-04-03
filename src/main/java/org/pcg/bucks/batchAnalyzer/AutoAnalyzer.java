package org.pcg.bucks.batchAnalyzer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.pcg.bucks.careact.documentUtils.Content;
import org.pcg.bucks.indexer.indexer.IndexHintDocument;
import org.pcg.bucks.indexer.indexer.IndexTopicDocument;
import org.pcg.bucks.indexer.smartSuggest.SS_Document;
import org.pcg.bucks.processors.DocProcessor;
import org.pcg.bucks.utils.ContentCreator;

/**
 * 
 * @author oonyimadu
 *
 */
public class AutoAnalyzer 
{
	public void start(String directory, String documentCategory)
	{
		File dir = new File(directory);
		if(dir.exists())
		{
			if(dir.isDirectory())
			{
				File[] fList = dir.listFiles();
				if(ArrayUtils.isNotEmpty(fList))
				{
					for(File file:fList)
					{
						String fname = file.getName();
						Content content = ContentCreator.createContent(fname);
						content.setDocumentCategory(documentCategory);
						String fpath = file.getPath();
						
						fpath = "file:/"+fpath;
						content.setDocumentUrl(fpath);
							
						//apply to docs other than the care act
						DocProcessor d = new DocProcessor("Other",fpath);
						Integer count = d.doAnalysis(content);
						
						if(count!=null)
						{
							//index content
							SS_Document ssD = new SS_Document();
							IndexHintDocument iHD = new IndexHintDocument();
							IndexTopicDocument idd = new IndexTopicDocument();
										
							try 
							{
								ssD.doIndexWholeDocument(count, count);
								ssD.doSS_Document(count, count);
								idd.doIndexTopic(count,count);
								iHD.doIndexHint(count, count);
							} 
							catch (SolrServerException e)
							{
								
								e.printStackTrace();
							} 
							catch (IOException e) 
							{
								
								e.printStackTrace();
							}
						}
						
					}
				}
			}
		}
	}
}
