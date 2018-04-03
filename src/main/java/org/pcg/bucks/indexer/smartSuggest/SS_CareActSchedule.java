package org.pcg.bucks.indexer.smartSuggest;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.pcg.bucks.careact.documentTypes.CareActSchedule;
import org.pcg.bucks.indexer.CareActContentRetriever;
import org.pcg.bucks.utils.CleanerUtils;

public class SS_CareActSchedule
{
	private CareActContentRetriever careActContentRetriever;
	
	public SS_CareActSchedule()
	{
		careActContentRetriever = new CareActContentRetriever();
	}
	
	public void doSS_CareActSchedule(Integer start, Integer end) throws SolrServerException, IOException
	{
		List<CareActSchedule> ss_content = careActContentRetriever.getListOfCareActSchedule(start, end);
		String ss_url = careActContentRetriever.getIndexUrl2();
		if(CollectionUtils.isNotEmpty(ss_content))
		{
			HttpSolrServer server = new HttpSolrServer(ss_url);
			for(CareActSchedule c_ac:ss_content)
			{
				String part_title = c_ac.getPartTitle();
				if(StringUtils.isNotBlank(part_title))
				{
					SolrInputDocument solrDoc = new SolrInputDocument();
					part_title = CleanerUtils.cleanApostrophe(part_title);
					String id = part_title+"_Keyword";
					solrDoc.addField("id", id);
					solrDoc.addField("entity", part_title);
					solrDoc.addField("entity_stored", part_title);
					solrDoc.addField("entityType", "Keyword");
					server.add(solrDoc);
				}
				
				String iTitle = c_ac.getiTitle();
				if(StringUtils.isNotBlank(iTitle))
				{
					SolrInputDocument solrDoc = new SolrInputDocument();
					iTitle = CleanerUtils.cleanApostrophe(iTitle);
					String id = iTitle+"_CareAct_Directive";
					
					solrDoc.addField("id", id);
					
					solrDoc.addField("entity", iTitle);
					solrDoc.addField("entity_stored",iTitle);
					solrDoc.addField("entityType", "CareAct_Directive");
					server.add(solrDoc);
				}
				
				List<String> listOfSubHeaders = c_ac.getListOfSubHeaders();
				if(CollectionUtils.isNotEmpty(listOfSubHeaders))
				{
					String fScheduleTitle = c_ac.getFullScheduleTitle();
					if(StringUtils.isNotBlank(fScheduleTitle))
					{
						SolrInputDocument solrDoc = new SolrInputDocument();
						fScheduleTitle = CleanerUtils.cleanApostrophe(fScheduleTitle);
						String id = fScheduleTitle+"_CareAct_Summary";
						
						solrDoc.addField("id", id);
						solrDoc.addField("entity", fScheduleTitle);
						solrDoc.addField("entity_stored",fScheduleTitle);
						solrDoc.addField("entityType", "CareAct_Summary");
						server.add(solrDoc);
					}
				}
				else
				{
					String fScheduleTitle = c_ac.getFullScheduleTitle();
					if(StringUtils.isNotBlank(fScheduleTitle))
					{
						SolrInputDocument solrDoc = new SolrInputDocument();
						fScheduleTitle = CleanerUtils.cleanApostrophe(fScheduleTitle);
						String id = fScheduleTitle+"_Keyword";
						
						solrDoc.addField("id", id);
						solrDoc.addField("entity", fScheduleTitle);
						solrDoc.addField("entity_stored",fScheduleTitle);
						solrDoc.addField("entityType", "Keyword");
						server.add(solrDoc);
					}
				}
				
			}
			
			SolrInputDocument solrDoc = new SolrInputDocument();
			
			String id = "UK-GOV"+"_Author";
			solrDoc.addField("id", id);
			solrDoc.addField("entity", "UK-GOV");
			solrDoc.addField("entity_stored", "UK-GOV");
			solrDoc.addField("entityType", "Author");
			server.add(solrDoc);
			
			
			server.commit();
		}
	}
	
	public static void main(String[] args) throws SolrServerException, IOException
	{
		SS_CareActSchedule ss_ca = new SS_CareActSchedule();
		ss_ca.doSS_CareActSchedule(0, 150);
	}
}
