package org.pcg.bucks.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pcg.bucks.careact.documentUtils.Content;

/**
 * Creates content object from file url
 * @author oonyimadu
 *
 */
public class ContentCreator 
{
	/**
	 * Format - Document title_vversion_date of publication.doc
	 * @param filePath
	 * @return Content
	 */
	public static Content createContent1(String filePath)
	{
		Content content = null;
		
		if(StringUtils.isNotBlank(filePath))
		{
			content = new Content();
			
			String documentName = StringUtils.substringBefore(filePath, "-");
			documentName = StringUtils.normalizeSpace(documentName);
			
			
			
			if(StringUtils.isNotBlank(documentName))
			{
				content.setDocumentName(documentName);
			}
			else
			{
				content.setDocumentName(StringUtils.substringBeforeLast(filePath, "."));
			}
			
			String version = StringUtils.substringAfter(filePath, "_");
			version = StringUtils.substringBefore(version,"_");
			if(version.contains("v"))
			{
				version = StringUtils.substringAfter(version, "v");
				version = StringUtils.normalizeSpace(version);
				content.setVersion(version);
			}
			
			String dateOfPub = StringUtils.substringAfterLast(filePath, "_");
			dateOfPub = StringUtils.substringBefore(dateOfPub, ".");
			
			
			String yyyy = StringUtils.normalizeSpace(StringUtils.substring(dateOfPub, 0,4));
			String mm = StringUtils.normalizeSpace(StringUtils.substring(dateOfPub, 4,6));
			String dd = StringUtils.normalizeSpace(StringUtils.substring(dateOfPub, 6,8));
			
			String pubDate = yyyy+"-"+mm+"-"+dd;
			if(StringUtils.isNotBlank(pubDate))
			{
				content.setDateOfPublication(pubDate);
			}
		}
		return content;
	}
	
	/**
	 * Format - Document title$$author1_author2_author3$$_vversion_date of publication.doc
	 * @param filePath
	 * @return Content
	 */
	public static Content createContent(String filePath)
	{
		Content content = null;
		
		if(StringUtils.isNotBlank(filePath))
		{
			content = new Content();
			
			String documentName = StringUtils.substringBefore(filePath, "$");
			documentName = StringUtils.normalizeSpace(documentName);
			
			
			
			if(StringUtils.isNotBlank(documentName))
			{
				content.setDocumentName(documentName);
			}
			else
			{
				content.setDocumentName(StringUtils.substringBeforeLast(filePath, "."));
			}
			
			String authorsList= StringUtils.substringAfter(filePath, "$$");
			authorsList= StringUtils.substringBefore(authorsList, "$$");
			
			String[] author =  authorsList.split("_");
			List<String> alist = Arrays.asList(author);
			content.setAuthor(alist);
			
			String version = StringUtils.substringAfterLast(filePath, "$");
			version = StringUtils.substringAfter(version, "_");
			version = StringUtils.substringBefore(version,"_");
			if(version.contains("v"))
			{
				version = StringUtils.substringAfter(version, "v");
				version = StringUtils.normalizeSpace(version);
				content.setVersion(version);
			}
			
			String dateOfPub = StringUtils.substringAfterLast(filePath, "_");
			dateOfPub = StringUtils.substringBefore(dateOfPub, ".");
			
			
			String yyyy = StringUtils.normalizeSpace(StringUtils.substring(dateOfPub, 0,4));
			String mm = StringUtils.normalizeSpace(StringUtils.substring(dateOfPub, 4,6));
			String dd = StringUtils.normalizeSpace(StringUtils.substring(dateOfPub, 6,8));
			
			String pubDate = yyyy+"-"+mm+"-"+dd;
			if(StringUtils.isNotBlank(pubDate))
			{
				content.setDateOfPublication(pubDate);
			}
		}
		return content;
	}
	
	
	
}
