package org.pcg.bucks.processors;


import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.pcg.bucks.careact.documentTypes.CareActContent;
import org.pcg.bucks.careact.documentTypes.CareActItalicizedBody;
import org.pcg.bucks.careact.documentTypes.CareActSchedule;
import org.pcg.bucks.careact.documentTypes.DocumentNotes;
import org.pcg.bucks.careact.documentUtils.Content;
import org.pcg.bucks.careact.documentUtils.Questions;
import org.pcg.bucks.careact.documentUtils.SubHeaders;
import org.pcg.bucks.careact.documentUtils.TableContent;
import org.pcg.bucks.utils.Loader;
import org.pcg.bucks.utils.MSWordImageExtractor;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Utils;
import gate.creole.ResourceInstantiationException;
import gate.util.DocumentProcessor;
import gate.util.GateException;

/**
 * 
 * @author oonyimadu
 *
 */
public class DocProcessor 
{

	DocumentProcessor processor;
	Loader loader;
	Document document;
	String documentUrl;
	String documentType;
	

	/**
	 * 
	 * @param documentType
	 * @param content
	 */
	public DocProcessor(String documentType, Content content)
	{
		
		String dUrl =  content.getDocumentUrl();
		this.documentType = documentType;
		this.documentUrl = dUrl;
		loader = new Loader(documentType);
		processor = loader.getProcessor();
		
		document = createDocument();
	}
	
	/**
	 * 
	 * @param documentType
	 * @param documentUrl
	 */
	public DocProcessor(String documentType, String documentUrl) 
	{
		this.documentType = documentType;
		this.documentUrl = documentUrl;
		loader = new Loader(documentType);
		processor = loader.getProcessor();
		document = createDocument();

	}
	
	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
	
	
	/**
	 * 
	 * @return Document 
	 */
	private Document createDocument()
	{
		try 
		{
			document = Factory.newDocument(new URL(documentUrl), "UTF-8");
		} 
		catch (ResourceInstantiationException e)
		{
						
			e.printStackTrace();
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
			
		}
		return document;
	}
	
	public DocumentProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(DocumentProcessor processor) {
		this.processor = processor;
	}
	
	public Document getDoc() {
		return document;
	}

	public void setDoc(Document doc) {
		this.document = doc;
	}
	
