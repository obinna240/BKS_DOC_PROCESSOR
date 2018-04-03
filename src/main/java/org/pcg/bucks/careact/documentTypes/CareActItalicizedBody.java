package org.pcg.bucks.careact.documentTypes;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="CareActItalicizedBody")
public class CareActItalicizedBody
{
	private String id;
	private String introduction;
	private String dateOfEnactment;
	private Date dateOfUpload;
	private String careActHeader;
	//care act content
	private String fullCareActContent;
	private String italTitle;
	private String italBody;
	private String part;
	private String partTitle;
	private String partNumber;
	
	private List<String> listOfSubHeaders;
	private String numberOfSubHeaders;
	
	private Integer docNumber;
	
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public String getPartTitle() {
		return partTitle;
	}
	public void setPartTitle(String partTitle) {
		this.partTitle = partTitle;
	}
	
	
	public String getItalBody() {
		return italBody;
	}
	public void setItalBody(String italBody) {
		this.italBody = italBody;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getDateOfEnactment() {
		return dateOfEnactment;
	}
	public void setDateOfEnactment(String dateOfEnactment) {
		this.dateOfEnactment = dateOfEnactment;
	}
	public Date getDateOfUpload() {
		return dateOfUpload;
	}
	public void setDateOfUpload(Date dateOfUpload) {
		this.dateOfUpload = dateOfUpload;
	}
	public String getCareActHeader() {
		return careActHeader;
	}
	public void setCareActHeader(String careActHeader) {
		this.careActHeader = careActHeader;
	}
	public String getFullCareActContent() {
		return fullCareActContent;
	}
	public void setFullCareActContent(String fullCareActContent) {
		this.fullCareActContent = fullCareActContent;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public List<String> getListOfSubHeaders() {
		return listOfSubHeaders;
	}
	public void setListOfSubHeaders(List<String> listOfSubHeaders) {
		this.listOfSubHeaders = listOfSubHeaders;
	}
	public String getNumberOfSubHeaders() {
		return numberOfSubHeaders;
	}
	public void setNumberOfSubHeaders(String numberOfSubHeaders) {
		this.numberOfSubHeaders = numberOfSubHeaders;
	}
	public String getItalTitle() {
		return italTitle;
	}
	public void setItalTitle(String italTitle) {
		this.italTitle = italTitle;
	}
	public Integer getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(Integer docNumber) {
		this.docNumber = docNumber;
	}

}
