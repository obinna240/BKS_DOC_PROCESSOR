package org.pcg.bucks.careact.documentTypes;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.pcg.bucks.careact.documentUtils.Questions;
import org.pcg.bucks.careact.documentUtils.SubHeaders;
import org.pcg.bucks.careact.documentUtils.TableContent;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="DocumentNotes")
public class DocumentNotes 
{
	private Set<String> listOfOrganizations;
	private Set<String> listOfLocations;
	private Set<String> listOfPersons;
	private Set<String> listOfUrls;
	private Set<String> listOfEmails;
	private List<String> listOfImages;
	private String documentTitle;
	private Set<String> otherTitle;
	private String documentBody;
	private Date dateOfUpLoad;
	private String documentLocation;
	private List<Questions> listOfQuestions;
	private List<SubHeaders> listOfSubHeaders;
	private List<SubHeaders> listOfSubHeaders2;
	private List<TableContent> tableContent;
	private Set<String> importantWords;
	private Set<String> author2;
	private String version;
	private String creator;
	private String author;
	private String fileName;
	private String type;   //guidance note, policy, strategy etc
	private String document_Owner;
	private String dateOfPublication;
	private Date dateOfPublicationAsDate;
	private Integer docNumber;
		
	public String getDateOfPublication() {
		return dateOfPublication;
	}
	public void setDateOfPublication(String dateOfPublication) {
		this.dateOfPublication = dateOfPublication;
	}
	public Set<String> getListOfOrganizations() 
	{
		return listOfOrganizations;
	}
	public void setListOfOrganizations(Set<String> listOfOrganizations) 
	{
		this.listOfOrganizations = listOfOrganizations;
	}
	public Set<String> getListOfLocations() {
		return listOfLocations;
	}
	public void setListOfLocations(Set<String> listOfLocations) {
		this.listOfLocations = listOfLocations;
	}
	public Set<String> getListOfPersons() {
		return listOfPersons;
	}
	public void setListOfPersons(Set<String> listOfPersons) {
		this.listOfPersons = listOfPersons;
	}
	public Set<String> getListOfUrls() {
		return listOfUrls;
	}
	public void setListOfUrls(Set<String> listOfUrls) {
		this.listOfUrls = listOfUrls;
	}
	public String getDocumentTitle() {
		return documentTitle;
	}
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}
	public String getDocumentBody() {
		return documentBody;
	}
	public void setDocumentBody(String documentBody) {
		this.documentBody = documentBody;
	}
	public Date getDateOfUpLoad() {
		return dateOfUpLoad;
	}
	public void setDateOfUpLoad(Date dateOfUpLoad) {
		this.dateOfUpLoad = dateOfUpLoad;
	}
	public String getDocumentLocation() {
		return documentLocation;
	}
	public void setDocumentLocation(String documentLocation) {
		this.documentLocation = documentLocation;
	}
	public List<Questions> getListOfQuestions() {
		return listOfQuestions;
	}
	public void setListOfQuestions(List<Questions> listOfQuestions) {
		this.listOfQuestions = listOfQuestions;
	}
	public List<SubHeaders> getListOfSubHeaders() {
		return listOfSubHeaders;
	}
	public void setListOfSubHeaders(List<SubHeaders> listOfSubHeaders) {
		this.listOfSubHeaders = listOfSubHeaders;
	}
	public List<TableContent> getTableContent() {
		return tableContent;
	}
	public void setTableContent(List<TableContent> tableContent) {
		this.tableContent = tableContent;
	}
	public List<String> getListOfImages() {
		return listOfImages;
	}
	public void setListOfImages(List<String> listOfImages) {
		this.listOfImages = listOfImages;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Set<String> getListOfEmails() {
		return listOfEmails;
	}
	public void setListOfEmails(Set<String> listOfEmails) {
		this.listOfEmails = listOfEmails;
	}
	public Set<String> getOtherTitle() {
		return otherTitle;
	}
	public void setOtherTitle(Set<String> otherTitle) {
		this.otherTitle = otherTitle;
	}
	public Set<String> getImportantWords() {
		return importantWords;
	}
	public void setImportantWords(Set<String> importantWords) {
		this.importantWords = importantWords;
	}
	public String getDocument_Owner() {
		return document_Owner;
	}
	public void setDocument_Owner(String document_Owner) {
		this.document_Owner = document_Owner;
	}
	public List<SubHeaders> getListOfSubHeaders2() {
		return listOfSubHeaders2;
	}
	public void setListOfSubHeaders2(List<SubHeaders> listOfSubHeaders2) {
		this.listOfSubHeaders2 = listOfSubHeaders2;
	}
	public Integer getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(Integer docNumber) {
		this.docNumber = docNumber;
	}
	public Set<String> getAuthor2() {
		return author2;
	}
	public void setAuthor2(Set<String> author2) {
		this.author2 = author2;
	}
	public Date getDateOfPublicationAsDate() {
		return dateOfPublicationAsDate;
	}
	public void setDateOfPublicationAsDate(Date dateOfPublicationAsDate) {
		this.dateOfPublicationAsDate = dateOfPublicationAsDate;
	}
	
	
	
	
}
