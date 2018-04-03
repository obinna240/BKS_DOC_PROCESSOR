package org.pcg.bucks.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CleanerUtils 
{
	static final Pattern PUNCT = Pattern.compile("[(){},.;!?<>%]");
	
	public static String processWord(String string)
	{
		return PUNCT.matcher(string).replaceAll("");
	}
	
	public static String removeLeadingNumber(String string)
	{
		string = StringUtils.normalizeSpace(string);
		string = string.replaceFirst("^\\d+", "");
		string = StringUtils.normalizeSpace(string);
		return string;
	}
	
	public static  String replaceFirstDot(String string)
	{
		string=string.replaceFirst("^\\.", "");
		string = StringUtils.normalizeSpace(string);
		return string;
	}
	
	public static String removePunctuation(String string)
	{
		string = StringUtils.normalizeSpace(string);
		string = CleanerUtils.processWord(string);
		return string;
	}
	
	public static int getTextSize(String string)
	{
		String[] array = string.split("\\s");
		int arrSize = array.length;
		return arrSize;
	}
	
	public static String cleanApostrophe(String string)
	{
		string = StringUtils.replace(string,"’","'");
		return string;
	}
	
	
}
