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
import org.pcg.bucks.careact.documentTypes.CareActContent;
import org.pcg.bucks.indexer.CareActContentRetriever;
import org.pcg.bucks.utils.CleanerUtils;

public class SS_CareActContent 
{
	private CareActContentRetriever careActContentRetriever;
	
	public SS_CareActContent()
	{
		careActContentRetriever = new CareActContentRetriever();
	}
	
	
	
	
	public void doSS_CareActContent(Integer start, Integer end) throws SolrServerException, IOException 
	{
		List<CareActContent> ss_content = careActContentRetriever.getListOfCareActContent(start, end);
		String ss_url = careActContentRetriever.getIndexUrl2();
		
		HttpSolrServer server = new HttpSolrServer(ss_url);
		if(CollectionUtils.isNotEmpty(ss_content))
		{
			
			for(CareActContent c_ac:ss_content)
			{
								
				String s_title = c_ac.getSubHeadingTitle();
				if(StringUtils.isNotBlank(s_title))
				{
					SolrInputDocument solrDoc = new SolrInputDocument();
					
					s_title = CleanerUtils.removeLeadingNumber(s_title);
					
					//FileUtils.write(new File("written"), "\n"+s_title, true);
					s_title = CleanerUtils.cleanApostrophe(s_title);
					String id = s_title+"_CareAct_Directive";
					
					solrDoc.addField("id", id);
					solrDoc.addField("entity", s_title);
					solrDoc.addField("entity_stored", s_title);
					solrDoc.addField("entityType", "CareAct_Directive");
					server.add(solrDoc);
				}
				
				String part_title = c_ac.getPartTitle();
				if(StringUtils.isNotBlank(part_title))
				{
					part_title = CleanerUtils.cleanApostrophe(part_title);
					SolrInputDocument solrDoc = new SolrInputDocument();
					String id = part_title+"_Keyword";
					solrDoc.addField("id", id);
					solrDoc.addField("entity", part_title);
					solrDoc.addField("entity_stored", part_title);
					solrDoc.addField("entityType", "Keyword");
					server.add(solrDoc);
				}
				
				String chapter_title = c_ac.getChapterTitle();
				if(StringUtils.isNotBlank(chapter_title))
				{
					chapter_title = CleanerUtils.cleanApostrophe(chapter_title);
					SolrInputDocument solrDoc = new SolrInputDocument();
					String id = chapter_title+"_Keyword";
					solrDoc.addField("id", id);
					solrDoc.addField("entity", chapter_title);
					solrDoc.addField("entity_stored", chapter_title);
					solrDoc.addField("entityType", "Keyword");
					server.add(solrDoc);
					
					//server.commit();
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
	
	
	
	public static void main(String[] args) throws SolrServerException, IOException
	{
		//String x = "99 This is a new car";
		//x = "1 This is a new car";
		//StringUtils.repla(source, regex, replacement);
		//x = x.replaceFirst("^\\d+", "");
		//System.out.println(x);
		
		SS_CareActContent ssca = new SS_CareActContent();
		ssca.doSS_CareActContent(0, 300);
	}
	
	
}
