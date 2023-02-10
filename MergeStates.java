package test.com.novartis.swy.lc;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MergeStates {

	public static void main (String[] args)
	{
		System.out.println("Processing Start ============================");
		MergeStates object=new MergeStates();
		object.prepareXML();
		System.out.println("Processing End ============================");
	}

	public void prepareXML()
	{
		try
		{
			String sourceFolderPath="C:\\Users\\SHUBHKU1\\Desktop\\16.6.1 SecRF\\4.2 LC merged with 16.6.1\\DEVAPPCt Temp\\d2_lifecycle_config\\contents\\QM Cat 2";
			String targetFolderPath="C:\\Users\\SHUBHKU1\\Desktop\\16.6.1 SecRF\\4.2 LC merged with 16.6.1\\DEVAPPCt Temp\\d2_lifecycle_config\\contents\\QM Cat 2\\Merged _State";
			String outPutFileName="QM Cat 2 Controlled Document Lifecycle.xml";
			
			File folderObj=new File(sourceFolderPath);
			StringBuffer buffer=new StringBuffer();

			if (folderObj.isDirectory())
			{
				DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

				DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

				Document document = documentBuilder.newDocument();

				// root element
				Element root = document.createElement("d2lifecycle");
				document.appendChild(root);

				Attr attr = document.createAttribute("state_attribute");
				attr.setValue("");
				root.setAttributeNode(attr);

				attr = document.createAttribute("execute_actions_on_start");
				attr.setValue("true");
				root.setAttributeNode(attr);


				File stateFiles[]=folderObj.listFiles();
				if(stateFiles!=null && stateFiles.length>0)
				{
					for (int idx=0;idx<stateFiles.length;idx++)
					{

						if(!stateFiles[idx].isDirectory())
						{
							String filePath=sourceFolderPath + "\\" + stateFiles[idx].getName();
							System.out.println(filePath);
							Document stateDoc=  documentBuilder.parse(new File( filePath ));

							Element stateRoot = stateDoc.getDocumentElement();
							Node imported_node = document.importNode(stateRoot, true);
							Element eElement = (Element) imported_node;
							root.insertBefore(eElement,null);
						}
					}

				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.METHOD,"xml");

				DOMSource domSource = new DOMSource(document);

				File targetFolderObj=new File(targetFolderPath);
				if(!targetFolderObj.exists())
					targetFolderObj.mkdirs();

				String xmlFilePath=targetFolderPath + "\\" + outPutFileName;
				File targetFile=new File(xmlFilePath);
				targetFile.createNewFile();
				System.out.println("Pathhhh=["+xmlFilePath+"]");

				FileOutputStream outStream = new FileOutputStream(targetFile); 
				StreamResult streamResult = new StreamResult(outStream);


				// If you use
				// StreamResult result = new StreamResult(System.out);
				// the output will be pushed to the standard output ...
				// You can use that for debugging 
				System.out.println("Done creating XML Fileeeeeeeeeee");
				transformer.transform(domSource, streamResult);

				System.out.println("Done creating XML File");

			}			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
