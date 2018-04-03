package org.pcg.bucks.careact.documentTypes;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="CareActContent")
public class CareActContent 
{

	//care act meta
	private String id;
	private String introduction;
	private String dateOfEnactment;
	private Date dateOfUpload;
	private String careActHeader;
	//care act content
	private String fullCareActContent; // CareActHeaderContent
	
	//subheading content
	private String subHeadingTitle; //title
	private String level;
	private String containedIn;
	private String bodyAsList;
	private String body;
	
	//italicizedHeader
	private String iHeader;
	private String part_of;
	private String is_part_of;
	private String partNumber;
	private String partTitle;
	
	//chapter content
	private String containsChapter;
	private String fullChapterBody;
	private String fullChapterTitle;
	private String chapterTitle;
	private String chapterNumber;
	
	private Integer docNumber;
	
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
	public String getSubHeadingTitle() {
		return subHeadingTitle;
	}
	public void setSubHeadingTitle(String subHeadingTitle) {
		this.subHeadingTitle = subHeadingTitle;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getContainedIn() {
		return containedIn;
	}
	public void setContainedIn(String containedIn) {
		this.containedIn = containedIn;
	}
	public String getBodyAsList() {
		return bodyAsList;
	}
	public void setBodyAsList(String bodyAsList) {
		this.bodyAsList = bodyAsList;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getiHeader() {
		return iHeader;
	}
	public void setiHeader(String iHeader) {
		this.iHeader = iHeader;
	}
	public String getPart_of() {
		return part_of;
	}
	public void setPart_of(String part_of) {
		this.part_of = part_of;
	}
	public String getIs_part_of() {
		return is_part_of;
	}
	public void setIs_part_of(String is_part_of) {
		this.is_part_of = is_part_of;
	}
	public String getContainsChapter() {
		return containsChapter;
	}
	public void setContainsChapter(String containsChapter) {
		this.containsChapter = containsChapter;
	}
	public String getFullChapterBody() {
		return fullChapterBody;
	}
	public void setFullChapterBody(String fullChapterBody) {
		this.fullChapterBody = fullChapterBody;
	}
	public String getFullChapterTitle() {
		return fullChapterTitle;
	}
	public void setFullChapterTitle(String fullChapterTitle) {
		this.fullChapterTitle = fullChapterTitle;
	}
	public String getChapterTitle() {
		return chapterTitle;
	}
	public void setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}
	public String getChapterNumber() {
		return chapterNumber;
	}
	public void setChapterNumber(String chapterNumber) {
		this.chapterNumber = chapterNumber;
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
