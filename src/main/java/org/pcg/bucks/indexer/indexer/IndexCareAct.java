package org.pcg.bucks.indexer.indexer;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.pcg.bucks.careact.documentTypes.CareActContent;
import org.pcg.bucks.careact.documentTypes.CareActItalicizedBody;
import org.pcg.bucks.careact.documentTypes.CareActSchedule;
import org.pcg.bucks.indexer.CareActContentRetriever;
import org.pcg.bucks.utils.CleanerUtils;

/**
 * 
 * @author oonyimadu
 *
 */
public class IndexCareAct
{

	private CareActContentRetriever careActContentRetriever;
	
	/**
	 * 
	 */
	public IndexCareAct()
	{
		careActContentRetriever = new CareActContentRetriever();
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void doI_CareActContent(Integer start, Integer end) throws SolrServerException, IOException 
	{
		List<CareActContent> ss_content = careActContentRetriever.getListOfCareActContent(start, end);
	
		String ss_url = careActContentRetriever.getIndexUrl1();
		
		if(CollectionUtils.isNotEmpty(ss_content))
		{
			HttpSolrServer server = new HttpSolrServer(ss_url);		
			
			for(CareActContent c_ac:ss_content)
			{
				SolrInputDocument solrDoc = new SolrInputDocument();
				
				//get and set ID
				Integer docNumber = c_ac.getDocNumber();
				String id = "CareAct_"+docNumber;
				
				solrDoc.addField("id", id);
				
				//get and set the title
				String s_title = c_ac.getSubHeadingTitle();
				s_title = CleanerUtils.removeLeadingNumber(s_title);
				
				if(StringUtils.isNotBlank(s_title))
				{
					s_title = CleanerUtils.cleanApostrophe(s_title);
					s_title = StringUtils.normalizeSpace(s_title);
					solrDoc.addField("title", s_title);
				}
				solrDoc.addField("docGroup", "The Care Act");
				solrDoc.addField("docGroup", "Policy");
				solrDoc.addField("docType","CareAct_Directive");
				solrDoc.addField("docType2","CareAct");
				solrDoc.addField("owner", "UK-GOV");
				solrDoc.addField("author", "UK-GOV");
				solrDoc.addField("documentUrl", "http://legislation.gov.uk/ukpga/2014/23/pdfs/ukpga_20140023_en.pdf");
				solrDoc.addField("dateOfUpload", c_ac.getDateOfUpload());
				
				String body = c_ac.getBody();
				
				if(StringUtils.isNotBlank(body))
				{
					body = CleanerUtils.cleanApostrophe(body);
					solrDoc.addField("body",body);
				}
				
				String partNumber = c_ac.getPartNumber();
				String part_title = c_ac.getPartTitle();
				
				if(StringUtils.isNotBlank(part_title))
				{
					part_title = CleanerUtils.cleanApostrophe(part_title);	
					part_title = StringUtils.normalizeSpace(part_title);
					solrDoc.addField("keyword", part_title);
					solrDoc.addField("partTitle", "PART "+partNumber+" - "+part_title);
				}
				
				String chapter_title = c_ac.getChapterTitle();
				String chapterNumber = c_ac.getChapterNumber();
				
				if(StringUtils.isNotBlank(chapter_title))
				{
					chapter_title = CleanerUtils.cleanApostrophe(chapter_title);
					chapter_title = StringUtils.normalizeSpace(chapter_title);
					solrDoc.addField("keyword", chapter_title);
					solrDoc.addField("chapterTitle", "CHAPTER "+chapterNumber+" - "+chapter_title);
				}
				
				String iHeader = c_ac.getiHeader();
				
				if(StringUtils.isNotBlank(iHeader))
				{
					iHeader = CleanerUtils.cleanApostrophe(iHeader);
					iHeader = StringUtils.normalizeSpace(iHeader);
					solrDoc.addField("other_title", iHeader);
					solrDoc.addField("partOfDocTitle", iHeader);
				}
				
				String fBody = c_ac.getFullChapterBody();
				
				if(StringUtils.isNotBlank(fBody))
				{
					fBody = CleanerUtils.cleanApostrophe(fBody);
					solrDoc.addField("partOfDocBody", fBody);
					
				}
				else
				{
					fBody = c_ac.getFullCareActContent();
					fBody = CleanerUtils.cleanApostrophe(fBody);
					solrDoc.addField("partOfDocBody", fBody);
				}
				
				server.add(solrDoc);
		
			}
			server.commit();
		}
		
	}
	
	
	public static void main(String[] args) throws SolrServerException, IOException
	{
		IndexCareAct id = new IndexCareAct();
		id.doI_CareActContent(0, 150);
		
	}
	
	
}
