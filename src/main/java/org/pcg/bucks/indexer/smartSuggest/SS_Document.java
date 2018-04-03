package org.pcg.bucks.indexer.smartSuggest;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.pcg.bucks.careact.documentTypes.DocumentNotes;
import org.pcg.bucks.careact.documentUtils.Questions;
import org.pcg.bucks.careact.documentUtils.SubHeaders;
import org.pcg.bucks.indexer.DocumentContentRetriever;
import org.pcg.bucks.utils.CleanerUtils;

import com.mongodb.gridfs.GridFSDBFile;

import antlr.Utils;

/**
 * 
 * @author oonyimadu
 *
 */
public class SS_Document 
{
	private DocumentContentRetriever documentContentRetriever;
	
	
	public SS_Document()
	{
		documentContentRetriever = new DocumentContentRetriever();
	}
	
	public void doIndexWholeDocument(Integer start, Integer end) throws SolrServerException, IOException
	{
		List<DocumentNotes> ldoc = documentContentRetriever.getListOfDocumentNotes(start, end);
		
		String ss_url = documentContentRetriever.getIndexUrl1();
		HttpSolrServer server = new HttpSolrServer(ss_url);
		
		if(CollectionUtils.isNotEmpty(ldoc))
		{
			for(DocumentNotes dnote:ldoc)
			{
				SolrInputDocument solrDoc = new SolrInputDocument();
				
				//get and set id
				Integer docNumber = dnote.getDocNumber();
				if(docNumber!=null)
				{
					solrDoc.addField("id", "FullBody_"+docNumber);
					
										
				}
				
				
				
				String version = dnote.getVersion();
				String dateOfPub = dnote.getDateOfPublication();
				Date dateOfPubAsDate = dnote.getDateOfPublicationAsDate();
						
				//index document date
				if(StringUtils.isNotBlank(dateOfPub))
				{
					solrDoc.addField("dateOfPublicationString", dateOfPub);		
					solrDoc.addField("dateOfPublication", dateOfPubAsDate);
				}
												
				if(StringUtils.isNotBlank(version))
				{
					solrDoc.addField("docVersion", version);
				}
				
				//get documentGroup
				String docGroup = dnote.getType(); 
				
				if(StringUtils.isNotBlank(docGroup))
				{
					solrDoc.addField("docGroup", docGroup);
										
				}
				
				//set document type
				solrDoc.addField("docType", "FullBody");
				
				//get and set owner
				String docOwner = dnote.getDocument_Owner();
				docOwner = CleanerUtils.cleanApostrophe(docOwner);
				
				if(StringUtils.isNotBlank(docOwner))
				{
					docOwner = StringUtils.normalizeSpace(docOwner);
					solrDoc.addField("owner", docOwner);
										
				}
				
				
				
				//get and set author
				String author = dnote.getAuthor();
				author = CleanerUtils.cleanApostrophe(author);
				if(StringUtils.isNotBlank(author))
				{
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
				
				//get and set document Url
				String documentUrl = dnote.getFileName();
				if(StringUtils.isNotBlank(documentUrl))
				{
					documentUrl = StringUtils.substringAfterLast(documentUrl, "/");
					solrDoc.addField("documentUrl", documentUrl);
									
				}
				
				//get and set urls
				Set<String> urlsL = dnote.getListOfUrls();
				if(CollectionUtils.isNotEmpty(urlsL))
				{
					for(String t:urlsL)
					{
						if(StringUtils.isNotBlank(t))
						{
							solrDoc.addField("associatedUrls", t);
						}
					}
				}
				
				//get and set emails
				Set<String> emailsL = dnote.getListOfEmails();
				if(CollectionUtils.isNotEmpty(emailsL))
				{
					for(String t:emailsL)
					{
						if(StringUtils.isNotBlank(t))
						{
							solrDoc.addField("associatedEmails", t);
						}
					}
				}
				
				//get and set images
				//List<String> imagesL = dnote.getListOfImages();
				//if(CollectionUtils.isNotEmpty(imagesL))
				//{
				//	for(String t:imagesL)
				//	{
				//		if(StringUtils.isNotBlank(t))
				//		{
				//			solrDoc.addField("imageUrl", t);
				//		}
				//	}
				//}
				
				//get and set document title
				String title = dnote.getDocumentTitle();
				if(StringUtils.isNotBlank(title))
				{
					title = CleanerUtils.cleanApostrophe(title);
					title = StringUtils.normalizeSpace(title);
					solrDoc.addField("title", title);
					solrDoc.addField("dTitle", title);
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
							solrDoc.addField("title", t);
						}
					}
				}
				
				//get and set date of upload
				Date dateOfUpload = dnote.getDateOfUpLoad();
				if(dateOfUpload!=null)
				{
					solrDoc.addField("dateOfUpload",dateOfUpload);
				}
				
				//get and set keywords
				Set<String> importantWord = dnote.getImportantWords();
				if(CollectionUtils.isNotEmpty(importantWord))
				{
					for(String iWo:importantWord)
					{
						if(StringUtils.isNotBlank(iWo))
						{
							iWo = StringUtils.normalizeSpace(iWo);
							iWo = CleanerUtils.removeLeadingNumber(iWo);
							iWo = CleanerUtils.removePunctuation(iWo);
							iWo = CleanerUtils.removeLeadingNumber(iWo);
							iWo = CleanerUtils.cleanApostrophe(iWo);
							iWo = StringUtils.normalizeSpace(iWo);
							
							solrDoc.addField("keyword", iWo);
												
						}
					}
				}
				
				//get and set Body
				String body = dnote.getDocumentBody();
				if(StringUtils.isNotBlank(body))
				{
					body = CleanerUtils.cleanApostrophe(body);
					solrDoc.addField("body", body);
				}
				
				//get and set organization entities
				Set<String> setOfOrganizations = dnote.getListOfOrganizations();
				if(CollectionUtils.isNotEmpty(setOfOrganizations))
				{
					for(String org:setOfOrganizations)
					{
						org = StringUtils.normalizeSpace(org);
						if(StringUtils.isNotBlank(org))
						{
							org = CleanerUtils.cleanApostrophe(org);
							org = StringUtils.normalizeSpace(org);
							solrDoc.addField("organizationEntities", org);
												
						}
					}
				}
				
				//get and set person entities
				Set<String> setPerson = dnote.getListOfPersons();
				if(CollectionUtils.isNotEmpty(setPerson))
				{
					for(String sper:setPerson)
					{
						sper = StringUtils.normalizeSpace(sper);
						if(StringUtils.isNotBlank(sper))
						{
							sper = CleanerUtils.cleanApostrophe(sper);
							sper = StringUtils.normalizeSpace(sper);
							solrDoc.addField("personEntities", sper);
							
						}
					}
				}
				
				//get and set place entities
				Set<String> setOfLocations = dnote.getListOfLocations();
				if(CollectionUtils.isNotEmpty(setOfLocations))
				{
					for(String sl:setOfLocations)
					{
						sl = StringUtils.normalizeSpace(sl);
						if(StringUtils.isNotBlank(sl))
						{
							sl = CleanerUtils.cleanApostrophe(sl);
							sl = StringUtils.normalizeSpace(sl);
							solrDoc.addField("placeEntities", sl);
							
						}
					}
				}
				
				server.add(solrDoc);
				server.commit();
			}
		}
		
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void doSS_Document(Integer start, Integer end) throws SolrServerException, IOException
	{
		List<DocumentNotes> ldoc = documentContentRetriever.getListOfDocumentNotes(start, end);
		
		String ss_url = documentContentRetriever.getIndexUrl2();
		HttpSolrServer server = new HttpSolrServer(ss_url);
		
		if(CollectionUtils.isNotEmpty(ldoc))
		{
			for(DocumentNotes dnote:ldoc)
			{
				Set<String> setOfLocations = dnote.getListOfLocations();
				if(CollectionUtils.isNotEmpty(setOfLocations))
				{
					for(String sl:setOfLocations)
					{
						sl = StringUtils.normalizeSpace(sl);
						sl = CleanerUtils.cleanApostrophe(sl);
						if(StringUtils.isNotBlank(sl))
						{
							SolrInputDocument solrDoc = new SolrInputDocument();
							String id = sl+"_Place";
							solrDoc.addField("id", id);
							solrDoc.addField("entity", sl);
							solrDoc.addField("entity_stored", sl);
							solrDoc.addField("entityType", "Place");
							server.add(solrDoc);
						}
					}
				}
				
				String author = dnote.getAuthor();
				author = CleanerUtils.cleanApostrophe(author);
				if(StringUtils.isNotBlank(author))
				{
					SolrInputDocument solrDoc = new SolrInputDocument();
					String id = author+"_Author";
					
					solrDoc.addField("id", id);
					solrDoc.addField("entity", author);
					solrDoc.addField("entity_stored", author);
					solrDoc.addField("entityType", "Author");
					server.add(solrDoc);
				}
				
				Set<String> author2 = dnote.getAuthor2();
				if(CollectionUtils.isNotEmpty(author2))
				{
					for(String authorr:author2)
					{
						SolrInputDocument solrDoc = new SolrInputDocument();
						authorr = CleanerUtils.cleanApostrophe(authorr);
						String id = authorr+"_Author";
						
						solrDoc.addField("id", id);
						solrDoc.addField("entity", authorr);
						solrDoc.addField("entity_stored", authorr);
						solrDoc.addField("entityType", "Author");
						server.add(solrDoc);
					}
				}
				
				Set<String> setOfOrganizations = dnote.getListOfOrganizations();
				if(CollectionUtils.isNotEmpty(setOfOrganizations))
				{
					for(String org:setOfOrganizations)
					{
						org = StringUtils.normalizeSpace(org);
						if(StringUtils.isNotBlank(org))
						{
							SolrInputDocument solrDoc = new SolrInputDocument();
							org = CleanerUtils.cleanApostrophe(org);
							String id = org+"_Organization";
							solrDoc.addField("id", id);
							solrDoc.addField("entity", org);
							solrDoc.addField("entity_stored", org);
							solrDoc.addField("entityType", "Organization");
							server.add(solrDoc);
						}
					}
				}
				
				Set<String> setPerson = dnote.getListOfPersons();
				if(CollectionUtils.isNotEmpty(setPerson))
				{
					for(String sper:setPerson)
					{
						sper = StringUtils.normalizeSpace(sper);
						sper = CleanerUtils.cleanApostrophe(sper);
						if(StringUtils.isNotBlank(sper))
						{
							SolrInputDocument solrDoc = new SolrInputDocument();
							String id = sper+"_Person";
							solrDoc.addField("id", id);
							solrDoc.addField("entity", sper);
							solrDoc.addField("entity_stored", sper);
							solrDoc.addField("entityType", "Person");
							server.add(solrDoc);
						}
					}
				}
				
				String docTitle = dnote.getDocumentTitle();
				if(StringUtils.isNotBlank(docTitle))
				{
					docTitle = StringUtils.normalizeSpace(docTitle);
					docTitle = CleanerUtils.cleanApostrophe(docTitle);
					SolrInputDocument solrDoc = new SolrInputDocument();
					String id = docTitle+"_Document";
					solrDoc.addField("id", id);
					solrDoc.addField("entity", docTitle);
					solrDoc.addField("entity_stored", docTitle);
					solrDoc.addField("entityType", "Document");
					server.add(solrDoc);
				}
				
				Set<String> setTitle = dnote.getOtherTitle();
				if(CollectionUtils.isNotEmpty(setTitle))
				{
					for(String stTitle:setTitle)
					{
						if(StringUtils.isNotBlank(stTitle))
						{
							stTitle = StringUtils.normalizeSpace(stTitle);
							stTitle = CleanerUtils.cleanApostrophe(stTitle);
							SolrInputDocument solrDoc = new SolrInputDocument();
							String id = stTitle+"_Document";
							solrDoc.addField("id", id);
							solrDoc.addField("entity", stTitle);
							solrDoc.addField("entity_stored", stTitle);
							solrDoc.addField("entityType", "Document");
							server.add(solrDoc);
						}
					}
				}
				
				Set<String> importantWord = dnote.getImportantWords();
				if(CollectionUtils.isNotEmpty(importantWord))
				{
					for(String iWo:importantWord)
					{
						if(StringUtils.isNotBlank(iWo))
						{
							iWo = StringUtils.normalizeSpace(iWo);
							iWo = CleanerUtils.removeLeadingNumber(iWo);
							iWo = CleanerUtils.removePunctuation(iWo);
							iWo = CleanerUtils.removeLeadingNumber(iWo);
							
							iWo = CleanerUtils.cleanApostrophe(iWo);
							
							SolrInputDocument solrDoc = new SolrInputDocument();
							String id = iWo+"_Keyword";
							solrDoc.addField("id", id);
							solrDoc.addField("entity", iWo);
							solrDoc.addField("entity_stored", iWo);
							solrDoc.addField("entityType", "Keyword");
							server.add(solrDoc);
						}
					}
				}
				
				List<Questions> lQuest = dnote.getListOfQuestions();
				if(CollectionUtils.isNotEmpty(lQuest))
				{
					for(Questions q:lQuest)
					{
						String hPrec = q.getQHeaderPrecision();
						if(hPrec.equals("High"))
						{
							String questText = q.getQuestionText();
							if(StringUtils.isNotBlank(questText))
							{
								questText = CleanerUtils.removeLeadingNumber(questText);
								questText = StringUtils.normalizeSpace(questText);
								
								questText = CleanerUtils.replaceFirstDot(questText);
								questText = CleanerUtils.removeLeadingNumber(questText);
								
								questText = CleanerUtils.cleanApostrophe(questText);
								
								String qBody = q.getQuestionBody();
								qBody = CleanerUtils.cleanApostrophe(qBody);
								qBody = StringUtils.normalizeSpace(qBody);
								if(StringUtils.isNotBlank(qBody))
								{
									String arrBody[]  = qBody.split("\\s");
									
									if(arrBody.length>=40)
									{
										SolrInputDocument solrDoc = new SolrInputDocument();
										String id = questText+"_Hints";
										solrDoc.addField("id", id);
										solrDoc.addField("entity", questText);
										solrDoc.addField("entity_stored", questText);
										solrDoc.addField("entityType", "Hints");
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
										String id = questText+"_Hints";
										solrDoc.addField("id", id);
										solrDoc.addField("entity", questText);
										solrDoc.addField("entity_stored", questText);
										solrDoc.addField("entityType", "Hints");
										server.add(solrDoc);
									}
								}
							}
						}
					}
				}
				
				List<SubHeaders> sub1 = dnote.getListOfSubHeaders();
				if(CollectionUtils.isNotEmpty(sub1))
				{
					for(SubHeaders subheader:sub1)
					{
						String subH  = subheader.getSubHeader();
						String body = subheader.getBody();
						
						if(StringUtils.isNotBlank(subH)&&StringUtils.isNotBlank(body))
						{
							String sn = CleanerUtils.removeLeadingNumber(subH);
							String sn1 = CleanerUtils.removePunctuation(subH);
							
							sn = StringUtils.removeEnd(sn, ":");
							sn = StringUtils.normalizeSpace(sn);
							sn = StringUtils.removeEnd(sn, "?");
							sn = StringUtils.normalizeSpace(sn);
							sn = StringUtils.removeEnd(sn, "!");
							sn = StringUtils.normalizeSpace(sn);
							sn = CleanerUtils.replaceFirstDot(sn);
							sn = CleanerUtils.removeLeadingNumber(sn);	
							sn = CleanerUtils.cleanApostrophe(sn);
							
							String sb = CleanerUtils.removeLeadingNumber(body);
							sb = CleanerUtils.removePunctuation(body);
							sb = CleanerUtils.cleanApostrophe(sb);
							
							if(StringUtils.isNotBlank(sn1) && StringUtils.isNotBlank(sb))
							{
								int s1 = CleanerUtils.getTextSize(sn1);
								int s2 = CleanerUtils.getTextSize(sb);
								if(s1>2 && s1<10)
								{
									if(s2>20)
									{
										SolrInputDocument solrDoc = new SolrInputDocument();
										String id = sn+"_Topic";
										solrDoc.addField("id", id);
										solrDoc.addField("entity", sn);
										solrDoc.addField("entity_stored", sn);
										solrDoc.addField("entityType", "Topic");
										server.add(solrDoc);
									}
								}
							}
						}
					}
				}
				
				List<SubHeaders> sub2 = dnote.getListOfSubHeaders2();
				if(CollectionUtils.isNotEmpty(sub2))
				{
					for(SubHeaders subheader:sub2)
					{
						String subH  = subheader.getSubHeader();
						String body = subheader.getBody();
						
						if(StringUtils.isNotBlank(subH)&&StringUtils.isNotBlank(body))
						{
							String sn = CleanerUtils.removeLeadingNumber(subH);
							
							sn = StringUtils.removeEnd(sn, ":");
							sn = StringUtils.normalizeSpace(sn);
							sn = StringUtils.removeEnd(sn, "?");
							sn = StringUtils.normalizeSpace(sn);
							sn = StringUtils.removeEnd(sn, "!");
							sn = StringUtils.normalizeSpace(sn);
							sn = CleanerUtils.replaceFirstDot(sn);
							sn = CleanerUtils.removeLeadingNumber(sn);
							sn = CleanerUtils.cleanApostrophe(sn);
							
							String sn1 = CleanerUtils.removePunctuation(subH);
							
							String sb = CleanerUtils.removeLeadingNumber(body);
							sb = CleanerUtils.removePunctuation(body);
							sb = CleanerUtils.cleanApostrophe(sb);
							
							if(StringUtils.isNotBlank(sn1) && StringUtils.isNotBlank(sb))
							{
								int s1 = CleanerUtils.getTextSize(sn1);
								int s2 = CleanerUtils.getTextSize(sb);
								if(s1>2 && s1<10)
								{
									if(s2>20)
									{
										SolrInputDocument solrDoc = new SolrInputDocument();
										String id = sn+"_Topic";
										solrDoc.addField("id", id);
										solrDoc.addField("entity", sn);
										solrDoc.addField("entity_stored", sn);
										solrDoc.addField("entityType", "Topic");
										server.add(solrDoc);
									}
								}
							}
						}
					}
				}
				
			}
			server.commit();
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
			
		SS_Document ssD = new SS_Document();
		
		//ssD.doIndexWholeDocument(8, 44);
		ssD.doSS_Document(0, 45);
	}
	
}
