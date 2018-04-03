package org.pcg.bucks.indexer.smartSuggest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.pcg.bucks.careact.documentTypes.CareActItalicizedBody;
import org.pcg.bucks.indexer.CareActContentRetriever;
import org.pcg.bucks.utils.CleanerUtils;

public class SS_CareActItalicizedBody 
{
	private CareActContentRetriever careActContentRetriever;
	
	public SS_CareActItalicizedBody()
	{
		careActContentRetriever = new CareActContentRetriever();
	}
	
	public void doSS_CareActItalicizedBody(Integer start, Integer end) throws SolrServerException, IOException
	{
		List<CareActItalicizedBody> ss_content = careActContentRetriever.getListOfCareActItalicizedBody(start, end);
		String ss_url = careActContentRetriever.getIndexUrl2();
		
		if(CollectionUtils.isNotEmpty(ss_content))
		{
			HttpSolrServer server = new HttpSolrServer(ss_url);
			for(CareActItalicizedBody c_ac:ss_content)
			{
				String italTitle = c_ac.getItalTitle();
				if(StringUtils.isNotBlank(italTitle))
				{
					SolrInputDocument solrDoc = new SolrInputDocument();
					italTitle = CleanerUtils.cleanApostrophe(italTitle);
					String id = italTitle+"_CareAct_Summary";
					
					//FileUtils.write(new File("written2"), "\n"+italTitle, true);
					
					solrDoc.addField("id", id);
					solrDoc.addField("entity", italTitle);
					solrDoc.addField("entity_stored", italTitle);
					solrDoc.addField("entityType", "CareAct_Summary");
					server.add(solrDoc);
					
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
		SS_CareActItalicizedBody ssItal = new SS_CareActItalicizedBody();
		ssItal.doSS_CareActItalicizedBody(0, 350);
	}
}
