package org.pcg.bucks.indexer.indexer;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.pcg.bucks.careact.documentTypes.CareActItalicizedBody;
import org.pcg.bucks.indexer.CareActContentRetriever;
import org.pcg.bucks.utils.CleanerUtils;

/**
 * Summary of a care act directive
 * @author oonyimadu
 *
 */
public class IndexCareActItal 
{
private CareActContentRetriever careActContentRetriever;
	
	public IndexCareActItal ()
	{
		careActContentRetriever = new CareActContentRetriever();
	}
	
	public void doI_CareActItalicizedBody(Integer start, Integer end) throws SolrServerException, IOException
	{
		List<CareActItalicizedBody> ss_content = careActContentRetriever.getListOfCareActItalicizedBody(start, end);
		String ss_url = careActContentRetriever.getIndexUrl1();
		
		if(CollectionUtils.isNotEmpty(ss_content))
		{
			HttpSolrServer server = new HttpSolrServer(ss_url);
			for(CareActItalicizedBody c_ac:ss_content)
			{
				String italTitle = c_ac.getItalTitle();
				if(StringUtils.isNotBlank(italTitle))
				{
					SolrInputDocument solrDoc = new SolrInputDocument();
					

					solrDoc.addField("docGroup", "Policy");
					solrDoc.addField("docType","CareAct_Summary");
					solrDoc.addField("docType2","CareAct");
					solrDoc.addField("owner", "UK-GOV");
					solrDoc.addField("author", "UK-GOV");
					solrDoc.addField("documentUrl", "http://legislation.gov.uk/ukpga/2014/23/pdfs/ukpga_20140023_en.pdf");
					solrDoc.addField("dateOfUpload", c_ac.getDateOfUpload());
					
					Integer docNumber = c_ac.getDocNumber();
					
			
					String id = "CareAct_Summary_"+docNumber;
					
					solrDoc.addField("id", id);
					italTitle = CleanerUtils.cleanApostrophe(italTitle);
					
					italTitle = StringUtils.normalizeSpace(italTitle);
					
					solrDoc.addField("title", italTitle);
					
					
					String part_title = c_ac.getPartTitle();
					
					
					String partNumber = c_ac.getPartNumber();
					String italBody = c_ac.getItalBody();
					
					if(StringUtils.isNotBlank(italBody))
					{
						italBody = CleanerUtils.cleanApostrophe(italBody);
						solrDoc.addField("partOfDocBody", italBody);
					}
					
					if(StringUtils.isNotBlank(part_title))
					{
							
						part_title = CleanerUtils.cleanApostrophe(part_title);
						part_title = StringUtils.normalizeSpace(part_title);
						solrDoc.addField("partTitle", "PART "+partNumber+" - "+part_title);
					}
					
					List<String> lhead = c_ac.getListOfSubHeaders();
					if(CollectionUtils.isNotEmpty(lhead))
					{
					
						for(String str:lhead)
						{
							str = CleanerUtils.removeLeadingNumber(str);
							str = CleanerUtils.cleanApostrophe(str);
							str = StringUtils.normalizeSpace(str);
							solrDoc.addField("listOfTitles",str);
						}
						
					}
					server.add(solrDoc);
					
				}
			}
			server.commit();
		}
	}
	
	public static void main(String[] args) throws SolrServerException, IOException
	{
		IndexCareActItal indItal = new IndexCareActItal();
		indItal.doI_CareActItalicizedBody(0,150);
	}
}
