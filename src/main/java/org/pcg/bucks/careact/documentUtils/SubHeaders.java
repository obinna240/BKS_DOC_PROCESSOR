package org.pcg.bucks.careact.documentUtils;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author oonyimadu
 *
 */
public class SubHeaders 
{
	private String subHeader;
	private List<String>  listOfImportantWords;
	private List<String> listOfImportantQuestions;
	private String body;
	private String bodyAsList;
	//private List<String> listOfRelatedUrls;
	//private List<String> listOfEmails;
	private String type;
	
	/**
	public List<String> getListOfRelatedUrls() {
		return listOfRelatedUrls;
	}
	public void setListOfRelatedUrls(List<String> listOfRelatedUrls) {
		this.listOfRelatedUrls = listOfRelatedUrls;
	}
	public List<String> getListOfEmails() {
		return listOfEmails;
	}
	public void setListOfEmails(List<String> listOfEmails) {
		this.listOfEmails = listOfEmails;
	}
	 */
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public List<String> getListOfImportantWords() {
		return listOfImportantWords;
	}
	public void setListOfImportantWords(List<String> listOfImportantWords) {
		this.listOfImportantWords = listOfImportantWords;
	}
	
	public String getSubHeader() {
		return subHeader;
	}
	public void setSubHeader(String subHeader) {
		this.subHeader = subHeader;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public List<String> getListOfImportantQuestions() {
		return listOfImportantQuestions;
	}
	public void setListOfImportantQuestions(List<String> listOfImportantQuestions) {
		this.listOfImportantQuestions = listOfImportantQuestions;
	}
	
	public String getBodyAsList() {
		return bodyAsList;
	}
	public void setBodyAsList(String bodyAsList) {
		this.bodyAsList = bodyAsList;
	}
}
