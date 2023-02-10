package test.com.novartis.swy.lc;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DictionaryDqlLength {

	Properties properties=null;

	public static void main (String[] args)
	{
		System.out.println("Processing Start ============================");
		DictionaryDqlLength object=new DictionaryDqlLength();
		object.processXML();
		System.out.println("Processing End ============================");
	}

	public void processXML()
	{

		try 
		{
			String sourceFolderPath="C:\\KShubham\\K8S-GitLab\\Application\\Dev\\TCR-Lines\\config_exports\\SubWayTCR_Export-Config\\d2_dictionary";
			File folderObj=new File(sourceFolderPath);
			
			File stateFiles[]=folderObj.listFiles();
			
			//readPropertyFile();
			//int file_to_be_process=Integer.parseInt(properties.getProperty("FILES_TO_BE_PROCESS"));
			int file_to_be_process=stateFiles.length;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			Document document = null;

			for (int i=0;i<file_to_be_process;i++)
			{
				builder = null;
				builder = factory.newDocumentBuilder();
				document =null;
				
				String file_name=stateFiles[i].getName();
				
				System.out.print("\n");
				System.out.print("File Name=["+file_name+"] ");
				
				document = builder.parse(new File( sourceFolderPath+"\\"+file_name ));

			

				document.getDocumentElement().normalize();

				Element root = document.getDocumentElement();
				//System.out.println(root.getNodeName());

					NamedNodeMap  attrMap=root.getAttributes();
					int attrSize=attrMap.getLength();
					ArrayList<String> attrList=new ArrayList<String>();
					
						for (int index=0;index<attrSize;index++)
						{
							Node attr=attrMap.item(index);
							//System.out.println("Attr Name ="+ attr.getNodeName());
							//attrList.add(attr.getNodeName().toLowerCase());
							
							
							if(attr.getNodeName().toLowerCase().equalsIgnoreCase("dql") )
							{
								
								//String is_dql_str= attr.get;
								
								String dqlStr=attr.getNodeValue();
								
								//System.out.println(file_name);
								System.out.print("\t"+ dqlStr.length());
								break;
								
								
							}
							
						}

			}
			
		}
		catch (Exception ex) 
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

	}

	/*public  String getText(Node node) throws Exception
	{
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
		xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		xform.transform(new DOMSource(node), new StreamResult(buf));
		return(buf.toString());

	}*/

	/*public void writeStrInFile(String state,String fileContent, String folderPath) throws IOException
	{
		File folder=new File(folderPath);

		if(!folder.exists())
			folder.mkdirs();

		File file=new File(folderPath + "\\" +state + ".xml");
		file.createNewFile();

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(fileContent);
		writer.close();

	}*/

	/*public void readPropertyFile1() throws Exception
	{

		properties=new Properties();

		//File tFile=new File(System.getProperty("user.dir")+"/Extraction.properties");
		//prop.load(new FileInputStream(tFile));

		InputStream is=getClass().getClassLoader().getResourceAsStream("RetrieveStates.properties");
		properties.load(is);
	}*/
}
