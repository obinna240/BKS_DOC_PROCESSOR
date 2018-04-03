package org.pcg.bucks.careact.documentUtils;

import java.util.Date;
import java.util.List;

/**
 * Content - Class representing the file to be uploaded
 * @author oonyimadu
 *
 */
public class Content 
{
	private String documentCategory;
	private String documentUrl;
	private List<String> Author;
	private String version;
	private String dateOfPublication;
	
	private String documentName;
		
	
	public String getDocumentUrl() {
		return documentUrl;
	}
	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}
	public List<String> getAuthor() {
		return Author;
	}
	public void setAuthor(List<String> author) {
		Author = author;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDateOfPublication() {
		return dateOfPublication;
	}
	public void setDateOfPublication(String dateOfPublication) {
		this.dateOfPublication = dateOfPublication;
	}
	
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getDocumentCategory() {
		return documentCategory;
	}
	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}
	
	
}
