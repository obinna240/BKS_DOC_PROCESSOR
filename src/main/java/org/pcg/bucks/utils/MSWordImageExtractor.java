package org.pcg.bucks.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
//import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.pcg.bucks.indexer.DocumentContentRetriever;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * 
 * @author oonyimadu
 *
 */
public class MSWordImageExtractor
{
	static Properties properties = null;
	InputStream input = null;
	private final static Logger LOGGER = Logger.getLogger(MSWordImageExtractor.class);
	public static String tempfile;
	
	public MSWordImageExtractor()
	{
		//tempFile
		try
		{
			properties = new Properties();
			input = new FileInputStream("config.properties");
			properties.load(input);
			
			tempfile = properties.getProperty("tempFile");
			
			File f = new File(tempfile);
			if(!f.exists()) //&& !f.isDirectory())
			{
				FileUtils.forceMkdir(f);
			}
		}
		catch(IOException exception)
		{
			LOGGER.info(exception.getMessage());
		}
		finally
		{
			if (input != null) 
			{
				try 
				{
					input.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
  
		//selectwORD();
		
		MSWordImageExtractor extractor = new MSWordImageExtractor();
		
		extractor.extractImage("C:/Users/oonyimadu/Documents/Bucks_ss_files/Section 2 BCC Safeguarding Adults Procedure_v1_20160301.docx");
				
		//try 
		//{
		//	FileUtils.forceDelete(new File(tempfile));
		//} 
		//catch (IOException e) {
			
		//	e.printStackTrace();
		//}
	}
		
	/**
	 * 
	 * @param documentSource
	 */
	public void extractImage(String documentSource)
	{
		String last = StringUtils.substringAfterLast(documentSource, ".");
						
		if(last.equals("docx"))
		{
			extractImages(documentSource);
		}
		else if(last.equals("doc"))
		{
			extractImagesDoc(documentSource);
		}	
	}
		
	
	
/**
 * FileChooser
 
 public static void selectwORD()
 {

	 JFileChooser chooser = new JFileChooser();
     FileNameExtensionFilter filter = new FileNameExtensionFilter("DOCX","docx");
     chooser.setFileFilter(filter);
     chooser.setMultiSelectionEnabled(false);
     int returnVal = chooser.showOpenDialog(null);
     if(returnVal == JFileChooser.APPROVE_OPTION) {
      File file=chooser.getSelectedFile();
      System.out.println("Please wait...");      
      extractImages(file.toString());
      System.out.println("Extraction complete");
           }
            
  }
  */
 
 /**
  * 
  * @param src
  */
 private void extractImages(String src)
 {
	 try
	 {
		 String fname = StringUtils.substringAfterLast(src, "/");
		 fname = StringUtils.substringBeforeLast(fname, ".");
 
		  FileInputStream fs=new FileInputStream(src);
		
		  XWPFDocument docx=new XWPFDocument(fs);
		
		  List<XWPFPictureData> piclist=docx.getAllPictures();
	
		  Iterator<XWPFPictureData> iterator=piclist.iterator();
		  
		  int i=0;
		  
		  while(iterator.hasNext())
		  {
			  
			   XWPFPictureData pic=iterator.next();
			   byte[] bytepic=pic.getData();
			   String extension = pic.suggestFileExtension();
			   			   
			   BufferedImage imag=ImageIO.read(new ByteArrayInputStream(bytepic));
			   
			   if(imag!=null)
			   {
				   if(StringUtils.isBlank(extension))
				   {
					   ImageIO.write(imag, "jpg", new File(tempfile+"/"+fname+i+".jpg"));
					  
				   }
				   else 
				   {
					   ImageIO.write(imag, extension, new File(tempfile+"/"+fname+i+"."+extension));
					   
				   }
				   i++;
			   }
		  }
	  
	 }
	 catch(Exception e)
	 {
		  e.printStackTrace();
	 }
  
 }
 
 /**
  * Extract 
  * @param src
  */
 private void extractImagesDoc(String src)
 {
	 
	 try
	 {
		 String fname = StringUtils.substringAfterLast(src, "/");
		 fname = StringUtils.substringBeforeLast(fname, ".");
  
		  FileInputStream fs=new FileInputStream(src);
		
		  HWPFDocument doc = new HWPFDocument(fs);
		
		  PicturesTable piclist = doc.getPicturesTable();
		  
		  List<Picture> pixList = piclist.getAllPictures();
		  
		 
		  Iterator<Picture> iterator=pixList.iterator();
		  
		  int i=30;
		  while(iterator.hasNext())
		  {
			  	
			   Picture pic=iterator.next();
			   
			   byte[] bytepic= pic.getContent();
			   
				String sc =  pic.suggestFileExtension();
							   
			   BufferedImage imag=ImageIO.read(new ByteArrayInputStream(bytepic));
			   if(imag!=null)
			   {
			         	if(StringUtils.isBlank(sc))
			         	{
			         		ImageIO.write(imag, "jpg", new File(tempfile+"/"+fname+i+".jpg"));
					         
			         	}
			         	else
			         	{
					          ImageIO.write(imag, sc, new File(tempfile+"/"+fname+i+"."+sc));
					          
			         	}
			         	i++;
			   }
		  }
  
	 }
	 catch(Exception e)
	 {
		 
		  e.printStackTrace();
	 }
  
 }
 

 
}
