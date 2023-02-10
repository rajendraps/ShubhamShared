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

public class RetrieveStates {

	Properties properties=null;

	public static void main (String[] args)
	{
		System.out.println("Processing Start ============================");
		RetrieveStates object=new RetrieveStates();
		object.processXML();
		System.out.println("Processing End ============================");
	}

	public void processXML()
	{

		try 
		{
			readPropertyFile();
			int file_to_be_process=Integer.parseInt(properties.getProperty("FILES_TO_BE_PROCESS"));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			Document document = null;

			for (int i=0;i<file_to_be_process;i++)
			{
				builder = null;
				builder = factory.newDocumentBuilder();
				document =null;
				
				String file_name=properties.getProperty("SOURCE_FILE_"+i);
				String targetFolder=properties.getProperty("TARGET_FOLDER_"+i);
				
				System.out.println("File Name=["+file_name+"] Folder=["+targetFolder+"]");
				
				document = builder.parse(new File( file_name ));

				/*	Schema schema = null;
					String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
					SchemaFactory schFactory = SchemaFactory.newInstance(language);
					schema = schFactory.newSchema(new File(file_name));

					Validator validator = schema.newValidator();
					validator.validate(new DOMSource(document));*/

				document.getDocumentElement().normalize();

				Element root = document.getDocumentElement();
				//System.out.println(root.getNodeName());

				//Get all employees
				NodeList nList = document.getElementsByTagName("state");
				//System.out.println("============================");

				//int incr=0;
				for (int temp = 0; temp < nList.getLength(); temp++)
				{
					Node node = nList.item(temp);
					NamedNodeMap  attrMap=node.getAttributes();
					int attrSize=attrMap.getLength();
					ArrayList<String> attrList=new ArrayList<String>();
					if (attrSize<=3)
					{
						for (int index=0;index<attrSize;index++)
						{
							Node attr=attrMap.item(index);
							//System.out.println("Attr Name ="+ attr.getNodeName());
							attrList.add(attr.getNodeName().toLowerCase());
						}

						if(attrList.contains("name") && attrList.contains("direct") && attrList.contains("entry") )
						{
							//System.out.println("");    //Just a separator
							if (node.getNodeType() == Node.ELEMENT_NODE)
							{
								//Print each employee's detail
								Element eElement = (Element) node;

								/*incr ++;
							System.out.println(incr +" State Name : "    + eElement.getAttribute("name"));	
							System.out.println("*****");
							System.out.println(getText(node));
							System.out.println("*****");

							if (incr==19)
								break;*/

								writeStrInFile(eElement.getAttribute("name"),getText(node),targetFolder);
							}
						}
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

	public  String getText(Node node) throws Exception
	{
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
		xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		xform.transform(new DOMSource(node), new StreamResult(buf));
		return(buf.toString());

	}

	public void writeStrInFile(String state,String fileContent, String folderPath) throws IOException
	{
		File folder=new File(folderPath);

		if(!folder.exists())
			folder.mkdirs();

		File file=new File(folderPath + "\\" +state + ".xml");
		file.createNewFile();

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(fileContent);
		writer.close();

	}

	public void readPropertyFile() throws Exception
	{

		properties=new Properties();

		//File tFile=new File(System.getProperty("user.dir")+"/Extraction.properties");
		//prop.load(new FileInputStream(tFile));

		InputStream is=getClass().getClassLoader().getResourceAsStream("RetrieveStates.properties");
		properties.load(is);
	}
}
