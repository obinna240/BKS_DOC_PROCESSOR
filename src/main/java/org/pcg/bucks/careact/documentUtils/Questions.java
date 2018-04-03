package org.pcg.bucks.careact.documentUtils;

import java.util.List;

public class Questions
{
	private String QHeaderPrecision;
	private String QHeaderPrecision2;
	private String QuestionText;
	private String QuestionBody;
	private String QuestionBodyAsList;
	
	private String context1;
	private String context2;
	private List<String> listOfImportantText;
	private List<String> listOfImportantQuestions;
	
	
	public String getQuestionBody() {
		return QuestionBody;
	}
	public void setQuestionBody(String questionBody) {
		QuestionBody = questionBody;
	}
	
	public String getContext1() {
		return context1;
	}
	public void setContext1(String context1) {
		this.context1 = context1;
	}
	public String getContext2() {
		return context2;
	}
	public void setContext2(String context2) {
		this.context2 = context2;
	}
	public List<String> getListOfImportantText() {
		return listOfImportantText;
	}
	public void setListOfImportantText(List<String> listOfImportantText) {
		this.listOfImportantText = listOfImportantText;
	}
	public List<String> getListOfImportantQuestions() {
		return listOfImportantQuestions;
	}
	public void setListOfImportantQuestions(List<String> listOfImportantQuestions) {
		this.listOfImportantQuestions = listOfImportantQuestions;
	}

	
	public String getQHeaderPrecision() {
		return QHeaderPrecision;
	}
	public void setQHeaderPrecision(String qHeaderPrecision) {
		QHeaderPrecision = qHeaderPrecision;
	}
	public String getQuestionText() {
		return QuestionText;
	}
	public void setQuestionText(String questionText) {
		QuestionText = questionText;
	}
	public String getQuestionBodyAsList() {
		return QuestionBodyAsList;
	}
	public void setQuestionBodyAsList(String questionBodyAsList) {
		QuestionBodyAsList = questionBodyAsList;
	}
	public String getQHeaderPrecision2() {
		return QHeaderPrecision2;
	}
	public void setQHeaderPrecision2(String qHeaderPrecision2) {
		QHeaderPrecision2 = qHeaderPrecision2;
	}
	
}
