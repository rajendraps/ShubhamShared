package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfLogger;


/**
 *  
 * @author SHUBHKU1
 * 
 * INPUT file should have data in below format, at least 1st 3 columns of excel file since we are using 1st coulmns in the code 
 * 
 * 	r_object_id			r_folder_path		i_folder_id							object_name		format_reviewers																	domain		product_code	swy_divisions	swy_lotus_divisions	r_modifier	r_modify_date			i_vstamp	r_object_type			swy_confidentiality
 *	0902b6988332e4d4						0b02b69883038b5b,0b02b6988314702c	Please Update	Skerjanec Andrej,Zubel Angela,Tang Dejun,Schaffar Gregor,Li Yuhan-1,Jaegle Ulrike	Clinical	GPN013				SZ				SZ					dmadmin		18-12-2022 11.53		30		cd_clinical_tmf_doc		RSTD
 *	0902b6988332e50e						0b02b69883038b5b,0b02b6988314702c	Please Update	Li Yuhan-1,Skerjanec Andrej,Zubel Angela,Tang Dejun,Schaffar Gregor,Jaegle Ulrike	Clinical	GPN013				SZ				SZ					dmadmin		18-12-2022 11.53		38		cd_clinical_tmf_doc		RSTD
 *
 * OUTPUT
 * 
 *  r_object_id			r_folder_path																																																					i_folder_id							object_name		format_reviewers																	domain		product_code	swy_divisions	swy_lotus_divisions	r_modifier	r_modify_date			i_vstamp	r_object_type			swy_confidentiality
 *	0902b6988332e4d4	/Compound/G/GPN013/Clinical/GP2013/Studies/CIGG013A2301J/Study Report Interim CSR unblinded data (Jul 2016)/01 Report|/TMF Cabinet/G/GPN013/CIGG013A2301J/HQ Documents/02.Central Trial Documents/02.03.Reports					0b02b69883038b5b,0b02b6988314702c	Please Update	Skerjanec Andrej,Zubel Angela,Tang Dejun,Schaffar Gregor,Li Yuhan-1,Jaegle Ulrike	Clinical	GPN013				SZ				SZ					dmadmin		18-12-2022 11.53		30		cd_clinical_tmf_doc		RSTD
 *	0902b6988332e50e	/Compound/G/GPN013/Clinical/GP2013/Studies/CIGG013A2301J/Study Report Interim CSR unblinded data (Jul 2016)/01 Report|/TMF Cabinet/G/GPN013/CIGG013A2301J/HQ Documents/02.Central Trial Documents/02.03.Reports					0b02b69883038b5b,0b02b6988314702c	Please Update	Li Yuhan-1,Skerjanec Andrej,Zubel Angela,Tang Dejun,Schaffar Gregor,Jaegle Ulrike	Clinical	GPN013				SZ				SZ					dmadmin		18-12-2022 11.53		38		cd_clinical_tmf_doc		RSTD
 * 
 *
 */
public class GetFolderPath {

	String szDataFile="C:\\Users\\SHUBHKU1\\Downloads\\SubWay_Data\\test_data\\Reviewers.xlsx";
	//String szUserSheetName="";

