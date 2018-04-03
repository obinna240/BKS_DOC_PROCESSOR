package org.pcg.bucks.indexer.indexer;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.pcg.bucks.careact.documentTypes.CareActSchedule;
import org.pcg.bucks.indexer.CareActContentRetriever;
import org.pcg.bucks.indexer.smartSuggest.SS_CareActSchedule;
import org.pcg.bucks.utils.CleanerUtils;

/**
 * 
 * @author oonyimadu
 *
 */
public class IndexCareActSchedule 
{
private CareActContentRetriever careActContentRetriever;
	
	public IndexCareActSchedule()
	{
		careActContentRetriever = new CareActContentRetriever();
	}
	
	public void doI_CareActSchedule(Integer start, Integer end) throws SolrServerException, IOException
	{
		List<CareActSchedule> ss_content = careActContentRetriever.getListOfCareActSchedule(start, end);
		String ss_url = careActContentRetriever.getIndexUrl1();
		
		if(CollectionUtils.isNotEmpty(ss_content))
		{
			
			HttpSolrServer server = new HttpSolrServer(ss_url);
			
			for(CareActSchedule c_ac:ss_content)
			{
				
				SolrInputDocument solrDoc = new SolrInputDocument();
				
				solrDoc.addField("docGroup", "Policy");
				
				solrDoc.addField("docType2","CareAct_Schedule");
				solrDoc.addField("owner", "UK-GOV");
				solrDoc.addField("author", "UK-GOV");
				solrDoc.addField("documentUrl", "http://legislation.gov.uk/ukpga/2014/23/pdfs/ukpga_20140023_en.pdf");
				solrDoc.addField("dateOfUpload", new Date());
								
				Integer docNumber = c_ac.getDocNumber();
				String part_title = c_ac.getPartTitle();
				
				if(StringUtils.isNotBlank(part_title))
				{
					part_title = CleanerUtils.cleanApostrophe(part_title);
					part_title = StringUtils.normalizeSpace(part_title);
					solrDoc.addField("keyword",part_title);
				}
				
				List<String> listOfSubHeaders = c_ac.getListOfSubHeaders();
				String fScheduleTitle = c_ac.getFullScheduleTitle();
				
				if(StringUtils.isNotBlank(fScheduleTitle))
				{
					fScheduleTitle = CleanerUtils.cleanApostrophe(fScheduleTitle);
					fScheduleTitle = StringUtils.normalizeSpace(fScheduleTitle);
					solrDoc.addField("keyword",fScheduleTitle);
				}
				
				if(CollectionUtils.isNotEmpty(listOfSubHeaders)) 
				{
					
					for(String str:listOfSubHeaders)
					{
						str = CleanerUtils.cleanApostrophe(str);
						str = StringUtils.normalizeSpace(str);
						solrDoc.addField("listOfTitles", str);
						
						
					}
					solrDoc.addField("docType","CareAct_Summary");
					
					solrDoc.addField("title", fScheduleTitle);
					solrDoc.addField("id","CareAct_Summary_Schedule_"+docNumber);
					String fbody = c_ac.getFullScheduleSectionBody();
					fbody = CleanerUtils.cleanApostrophe(fbody);
					solrDoc.addField("partOfDocBody", fbody);
				}
				else
				{
					solrDoc.addField("docType","CareAct_Directive");
					
					String ititle = c_ac.getiTitle();
					if(StringUtils.isNotBlank(ititle))
					{
						ititle = CleanerUtils.cleanApostrophe(ititle);
						ititle = StringUtils.normalizeSpace(ititle);
						solrDoc.addField("title",ititle);
						
						String body = c_ac.getiBody1();
						body = CleanerUtils.cleanApostrophe(body);
						solrDoc.addField("body",body);
						
						String partBody = c_ac.getFullScheduleSectionBody();
						partBody = CleanerUtils.cleanApostrophe(partBody);
						solrDoc.addField("partOfDocBody", partBody);
					}
					else
					{
						if(StringUtils.isNotBlank(part_title))
						{
							solrDoc.addField("title",part_title);
							
							String partBody = c_ac.getScheduleOutlineBody1();
							partBody = CleanerUtils.cleanApostrophe(partBody);
							solrDoc.addField("partOfDocBody", partBody);
							solrDoc.addField("other_title",fScheduleTitle);
							
						}
					}
					
					solrDoc.addField("id","CareAct_Directive_Schedule_"+docNumber);
					
				}
				server.add(solrDoc);
								
			}
			server.commit();
		}
	}
	
	public static void main(String[] args) throws SolrServerException, IOException
	{
		IndexCareActSchedule  ss_ca = new IndexCareActSchedule();
		ss_ca.doI_CareActSchedule(0, 350);
	}
	
	
}
