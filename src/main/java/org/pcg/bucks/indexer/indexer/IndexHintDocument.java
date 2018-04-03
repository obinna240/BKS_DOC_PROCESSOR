package org.pcg.bucks.indexer.indexer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.pcg.bucks.careact.documentTypes.DocumentNotes;
import org.pcg.bucks.careact.documentUtils.Questions;
import org.pcg.bucks.careact.documentUtils.SubHeaders;
import org.pcg.bucks.indexer.DocumentContentRetriever;
import org.pcg.bucks.utils.CleanerUtils;

/**
 * 
 * @author oonyimadu
 *
 */
public class IndexHintDocument 
{

	private DocumentContentRetriever documentContentRetriever;
	
	/**
	 * 
	 */
	public IndexHintDocument()
	{
		documentContentRetriever = new DocumentContentRetriever();
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void doIndexHint(Integer start, Integer end) throws SolrServerException, IOException
	{
		List<DocumentNotes> ldoc = documentContentRetriever.getListOfDocumentNotes(start, end);
		
		String ss_url = documentContentRetriever.getIndexUrl1();
		HttpSolrServer server = new HttpSolrServer(ss_url);
		
		if(CollectionUtils.isNotEmpty(ldoc))
		{
			for(DocumentNotes dnote:ldoc)
			{
				
				List<Questions> lQuest = dnote.getListOfQuestions();
				
				if(CollectionUtils.isNotEmpty(lQuest))
				{
		
					int count = 0;
					for(Questions q:lQuest)
					{
						
						String hPrec = q.getQHeaderPrecision();
						
						if(hPrec.equals("High"))
						{
																				
							String questText = q.getQuestionText();
							String qBody = q.getQuestionBody();
							
							if(StringUtils.isNotBlank(questText))
							{
								SolrInputDocument solrDoc = new SolrInputDocument();
								
								questText = CleanerUtils.removeLeadingNumber(questText);
								questText = StringUtils.normalizeSpace(questText);
								
								questText = CleanerUtils.replaceFirstDot(questText);
								questText = CleanerUtils.removeLeadingNumber(questText);
								
								questText = CleanerUtils.cleanApostrophe(questText);
								questText = StringUtils.normalizeSpace(questText);
								qBody = CleanerUtils.cleanApostrophe(qBody);
								qBody = StringUtils.normalizeSpace(qBody);
								
								if(StringUtils.isNotBlank(qBody))
								{
									String arrBody[]  = qBody.split("\\s");
									if(arrBody.length>=40)
									{	
										//get and set partOfID
										Integer docNumber = dnote.getDocNumber();
										String partOfID = "FullBody_"+docNumber;
										solrDoc.addField("partOfID", partOfID);
										
										String id = "Hints_"+docNumber+"_"+count;
										
										solrDoc.addField("id", id);
										
										solrDoc.addField("title", questText);
										
										solrDoc.addField("body", qBody);
										
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
										solrDoc.addField("docType", "Hints");
										
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
													t= CleanerUtils.cleanApostrophe(t);
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
									}
								}
							}
						}
						else if(hPrec.equals("Low"))
						{
							
							String questText = q.getQuestionText();
							
							
							questText = CleanerUtils.removeLeadingNumber(questText);
							
							questText = StringUtils.normalizeSpace(questText);
							questText = CleanerUtils.cleanApostrophe(questText);
							questText = StringUtils.normalizeSpace(questText);
							String arr[]  = questText.split("\\s");
							
							if(arr.length<=10)
							{
								String context1 = q.getContext1();
								String context2 = q.getContext2();
								context1 = CleanerUtils.cleanApostrophe(context1);
								context2 = CleanerUtils.cleanApostrophe(context2);
								
								String qBody = context1+"\n"+questText+"\n"+context2;
								
								if(StringUtils.isNotBlank(qBody))
								{
									String arrBody[]  = qBody.split("\\s");
									if(arrBody.length>=50)
									{
										SolrInputDocument solrDoc = new SolrInputDocument();
										
										//get and set partOfID
										Integer docNumber = dnote.getDocNumber();
										
										String partOfID = "FullBody_"+docNumber;
										
										solrDoc.addField("partOfID", partOfID);
										
										String id = "Hints_"+docNumber+"_"+count;
																	
										solrDoc.addField("id", id);
										
										solrDoc.addField("title", questText);
									
										
										solrDoc.addField("body", qBody);
										
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
										solrDoc.addField("docType", "Hints");
										
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
									}
								}
							}
						}
						count++;
					}
					server.commit();
				}
				

			}
		}
		
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param args
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public static void main(String[] args) throws SolrServerException, IOException
	{
	
		//String a = ".This is a cool .thing";
		//String[] ab = a.split("\\s");
		//System.out.println(ab.length+" "+ab[3]);
		//a=a.replaceFirst("^\\.", "fas ");
		//System.out.println(a);
		
		IndexHintDocument iHD = new IndexHintDocument();
		iHD.doIndexHint(0, 45);
		
	}
	
}