	/**
	 * 
	 */
	public void doAnalysis() 
	{
		if(document!=null)
		{
			try 
			{
				processor.processDocument(document);
				AnnotationSet defaultSet = document.getAnnotations();
				
				if(documentType.equals("care act"))
				{
									
					//processes care act schedules
					//processSchedules(defaultSet);
					
					//processes care act italicizedBody
					//processCareActItalHeader(defaultSet);
					
					
					//processes the care act
					processCareActContent(defaultSet);
				}
				else if(documentType.equals("Other"))
				{
					//process the document
					processDocument(defaultSet);
				}
			
			} 
			catch (GateException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param content
	 */
	public Integer doAnalysis(Content content) 
	{
		Integer count = null;
		if(document!=null)
		{
			try 
			{
				processor.processDocument(document);
				AnnotationSet defaultSet = document.getAnnotations();
				
				//if(documentType.equals("care act"))
				//{
									
					
					//processCareActContent(defaultSet);
				//}
				if(documentType.equals("Other"))
				{
					
					count = processDocument(defaultSet, content);
				}
			
			} 
			catch (GateException e) 
			{
				e.printStackTrace();
			}
		}
		return count;
	}
	
	/**
	 * 
	 * @param defaultSet
	 * @param content
	 */
	private Integer processDocument(AnnotationSet defaultSet, Content content)
	{
				
		AnnotationSet organizationSet = defaultSet.get("Organization");
		AnnotationSet locationSet = defaultSet.get("Location");
		AnnotationSet personSet = defaultSet.get("Person");
		
		Set<String> set = new HashSet<String>();
		set.add("href");
		AnnotationSet urls = defaultSet.get("a", set);
		AnnotationSet table = defaultSet.get("TableDef");
		AnnotationSet imgs = defaultSet.get("img");
		AnnotationSet dTitle1 = defaultSet.get("title");
		
		AnnotationSet questionSet = defaultSet.get("Question");
		AnnotationSet boldSet = defaultSet.get("BoldHeader");
		AnnotationSet important_bold_words = defaultSet.get("Important_Bold_Words");
		AnnotationSet otherImportantWords = defaultSet.get("OtherImportantWord");
		
		Set<String> desubset = new HashSet<String>();
		desubset.add("body");
		desubset.add("text");
		AnnotationSet subHeader2 = defaultSet.get("SubHeader", desubset);
		
		FeatureMap dmap = document.getFeatures();
		String DcCreator = (String) dmap.get("DC:CREATOR");
		String DcTitle = (String) dmap.get("DC:TITLE");
		String metaAuthor = (String) dmap.get("META:AUTHOR");
		String filename = (String) dmap.get("gate.SourceURL");
		
		DocumentNotes ds = new DocumentNotes();
		
		String fullDocumentContent = document.getContent().toString();
		
		
		long lsize = loader.getMongoOps().getCollection("DocumentNotes").count();
		Integer count = (int) lsize;
		count = count+1;
		
		ds.setDocument_Owner("Bucks");
		ds.setType(content.getDocumentCategory());
		ds.setDocNumber(count);
		
		ds.setDateOfUpLoad(new Date());
		ds.setFileName(filename);
		ds.setDocumentBody(fullDocumentContent);
		ds.setDocumentTitle(content.getDocumentName());
		ds.setDateOfPublication(content.getDateOfPublication());
		
		List<String> authListss = content.getAuthor();
		if(CollectionUtils.isNotEmpty(authListss))
		{
			Set<String> sofAuthors = new HashSet<String>(authListss);
			ds.setAuthor2(sofAuthors);
		}
		ds.setVersion(content.getVersion());
		
		ds.setAuthor(metaAuthor);
		ds.setCreator(DcCreator);
						
		if(CollectionUtils.isNotEmpty(organizationSet))
		{
			Set<String> orgSet = new HashSet<String>();
			for(Annotation orgAnn:organizationSet)
			{
				String organiation =  Utils.cleanStringFor(document, orgAnn);
				orgSet.add(organiation);
			}
			ds.setListOfOrganizations(orgSet);
		}
		
		if(CollectionUtils.isNotEmpty(locationSet))
		{
			Set<String> locSet = new HashSet<String>();
			for(Annotation locAnn:locationSet)
			{
				String location =  Utils.cleanStringFor(document,locAnn);
				locSet.add(location);
			}
			ds.setListOfLocations(locSet);
		}
		
		if(CollectionUtils.isNotEmpty(personSet))
		{
			Set<String> pSet = new HashSet<String>();
			for(Annotation pAnn:personSet)
			{
				String pensonn =  Utils.cleanStringFor(document,pAnn);
				pSet.add(pensonn);
			}
			ds.setListOfPersons(pSet);
		}
		
		if(CollectionUtils.isNotEmpty(urls))
		{
			Set<String> urlSet = new HashSet<String>();
			Set<String> emailSet = new HashSet<String>();
			
			for(Annotation pUrl:urls)
			{
				String urrl =  Utils.cleanStringFor(document,pUrl);
				urrl = StringUtils.normalizeSpace(urrl);
				
				
				if(StringUtils.containsIgnoreCase(urrl, "mailto"))
				{
					String email= StringUtils.substringAfter(urrl,"mailto:");
					email = StringUtils.normalizeSpace(email);
					emailSet.add(email);
				}
				else if(StringUtils.containsIgnoreCase(urrl, "http://"))
				{
					urlSet.add(urrl);
				}
				
				
			}
			ds.setListOfUrls(urlSet);
			ds.setListOfEmails(emailSet);
		}
		
		if(CollectionUtils.isNotEmpty(table))
		{
			List<TableContent> lcontent = new ArrayList<TableContent>();
			for(Annotation tAnn:table)
			{
				FeatureMap tmap = tAnn.getFeatures();
				String rowValue= (String) tmap.get("rowString");
				String tableId = (String) tmap.get("tableId");
				String tableValue = (String) tmap.get("tdValue");
				String tableContent = (String) tmap.get("tableContent");
				
				TableContent tbContent = new TableContent();
				
				tbContent.setTableId(tableId);
				tbContent.setRowValue(rowValue);
				tbContent.setTableDefValue(tableValue);
				tbContent.setFullTableContent(tableContent);
				
				lcontent.add(tbContent);
			}
			ds.setTableContent(lcontent);
		}
		
		if(CollectionUtils.isNotEmpty(imgs))
		{
			List<String> imageContentList = new ArrayList<String>();
			for(Annotation image:imgs)
			{
				FeatureMap imap = image.getFeatures();
				String imageSrc = (String) imap.get("src");
						
				
				imageContentList.add(imageSrc);
			}
			ds.setListOfImages(imageContentList);
		}
		
	
		if(CollectionUtils.isNotEmpty(dTitle1))
		{
			Set<String> docTile = new HashSet<String>();
			for(Annotation tile:dTitle1)
			{
				String title1 = Utils.cleanStringFor(document, tile);
				if(StringUtils.isNotBlank(title1))
				{
					docTile.add(title1);
				}
			}
			//process filename for doc Title
			//docTile.add("Schools HS Manual");
			ds.setOtherTitle(docTile);
		}
		
		if(CollectionUtils.isNotEmpty(questionSet))
		{
			List<Questions> questionL = new ArrayList<Questions>();
			for(Annotation qSet:questionSet)
			{
				Questions question = new Questions();
				FeatureMap Qfmap = qSet.getFeatures();
				String context1 = "";
				String context2 = "";
				String qHeaderPrecision = (String) Qfmap.get("QHeaderPrecision");
				question.setQHeaderPrecision(qHeaderPrecision);
				
				String qHeaderPrecision2 = (String) Qfmap.get("QHeaderPrecision2");
				question.setQHeaderPrecision2(qHeaderPrecision2);
				
				List<String> contextList = (List<String>) Qfmap.get("context");
				
				if(CollectionUtils.isNotEmpty(contextList))
				{
					if(contextList.size()==2)
					{
						context1 = contextList.get(1);
						context2 = contextList.get(0);
						
					}
					else if(contextList.size()< 2)
					{
						context1 = contextList.get(0);
						
					}
				}
				question.setContext1(context1);
				question.setContext2(context2);
				
				String questionText = (String) Qfmap.get("text");
				question.setQuestionText(questionText);
				
				String qBody = (String) Qfmap.get("Qbody");
				question.setQuestionBody(qBody);
				
				List<String> bodlist = (List<String>) Qfmap.get("bodyAsList");
				if(CollectionUtils.isNotEmpty(bodlist))
				{
					String bodyAsList= writeListToString(bodlist);
					question.setQuestionBodyAsList(bodyAsList);
				}
				List<String> limpText = (List<String>) Qfmap.get("listOfImportantText");
				question.setListOfImportantQuestions(limpText);
				
				List<String> limpQ = (List<String>) Qfmap.get("listOfImportantQuestions");
				question.setListOfImportantQuestions(limpQ);
				
				questionL.add(question);
			}
			ds.setListOfQuestions(questionL);
		}
		
		Set<String> impoWords = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(important_bold_words))
		{
			
			for(Annotation impWord:important_bold_words)
			{
				String iiword = Utils.cleanStringFor(document, impWord);
				impoWords.add(iiword);
			}
			
		}
		
		if(CollectionUtils.isNotEmpty(otherImportantWords))
		{
			for(Annotation impWord:otherImportantWords)
			{
				String iiword = Utils.cleanStringFor(document, impWord);
				impoWords.add(iiword);
			}
			
		}
		ds.setImportantWords(impoWords);
		
		if(CollectionUtils.isNotEmpty(subHeader2))
		{
			List<SubHeaders> listOfSubHeaders2 = new ArrayList<SubHeaders>();
			for(Annotation notbld:subHeader2)
			{
				SubHeaders subH = new SubHeaders();
				FeatureMap setMap = notbld.getFeatures();
				
				String subHeader = (String) setMap.get("text");
				subH.setSubHeader(subHeader);
				
				String body = (String) setMap.get("body");
				subH.setBody(body);
				listOfSubHeaders2.add(subH);
			}
			ds.setListOfSubHeaders2(listOfSubHeaders2);
		}
		
		if(CollectionUtils.isNotEmpty(boldSet))
		{
			List<SubHeaders> listOfSubHeaders = new ArrayList<SubHeaders>();
			for(Annotation bld:boldSet)
			{
				SubHeaders subH = new SubHeaders();
				FeatureMap bmap = bld.getFeatures();
				String subHeader = (String) bmap.get("text");
				subH.setSubHeader(subHeader);
				
				String body = (String) bmap.get("body");
				subH.setBody(body);
				
				List<String> bodyAsLists = (List<String>) bmap.get("bodyAsList");
				if(CollectionUtils.isNotEmpty(bodyAsLists))
				{
					String bodyAsLst= writeListToString(bodyAsLists);
					subH.setBodyAsList(bodyAsLst);
				}
				
				String type = (String) bmap.get("type");
				subH.setType(type);
				
				List<String> limpText = (List<String>) bmap.get("listOfImportantText");
				subH.setListOfImportantQuestions(limpText);
				
				List<String> limpQ = (List<String>) bmap.get("listOfImportantQuestions");
				subH.setListOfImportantQuestions(limpQ);
				
				listOfSubHeaders.add(subH);
				
			
				
			}
			ds.setListOfSubHeaders(listOfSubHeaders);
		}
		
		
		loader.getMongoOps().save(ds);
		Factory.deleteResource(document);
		
		return count;
		
	}
	
	
	
	
	
	
	private void processDocument(AnnotationSet defaultSet) 
	{
		
		
		AnnotationSet organizationSet = defaultSet.get("Organization");
		AnnotationSet locationSet = defaultSet.get("Location");
		AnnotationSet personSet = defaultSet.get("Person");
		
		Set<String> set = new HashSet<String>();
		set.add("href");
		AnnotationSet urls = defaultSet.get("a", set);
		AnnotationSet table = defaultSet.get("TableDef");
		AnnotationSet imgs = defaultSet.get("img");
		AnnotationSet dTitle1 = defaultSet.get("title");
		
		AnnotationSet questionSet = defaultSet.get("Question");
		AnnotationSet boldSet = defaultSet.get("BoldHeader");
		AnnotationSet important_bold_words = defaultSet.get("Important_Bold_Words");
		AnnotationSet otherImportantWords = defaultSet.get("OtherImportantWord");
		
		Set<String> desubset = new HashSet<String>();
		desubset.add("body");
		desubset.add("text");
		AnnotationSet subHeader2 = defaultSet.get("SubHeader", desubset);
		
		FeatureMap dmap = document.getFeatures();
		String DcCreator = (String) dmap.get("DC:CREATOR");
		String DcTitle = (String) dmap.get("DC:TITLE");
		String metaAuthor = (String) dmap.get("META:AUTHOR");
		String filename = (String) dmap.get("gate.SourceURL");
		
		DocumentNotes ds = new DocumentNotes();
		
		String fullDocumentContent = document.getContent().toString();
		
		Integer count = 400;
		ds.setDocument_Owner("Bucks");
		ds.setType("Policy");
		ds.setDocNumber(count);
		
		ds.setDateOfUpLoad(new Date());
		ds.setFileName(filename);
		ds.setDocumentBody(fullDocumentContent);
		ds.setDocumentTitle(DcTitle);
		ds.setAuthor(metaAuthor);
		ds.setCreator(DcCreator);
		
		
		
		if(CollectionUtils.isNotEmpty(organizationSet))
		{
			Set<String> orgSet = new HashSet<String>();
			for(Annotation orgAnn:organizationSet)
			{
				String organiation =  Utils.cleanStringFor(document, orgAnn);
				orgSet.add(organiation);
			}
			ds.setListOfOrganizations(orgSet);
		}
		
		if(CollectionUtils.isNotEmpty(locationSet))
		{
			Set<String> locSet = new HashSet<String>();
			for(Annotation locAnn:locationSet)
			{
				String location =  Utils.cleanStringFor(document,locAnn);
				locSet.add(location);
			}
			ds.setListOfLocations(locSet);
		}
		
		if(CollectionUtils.isNotEmpty(personSet))
		{
			Set<String> pSet = new HashSet<String>();
			for(Annotation pAnn:personSet)
			{
				String pensonn =  Utils.cleanStringFor(document,pAnn);
				pSet.add(pensonn);
			}
			ds.setListOfPersons(pSet);
		}
		
		if(CollectionUtils.isNotEmpty(urls))
		{
			Set<String> urlSet = new HashSet<String>();
			Set<String> emailSet = new HashSet<String>();
			
			for(Annotation pUrl:urls)
			{
				String urrl =  Utils.cleanStringFor(document,pUrl);
				urrl = StringUtils.normalizeSpace(urrl);
				
				
				if(StringUtils.containsIgnoreCase(urrl, "mailto"))
				{
					String email= StringUtils.substringAfter(urrl,"mailto:");
					email = StringUtils.normalizeSpace(email);
					emailSet.add(email);
				}
				else if(StringUtils.containsIgnoreCase(urrl, "http://"))
				{
					urlSet.add(urrl);
				}
				
				
			}
			ds.setListOfUrls(urlSet);
			ds.setListOfEmails(emailSet);
		}
		
		if(CollectionUtils.isNotEmpty(table))
		{
			List<TableContent> lcontent = new ArrayList<TableContent>();
			for(Annotation tAnn:table)
			{
				FeatureMap tmap = tAnn.getFeatures();
				String rowValue= (String) tmap.get("rowString");
				String tableId = (String) tmap.get("tableId");
				String tableValue = (String) tmap.get("tdValue");
				String tableContent = (String) tmap.get("tableContent");
				
				TableContent tbContent = new TableContent();
				
				tbContent.setTableId(tableId);
				tbContent.setRowValue(rowValue);
				tbContent.setTableDefValue(tableValue);
				tbContent.setFullTableContent(tableContent);
				
				lcontent.add(tbContent);
			}
			ds.setTableContent(lcontent);
		}
		
		if(CollectionUtils.isNotEmpty(imgs))
		{
			List<String> imageContentList = new ArrayList<String>();
			for(Annotation image:imgs)
			{
				FeatureMap imap = image.getFeatures();
				String imageSrc = (String) imap.get("src");
						
				
				imageContentList.add(imageSrc);
			}
			ds.setListOfImages(imageContentList);
		}
		
	
		if(CollectionUtils.isNotEmpty(dTitle1))
		{
			Set<String> docTile = new HashSet<String>();
			for(Annotation tile:dTitle1)
			{
				String title1 = Utils.cleanStringFor(document, tile);
				if(StringUtils.isNotBlank(title1))
				{
					docTile.add(title1);
				}
			}
			//process filename for doc Title
			//docTile.add("Schools HS Manual");
			ds.setOtherTitle(docTile);
		}
		
		if(CollectionUtils.isNotEmpty(questionSet))
		{
			List<Questions> questionL = new ArrayList<Questions>();
			for(Annotation qSet:questionSet)
			{
				Questions question = new Questions();
				FeatureMap Qfmap = qSet.getFeatures();
				String context1 = "";
				String context2 = "";
				String qHeaderPrecision = (String) Qfmap.get("QHeaderPrecision");
				question.setQHeaderPrecision(qHeaderPrecision);
				
				String qHeaderPrecision2 = (String) Qfmap.get("QHeaderPrecision2");
				question.setQHeaderPrecision2(qHeaderPrecision2);
				
				List<String> contextList = (List<String>) Qfmap.get("context");
				
				if(CollectionUtils.isNotEmpty(contextList))
				{
					if(contextList.size()==2)
					{
						context1 = contextList.get(1);
						context2 = contextList.get(0);
						
					}
					else if(contextList.size()< 2)
					{
						context1 = contextList.get(0);
						
					}
				}
				question.setContext1(context1);
				question.setContext2(context2);
				
				String questionText = (String) Qfmap.get("text");
				question.setQuestionText(questionText);
				
				String qBody = (String) Qfmap.get("Qbody");
				question.setQuestionBody(qBody);
				
				List<String> bodlist = (List<String>) Qfmap.get("bodyAsList");
				if(CollectionUtils.isNotEmpty(bodlist))
				{
					String bodyAsList= writeListToString(bodlist);
					question.setQuestionBodyAsList(bodyAsList);
				}
				List<String> limpText = (List<String>) Qfmap.get("listOfImportantText");
				question.setListOfImportantQuestions(limpText);
				
				List<String> limpQ = (List<String>) Qfmap.get("listOfImportantQuestions");
				question.setListOfImportantQuestions(limpQ);
				
				questionL.add(question);
			}
			ds.setListOfQuestions(questionL);
		}
		
		Set<String> impoWords = new HashSet<String>();
		if(CollectionUtils.isNotEmpty(important_bold_words))
		{
			
			for(Annotation impWord:important_bold_words)
			{
				String iiword = Utils.cleanStringFor(document, impWord);
				impoWords.add(iiword);
			}
			
		}
		
		if(CollectionUtils.isNotEmpty(otherImportantWords))
		{
			for(Annotation impWord:otherImportantWords)
			{
				String iiword = Utils.cleanStringFor(document, impWord);
				impoWords.add(iiword);
			}
			
		}
		ds.setImportantWords(impoWords);
		
		if(CollectionUtils.isNotEmpty(subHeader2))
		{
			List<SubHeaders> listOfSubHeaders2 = new ArrayList<SubHeaders>();
			for(Annotation notbld:subHeader2)
			{
				SubHeaders subH = new SubHeaders();
				FeatureMap setMap = notbld.getFeatures();
				
				String subHeader = (String) setMap.get("text");
				subH.setSubHeader(subHeader);
				
				String body = (String) setMap.get("body");
				subH.setBody(body);
				listOfSubHeaders2.add(subH);
			}
			ds.setListOfSubHeaders2(listOfSubHeaders2);
		}
		
		if(CollectionUtils.isNotEmpty(boldSet))
		{
			List<SubHeaders> listOfSubHeaders = new ArrayList<SubHeaders>();
			for(Annotation bld:boldSet)
			{
				SubHeaders subH = new SubHeaders();
				FeatureMap bmap = bld.getFeatures();
				String subHeader = (String) bmap.get("text");
				subH.setSubHeader(subHeader);
				
				String body = (String) bmap.get("body");
				subH.setBody(body);
				
				List<String> bodyAsLists = (List<String>) bmap.get("bodyAsList");
				if(CollectionUtils.isNotEmpty(bodyAsLists))
				{
					String bodyAsLst= writeListToString(bodyAsLists);
					subH.setBodyAsList(bodyAsLst);
				}
				
				String type = (String) bmap.get("type");
				subH.setType(type);
				
				List<String> limpText = (List<String>) bmap.get("listOfImportantText");
				subH.setListOfImportantQuestions(limpText);
				
				List<String> limpQ = (List<String>) bmap.get("listOfImportantQuestions");
				subH.setListOfImportantQuestions(limpQ);
				
				listOfSubHeaders.add(subH);
				
			
				
			}
			ds.setListOfSubHeaders(listOfSubHeaders);
		}
		
		URL sourceUrl = document.getSourceUrl();
		String urlstr = sourceUrl.toString();
		String urlstrs = document.getSourceUrl().toExternalForm();
		
		//System.out.println(urlstr);
		//System.out.println(urlstrs);
		urlstr = StringUtils.substringAfter(urlstr, "file:/");
		//System.out.println(urlstr);
		
		MSWordImageExtractor extractor = new MSWordImageExtractor();
		extractor.extractImage(urlstr);
		
		String tfile = MSWordImageExtractor.tempfile;
		File ifile = new File(tfile);
		File[] lfiles = ifile.listFiles();
		if(ArrayUtils.isNotEmpty(lfiles))
		{
			for(File ff: lfiles)
			{
				DBObject metaData = new BasicDBObject();
				metaData.put("docName", filename);
				metaData.put("docUrl", urlstr);
				metaData.put("image_doc_id", "FullBody_"+count);
				
				String title1 = Utils.cleanStringFor(document, dTitle1);
				metaData.put("associatedTitle", title1);
				
				FileInputStream inpStream = null;
				try 
				{
					inpStream = FileUtils.openInputStream(ff);
					
					loader.getGridOperations().store(inpStream, ff.getName(), "image", metaData);
					
				} catch (IOException e)
				{
					e.printStackTrace();
				} 
				finally
				{
					try {
						inpStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			//delete temp file
			try 
			{
				FileUtils.forceDelete(new File(tfile));
			} 
			catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		
		
		loader.getMongoOps().save(ds);
		Factory.deleteResource(document);
		
		
	}
	
	private void processCareActItalHeader(AnnotationSet defaultSet)
	{
		Integer count = 0;
		
		AnnotationSet careActHeaderSet = defaultSet.get("CareActHeader");
		AnnotationSet careActHeaderContentSet = defaultSet.get("CareActHeaderContent");
		AnnotationSet introSet = defaultSet.get("IntroductionToTheCareAct");
		AnnotationSet dateOfEnacmentSet = defaultSet.get("DateOfEnactment");
		
		String careActHeader = (String) careActHeaderSet.iterator().next().getFeatures().get("title");
		String fullCareActContent = (String) careActHeaderContentSet.iterator().next().getFeatures().get("body");
		String dateOfEnactment= (String) dateOfEnacmentSet.iterator().next().getFeatures().get("DateOfEnactment");
		dateOfEnactment=StringUtils.remove(dateOfEnactment, "[");
		dateOfEnactment=StringUtils.remove(dateOfEnactment, "]");
		dateOfEnactment=StringUtils.normalizeSpace(dateOfEnactment);
		String introduction = Utils.cleanStringFor(document, introSet);
		
		AnnotationSet ciBody = defaultSet.get("CareActItalicizedHeader");
		for(Annotation ciBodys:ciBody)
		{
			CareActItalicizedBody ciBdy = new CareActItalicizedBody();
			
			ciBdy.setIntroduction(introduction);
			ciBdy.setCareActHeader(careActHeader);
			ciBdy.setDateOfEnactment(dateOfEnactment);
			ciBdy.setDateOfUpload(new Date());
			ciBdy.setFullCareActContent(fullCareActContent);
			
			FeatureMap map = ciBodys.getFeatures();
			
			String italBody = (String) map.get("body");
			ciBdy.setItalBody(italBody);
			
			String isPartOf = (String) map.get("IS_PART_OF");
			String italTitle = (String) map.get("title");
			ciBdy.setItalTitle(italTitle);
			
			if(StringUtils.isNotBlank(isPartOf))
			{
				String partNumber = StringUtils.substringAfter(isPartOf, "PART ");
				partNumber = StringUtils.substring(partNumber, 0, 1);
				String partTitle = StringUtils.substringAfter(isPartOf, "PART "+partNumber);
				
				ciBdy.setPartNumber(partNumber);
				ciBdy.setPart("PART "+partNumber);
				
				ciBdy.setPartTitle(partTitle);
				
			}
			List<String> listOfSubHeaders = (List<String>) map.get("listOfSubHeaders");
			ciBdy.setListOfSubHeaders(listOfSubHeaders);
			
			Integer numberOfSubHeaders = (Integer) map.get("numberOfSubHeaders");
			ciBdy.setNumberOfSubHeaders(numberOfSubHeaders.toString());
			
			ciBdy.setDocNumber(count);
			loader.getMongoOps().save(ciBdy);
			count++;
		}
		Factory.deleteResource(document);
	}
	
	private void processCareActContent(AnnotationSet defaultSet)
	{
		Integer count = 0;
			
		AnnotationSet careActHeaderSet = defaultSet.get("CareActHeader");
		AnnotationSet careActHeaderContentSet = defaultSet.get("CareActHeaderContent");
		AnnotationSet introSet = defaultSet.get("IntroductionToTheCareAct");
		AnnotationSet dateOfEnacmentSet = defaultSet.get("DateOfEnactment");
		AnnotationSet subHeadingSet = defaultSet.get("CareActSubHeading");
		
		String careActHeader = (String) careActHeaderSet.iterator().next().getFeatures().get("title");
		String fullCareActContent = (String) careActHeaderContentSet.iterator().next().getFeatures().get("body");
		String dateOfEnactment= (String) dateOfEnacmentSet.iterator().next().getFeatures().get("DateOfEnactment");
		dateOfEnactment=StringUtils.remove(dateOfEnactment, "[");
		dateOfEnactment=StringUtils.remove(dateOfEnactment, "]");
		dateOfEnactment=StringUtils.normalizeSpace(dateOfEnactment);
		String introduction = Utils.cleanStringFor(document, introSet);
		
		for(Annotation subset:subHeadingSet)
		{
			CareActContent cc = new CareActContent();
			
			
			FeatureMap subsetH = subset.getFeatures();
			String subHeadingTitle = (String) subsetH.get("title");
			String level = (String) subsetH.get("level");
			String containedIn = (String) subsetH.get("containedIn");
			
			String body = (String) subsetH.get("body");
			List<String> bodyList = (List<String>) subsetH.get("bodyAsList");
			String bodyAsList = writeListToString(bodyList);
			
			String iHeader = (String) subsetH.get("italicizedHeader");
			String part_of = (String) subsetH.get("PART_OF");
			String is_part_of = (String) subsetH.get("IS_PART_OF");
			
			if(StringUtils.isNotBlank(is_part_of))
			{
				String partNumber = StringUtils.substringAfter(is_part_of, "PART ");
				partNumber = StringUtils.substring(partNumber, 0, 1);
				String partTitle = StringUtils.substringAfter(is_part_of, "PART "+partNumber);
				
				cc.setPartNumber(partNumber);
				cc.setPartTitle(partTitle);
				
			}
			
			String containsChapter = (String)subsetH.get("containsChapter");
			
			if(StringUtils.isNotBlank(containsChapter))
			{
				String fullChapterBody = (String)subsetH.get("fullChapterBody");
				
				String fullChapterTitle = (String)subsetH.get("fullChapterTitle");
				
				String chapter = StringUtils.substringAfter(fullChapterTitle, "CHAPTER ");
				chapter = StringUtils.substring(chapter, 0, 1);
				
				String chapterTitle = StringUtils.substringAfter(fullChapterTitle, "CHAPTER "+chapter);
				
				cc.setFullChapterBody(fullChapterBody);
				cc.setFullChapterTitle(fullChapterTitle);
				cc.setChapterTitle(chapterTitle);
				cc.setChapterNumber(chapter);
				
				
			}
			
			cc.setDateOfUpload(new Date());
			
			cc.setIs_part_of(is_part_of);
			cc.setPart_of(part_of);
			cc.setiHeader(iHeader);
			cc.setBody(body);
			cc.setBodyAsList(bodyAsList);
			cc.setContainedIn(containedIn);
			cc.setLevel(level);
			cc.setSubHeadingTitle(subHeadingTitle);
			
			cc.setCareActHeader(careActHeader);
			cc.setFullCareActContent(fullCareActContent);
			cc.setDateOfEnactment(dateOfEnactment);
			cc.setIntroduction(introduction);
			
			cc.setDocNumber(count);
			
			loader.getMongoOps().save(cc);
			Factory.deleteResource(document);
			count++;
		}
	}
	
	private void processSchedules(AnnotationSet defaultSet)
	{
		Integer count = 0;
		
		AnnotationSet scheduleHeader = defaultSet.get("ScheduleHeader");
		FeatureMap fmap = scheduleHeader.iterator().next().getFeatures();
		
		/*Schedule Content Body */
		String scheduleOutlineBody1 = (String) fmap.get("body");
		String scheduleOutlineHeader = (String)fmap.get("title");
		
		List<String> scheduleList = (List<String>) fmap.get("bodyAsList");
		String scheduleOutlineBody = writeListToString(scheduleList);
		
		/* Italicized Schedule Body*/
		AnnotationSet schedItalicized = defaultSet.get("ScheduleHeaderItalicized");
		
		AnnotationSet schedulePart = defaultSet.get("SchedulePart");
		for(Annotation annSchedulePart:schedulePart)
		{
			CareActSchedule careActSchedulePart = new CareActSchedule();
			careActSchedulePart.setScheduleOutlineHeader(scheduleOutlineHeader);
			careActSchedulePart.setScheduleOutlineBody(scheduleOutlineBody);
			careActSchedulePart.setScheduleOutlineBody1(scheduleOutlineBody1);
			careActSchedulePart.setType("schedulePart");
			
			FeatureMap acaMap = annSchedulePart.getFeatures();
			
			List<String> italHeaders =  (List<String>) acaMap.get("importantItalicizedHeaders");
			careActSchedulePart.setListOfSubHeaders(italHeaders);
			
			
			String sptbody = (String) acaMap.get("body");
			careActSchedulePart.setFullScheduleSectionBody(sptbody);
			
			String  schedPart = (String) acaMap.get("belongsTo_Schedule_Part_1");
			if(StringUtils.isNotBlank(schedPart))
			{
				String scheduleNumber = extractNumber(schedPart);
				String scheduleNumberWords = "SCHEDULE "+scheduleNumber;
				if(StringUtils.isNotBlank(scheduleNumber))
				{
					
					String fullScheduleTitle = StringUtils.substringAfter(schedPart, scheduleNumberWords);
					careActSchedulePart.setFullScheduleTitle(fullScheduleTitle);
					
					careActSchedulePart.setScheduleNumber(scheduleNumber);
					careActSchedulePart.setScheduleNumberWords(scheduleNumberWords);
				}
				String docPartName = Utils.cleanStringFor(document, annSchedulePart);
				
				String pNumber = extractNumber(docPartName);
				String pNumberWrd = "PART "+pNumber;
				if(StringUtils.isNotBlank(pNumber))
				{
					String fullPartTitle1 = StringUtils.substringAfter(docPartName, pNumberWrd);
					careActSchedulePart.setPartNumber(pNumberWrd);
					careActSchedulePart.setPartTitle(fullPartTitle1);
					
				}
			}
			else
			{
			String sptitle = (String) acaMap.get("title");
			String scheduleNumber = extractNumber(sptitle);
			String scheduleNumberWords = "SCHEDULE "+scheduleNumber;
				if(StringUtils.isNotBlank(scheduleNumber))
				{
					
					String fullScheduleTitle = StringUtils.substringAfter(sptitle, scheduleNumberWords);
					careActSchedulePart.setFullScheduleTitle(fullScheduleTitle);
					
					careActSchedulePart.setScheduleNumber(scheduleNumber);
					careActSchedulePart.setScheduleNumberWords(scheduleNumberWords);
				}
			}
			careActSchedulePart.setDocNumber(count);
			loader.getMongoOps().save(careActSchedulePart);
			count++;
		}
		
		for(Annotation annSchedItal:schedItalicized)
		{
			
			CareActSchedule careActSchedule = new CareActSchedule();
			
			careActSchedule.setScheduleOutlineHeader(scheduleOutlineHeader);
			careActSchedule.setScheduleOutlineBody(scheduleOutlineBody);
			careActSchedule.setScheduleOutlineBody1(scheduleOutlineBody1);
			
			
			careActSchedule.setType("scheduleItalics");
			
			FeatureMap italMap = annSchedItal.getFeatures();
			
			String iTitle= (String) italMap.get("title");
			String iBody1 = (String) italMap.get("body");
			List<String> scheduleItalicsBodyList = (List<String>)italMap.get("bodyAsList");
			String iBody= writeListToString(scheduleItalicsBodyList);
			
			List<String> fullSchedulePartBodyL = (List<String>) italMap.get("fullScheduleBody_List");
			String fullScheduleSectionBody= writeListToString(fullSchedulePartBodyL);
			
			String schedulePartTitle = (String) italMap.get("belongsTo_Schedule_Part_1");
			
			String scheduleNumber = extractNumber(schedulePartTitle );
			String scheduleNumberWords = "SCHEDULE "+scheduleNumber;
			
			if(StringUtils.isNotBlank(scheduleNumber))
			{
				
				String fullScheduleTitle = StringUtils.substringAfter(schedulePartTitle, scheduleNumberWords);
				careActSchedule.setFullScheduleTitle(fullScheduleTitle);
			}
			
			
			careActSchedule.setScheduleNumber(scheduleNumber);
			careActSchedule.setScheduleNumberWords(scheduleNumberWords);
			careActSchedule.setFullScheduleSectionBody(fullScheduleSectionBody);
			careActSchedule.setiTitle(iTitle);
			careActSchedule.setiBody(iBody);
			careActSchedule.setiBody1(iBody1);
			
			careActSchedule.setDocNumber(count);
			loader.getMongoOps().save(careActSchedule);
			count++;
						
		}
		
		Factory.deleteResource(document);
			
	}
	
	private String extractNumber(String str)
	{
		String num = "";
		if(StringUtils.isNotBlank(str))
		{
			num = str.replaceAll("\\D+","");
			num = StringUtils.normalizeSpace(num);
		}
		return num;
	}
	
	private String writeListToString(List<String> lst)
	{
		String fstring = "";
		StringBuilder sbu =new StringBuilder();
		
		for(String s:lst)
		{
			s = StringUtils.normalizeSpace(s);
			sbu.append("\n"+s);
		}
		fstring = sbu.toString();
		return fstring;
	}
	
	
	public static void main(String[] args) 
	{
		//DocProcessor d = new DocProcessor("care act","http://www.legislation.gov.uk/ukpga/2014/23/enacted");
		//DocProcessor d = new DocProcessor("Other","file:/C:/Users/obinnao2/Desktop/PCG_DOCUMENT_ANALYSIS/Analysis/New Sets/Schools_H_S_Manual.doc");
		
		//real
		//DocProcessor d = new DocProcessor("Other","file:/C:/Users/oonyimadu/Documents/PCG_DOCS/BCC files sent 15_MAR-2016/documents/Policy/Transition protocol_v1_20150730.docx");
		DocProcessor d = new DocProcessor("Other","file:/C:/Users/oonyimadu/Documents/Bucks_ss_files/BCC_SafeGuarding_1_image.docx");
		d.doAnalysis();
				
	}
	
}