	String dataFileLocation="";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GetFolderPath obj=new GetFolderPath();
		obj.fetchFolderPath();
	}

	public void fetchFolderPath() {
		//DqlRepeatingValuesL
		XSSFWorkbook wb =null;
		FileInputStream fis =null;
		//ArrayList<String> user_namesList=new  ArrayList<String>();  //0th
		try {
			File file = new File(szDataFile);
			//creating a new file instance  
			fis = new FileInputStream(file);
			//obtaining bytes from the file  
			//creating Workbook instance that refers to .xlsx file  
			wb = new XSSFWorkbook(fis);

			int numberOfSheets=wb.getNumberOfSheets();

			HashMap<String, String> foldrMap=new HashMap<>();

			if(numberOfSheets>0)
			{

				IDfSession session=null;
				DisConnect_Release seesionObject=new DisConnect_Release();
				try
				{
					session =seesionObject.getSession("Subqaw", "hpa_12sdsd", "abcdfdf");

					for(int i=0;i<numberOfSheets;i++)
					{

						XSSFSheet sheet = wb.getSheetAt(i);

						//creating a Sheet object to retrieve object  
						Iterator < Row > itr = sheet.iterator();
						//iterating over excel file
						int rowCounter=0;
						while (itr.hasNext()) {
							if(rowCounter==0)
							{
								rowCounter=rowCounter+1;
								continue;
							}
							String r_folder_path=null;
							Row row = itr.next();
							//Iterator < Cell > cellIterator = row.cellIterator();

							Cell cell=row.getCell(2);
							//iterating over each column  
							/*while (cellIterator.hasNext()) {

								Cell cell = cellIterator.next();*/
							if(row!=null)
							{
								if(cell!=null)
								{
									String dqlToExecute=null;

									if (cell.getColumnIndex()==2) // i_folder_id
									{
										String folderids[]=cell.getStringCellValue().split(",");

										if(folderids!=null && folderids.length>0)
										{
											r_folder_path=null;
											System.out.println(rowCounter+"...");
											for (int j=0;j<folderids.length;j++)
											{
												String folderid=folderids[j];

												if(foldrMap.containsKey(folderid))
												{
													if(r_folder_path==null || r_folder_path.isEmpty())
													{
														r_folder_path=foldrMap.get(folderid);
													}
													else
													{
														r_folder_path=r_folder_path+"|"+foldrMap.get(folderid);
													}
													System.out.println("Folder Object Id ["+folderid+"] exist in buffer");
													continue;
												}

												if(folderid!=null && !folderid.isEmpty() && folderid.length()==16)
												{
													if (dqlToExecute==null)
													{
														dqlToExecute="select r_object_id, r_folder_path from dm_folder_r where r_object_id in";
														dqlToExecute=dqlToExecute+"('"+folderid;
													}
													else
													{
														dqlToExecute=dqlToExecute+"','"+folderid;
													}
												}
											}

											if(dqlToExecute!=null && !dqlToExecute.trim().equalsIgnoreCase(""))
											{
												dqlToExecute=dqlToExecute+"')";
												dqlToExecute=dqlToExecute+" and r_folder_path is not nullstring";
												System.out.println(".."+dqlToExecute);
											}
										}
									}
									if(dqlToExecute!=null && !dqlToExecute.trim().equalsIgnoreCase(""))
									{
										try
										{
											IDfQuery query=new DfQuery();
											query.setDQL(dqlToExecute);
											IDfCollection coll=null;
											try
											{
												coll=query.execute(session, DfQuery.DF_EXEC_QUERY);

												while(coll.next())
												{
													String tmp_obj_id=coll.getString("r_object_id");
													String tmp_folder_path=coll.getString("r_folder_path");

													if(tmp_folder_path!=null && tmp_obj_id!=null)
													{
														if(r_folder_path==null || r_folder_path.isEmpty())
														{
															r_folder_path=tmp_folder_path;
														}
														else
														{
															r_folder_path=r_folder_path+"|"+tmp_folder_path;
														}

														if(foldrMap.containsKey(tmp_obj_id))
														{
															String tmpMapFldrPath=foldrMap.get(tmp_obj_id);
															String tmpMapFldrPaths[]=tmpMapFldrPath.split("|");

															List<String> folderPathList=Arrays.asList(tmpMapFldrPaths);

															if(!folderPathList.contains(tmpMapFldrPath))
															{
																tmpMapFldrPath=tmpMapFldrPath+"|"+tmp_folder_path;

																foldrMap.put(tmp_obj_id, tmpMapFldrPath);
															}
														}
														else
														{
															foldrMap.put(tmp_obj_id, tmp_folder_path);
														}
													}
												}
											}
											finally
											{
												coll.close();
												coll=null;
											}
										}
										catch(Exception ex)
										{
											ex.printStackTrace();
										}
									}
									if(r_folder_path!=null && !r_folder_path.isEmpty())
									{
										Cell fPathCell = row.getCell(1);
										if(fPathCell == null) {
											fPathCell=row.createCell(1);
										}
										fPathCell.setCellValue(r_folder_path);
									}

								}

								rowCounter=rowCounter+1;
							}
						}
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					seesionObject.releaseSession(session);
				}
			}
			//fis.close();

			FileOutputStream out = new FileOutputStream(szDataFile);
			wb.write(out);
			wb.close();
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				if(wb!=null)
					wb.close();
				if(fis!=null)
					fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	public void getData(ArrayList<String> szUSers, ArrayList<String> filesList)
	{
		try 
		{

			for(int j=0;j<filesList.size();j++)
			{

			}

		}
		catch(Exception ex)
		{

		}
	}


	public void readDataFile(String filePath)
	{
		try 
		{



		}
		catch(Exception ex)
		{

		}
	}

	public ArrayList<String> readxlsxFileDir()
	{
		ArrayList<String> fileList=new ArrayList<String>();

		try
		{
			File dir = new File(dataFileLocation);

			if(dir.isDirectory())
			{
				File[] files=dir.listFiles();

				for(int i=0;i<files.length;i++)
				{
					if(files[i].getName().toLowerCase().endsWith(".xlsx"))

						fileList.add(files[i].getAbsolutePath());
				}
			}
		}
		catch (Exception ex)
		{

		}
		return fileList;
	}
}
