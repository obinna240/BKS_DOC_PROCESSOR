package org.pcg.bucks.indexer.indexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.pcg.bucks.careact.documentTypes.DocumentNotes;
import org.pcg.bucks.careact.documentUtils.SubHeaders;
import org.pcg.bucks.indexer.DocumentContentRetriever;
import org.pcg.bucks.utils.CleanerUtils;

/**
 * 
 * @author oonyimadu
 *
 */
public class IndexTopicDocument
{
	private DocumentContentRetriever documentContentRetriever;
	
	public IndexTopicDocument()
	{
		documentContentRetriever = new DocumentContentRetriever();
	}
	
	public void doIndexTopic(Integer start, Integer end) throws SolrServerException, IOException
	{
		List<DocumentNotes> ldoc = documentContentRetriever.getListOfDocumentNotes(start, end);
		
		String ss_url = documentContentRetriever.getIndexUrl1();
		HttpSolrServer server = new HttpSolrServer(ss_url);
		
		if(CollectionUtils.isNotEmpty(ldoc))
		{
			
			int count = 0;
			
			for(DocumentNotes dnote:ldoc)
			{
				List<SubHeaders> subheaders1 = dnote.getListOfSubHeaders();
				List<SubHeaders> subheaders2 = dnote.getListOfSubHeaders2();
				
				if(CollectionUtils.isEmpty(subheaders1))
				{
					subheaders1 = new ArrayList<SubHeaders>();
				}
				
				if(CollectionUtils.isEmpty(subheaders2))
				{
					subheaders2 = new ArrayList<SubHeaders>();
				}
				
				
				Collection<SubHeaders> sub = CollectionUtils.union(subheaders1, subheaders2);
				
				Integer i  = sub.size();
				
				
				if(CollectionUtils.isNotEmpty(sub))
				{
					//get and set partOfID
					Integer docNumber = dnote.getDocNumber();
					String partOfID = "FullBody_"+docNumber;
					
					
					
					
					for(SubHeaders sus:sub)
					{
						
						String subH  = sus.getSubHeader();
						
						
						String body = sus.getBody();			
						
						if(StringUtils.isNotBlank(subH)&&StringUtils.isNotBlank(body))
						{
							
							String sn = CleanerUtils.removeLeadingNumber(subH);
							String sn1 = CleanerUtils.removePunctuation(subH);
							sn1 = CleanerUtils.cleanApostrophe(sn1);
							
							
							sn = StringUtils.removeEnd(sn, ":");
							sn = StringUtils.normalizeSpace(sn);
							sn = StringUtils.removeEnd(sn, "?");
							sn = StringUtils.normalizeSpace(sn);
							sn = StringUtils.removeEnd(sn, "!");
							sn = StringUtils.normalizeSpace(sn);
							sn = CleanerUtils.replaceFirstDot(sn);
							sn = CleanerUtils.removeLeadingNumber(sn);
							sn = CleanerUtils.cleanApostrophe(sn);
							sn = StringUtils.normalizeSpace(sn);
							
							String sb = CleanerUtils.removeLeadingNumber(body);
							sb = CleanerUtils.removePunctuation(body);
							sb = CleanerUtils.cleanApostrophe(sb);
							sb = StringUtils.normalizeSpace(sb);
							
							if(StringUtils.isNotBlank(sn1) && StringUtils.isNotBlank(sb))
							{
								int s1 = CleanerUtils.getTextSize(sn1);
								int s2 = CleanerUtils.getTextSize(sb);
								if(s1>2 && s1<10)
								{
									if(s2>20)
									{
										
										SolrInputDocument solrDoc = new SolrInputDocument();
										solrDoc.addField("partOfID", partOfID);
										
										String id = "Topic_"+docNumber+"_"+count;
										solrDoc.addField("id", id);
										
										FileUtils.write(new File("written4"), "\n"+sn, true);
										
										
										solrDoc.addField("title", sn);
										
										solrDoc.addField("body", sb);
										
										String docGroup = dnote.getType(); 
										if(StringUtils.isNotBlank(docGroup))
										{
											solrDoc.addField("docGroup", docGroup);
										}
										
										//get and set Body
										String docBody = dnote.getDocumentBody();
										
										if(StringUtils.isNotBlank(docBody))
										{
											docBody = CleanerUtils.cleanApostrophe(docBody);
											solrDoc.addField("partOfDocBody", docBody);
										}
										
										//get and set date of upload
										Date dateOfUpload = dnote.getDateOfUpLoad();
										if(dateOfUpload!=null)
										{
											solrDoc.addField("dateOfUpload",dateOfUpload);
										}
										
										//set document type
										solrDoc.addField("docType", "Topic");
										
										//get and set document title
										String title = dnote.getDocumentTitle();
										if(StringUtils.isNotBlank(title))
										{
											title = CleanerUtils.cleanApostrophe(title);
											title = StringUtils.normalizeSpace(title);
											solrDoc.addField("partOfDocTitle", title);
										}
										
										Set<String> title2 = dnote.getOtherTitle();
										if(CollectionUtils.isNotEmpty(title2))
										{
											for(String t:title2)
											{
												if(StringUtils.isNotBlank(t))
												{
													t = CleanerUtils.cleanApostrophe(t);
													t = StringUtils.normalizeSpace(t);
													solrDoc.addField("partOfDocTitle", t);
												}
											}
										}
										
										//get and set document Url
										String documentUrl = dnote.getFileName();
										if(StringUtils.isNotBlank(documentUrl))
										{
											documentUrl = StringUtils.substringAfterLast(documentUrl, "/");
											solrDoc.addField("documentUrl", documentUrl);
															
										}
										
										//get and set owner
										String docOwner = dnote.getDocument_Owner();
										if(StringUtils.isNotBlank(docOwner))
										{
											docOwner = CleanerUtils.cleanApostrophe(docOwner);
											docOwner = StringUtils.normalizeSpace(docOwner);
											solrDoc.addField("owner", docOwner);
																
										}
										
										//get and set author
										String author = dnote.getAuthor();
										if(StringUtils.isNotBlank(author))
										{
											author = CleanerUtils.cleanApostrophe(author);
											author = StringUtils.normalizeSpace(author);
											solrDoc.addField("author", author);
																
										}
										
										Set<String> author2 = dnote.getAuthor2();
										if(CollectionUtils.isNotEmpty(author2))
										{
											for(String authorr:author2)
											{
												
												
												authorr = CleanerUtils.cleanApostrophe(authorr);
												authorr = StringUtils.normalizeSpace(authorr);
												solrDoc.addField("author", authorr);
												
											}
										}
										
										server.add(solrDoc);
										
										count++;
									}
									server.commit();
								}
							}
						}
						
						
					}
				}
			}
		}
	}
	
	


	
	public static void main(String[] args) throws SolrServerException, IOException
	{
		IndexTopicDocument idd = new IndexTopicDocument();
		idd.doIndexTopic(26,26);
	}
}
