package org.pcg.bucks.careact.documentTypes;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="CareActSchedule")
public class CareActSchedule 
{
	
	private String id;
	private String scheduleOutlineHeader;
	private String scheduleOutlineBody;
	private String scheduleOutlineBody1;
	
	
	private String scheduleNumber;     		//e.g 1
	private String scheduleNumberWords; 	//e.g SCHEDULE 1
	private String fullScheduleTitle;  		//e.g Health Education England
	private String fullScheduleSectionBody; 
	
	private String iTitle;
	
	private String iBody1;
	private String iBody;
	
	private String type; //schedulePart, scheduleItalicized
	private List<String> listOfSubHeaders;
	
	private String partNumber; //optional applies to schedulePart
	private String partTitle; //optional applies to schedulePart
	
	private Integer docNumber;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScheduleOutlineHeader() {
		return scheduleOutlineHeader;
	}
	public void setScheduleOutlineHeader(String scheduleOutlineHeader) {
		this.scheduleOutlineHeader = scheduleOutlineHeader;
	}
	public String getScheduleOutlineBody() {
		return scheduleOutlineBody;
	}
	public void setScheduleOutlineBody(String scheduleOutlineBody) {
		this.scheduleOutlineBody = scheduleOutlineBody;
	}
	public String getScheduleOutlineBody1() {
		return scheduleOutlineBody1;
	}
	public void setScheduleOutlineBody1(String scheduleOutlineBody1) {
		this.scheduleOutlineBody1 = scheduleOutlineBody1;
	}
	
	public String getScheduleNumber() {
		return scheduleNumber;
	}
	public void setScheduleNumber(String scheduleNumber) {
		this.scheduleNumber = scheduleNumber;
	}
	public String getScheduleNumberWords() {
		return scheduleNumberWords;
	}
	public void setScheduleNumberWords(String scheduleNumberWords) {
		this.scheduleNumberWords = scheduleNumberWords;
	}
	public String getFullScheduleTitle() {
		return fullScheduleTitle;
	}
	public void setFullScheduleTitle(String fullScheduleTitle) {
		this.fullScheduleTitle = fullScheduleTitle;
	}
	public String getFullScheduleSectionBody() {
		return fullScheduleSectionBody;
	}
	public void setFullScheduleSectionBody(String fullScheduleSectionBody) {
		this.fullScheduleSectionBody = fullScheduleSectionBody;
	}
	
	
	public String getiTitle() {
		return iTitle;
	}
	public void setiTitle(String iTitle) {
		this.iTitle = iTitle;
	}
	public String getiBody1() {
		return iBody1;
	}
	public void setiBody1(String iBody1) {
		this.iBody1 = iBody1;
	}
	public String getiBody() {
		return iBody;
	}
	public void setiBody(String iBody) {
		this.iBody = iBody;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getListOfSubHeaders() {
		return listOfSubHeaders;
	}
	public void setListOfSubHeaders(List<String> listOfSubHeaders) {
		this.listOfSubHeaders = listOfSubHeaders;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getPartTitle() {
		return partTitle;
	}
	public void setPartTitle(String partTitle) {
		this.partTitle = partTitle;
	}
	public Integer getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(Integer docNumber) {
		this.docNumber = docNumber;
	}
	
}
