package test.com.novartis.swy.lc;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.emc.common.dctm.queries.DqlRepeatingValuesList;

public class CreateUserScript {
	public static void main(String[] args) {
		//DqlRepeatingValuesL
		
		try {
			File file = new File("C:\\Users\\SHUBHKU1\\Desktop\\Lotus\\Approach 2\\Implementation\\DM01-DEVAPPCT - China Test Users.xlsx");
			//creating a new file instance  
			FileInputStream fis = new FileInputStream(file);
			//obtaining bytes from the file  
			//creating Workbook instance that refers to .xlsx file  
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheet("R-Line");

			//creating a Sheet object to retrieve object  
			Iterator < Row > itr = sheet.iterator();
			//iterating over excel file

			ArrayList<String> user_login=new  ArrayList<String>();  //0th
			ArrayList<String> user_name=new  ArrayList<String>();   //1st
			ArrayList<String> user_address=new  ArrayList<String>();  //3rd
			ArrayList<String> user_pwd=new  ArrayList<String>();  //5th
			ArrayList<String> user_grp=new  ArrayList<String>();  // 2nd

			while (itr.hasNext()) {
				Row row = itr.next();
				Iterator < Cell > cellIterator = row.cellIterator();
				//iterating over each column  


				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					if (cell.getColumnIndex()==0)
					{
						user_login.add( cell.getStringCellValue());
					}
					else  if (cell.getColumnIndex()==1)
					{
						user_name.add(cell.getStringCellValue());
					}
					else  if (cell.getColumnIndex()==2)
					{
						user_grp.add( cell.getStringCellValue());
					}
					else  if (cell.getColumnIndex()==3)
					{
						user_address.add( cell.getStringCellValue());
					}
					else  if (cell.getColumnIndex()==5)
					{
						user_pwd.add( cell.getStringCellValue());
					}          

					// break;

				}        
			}
//System.out.println(user_login.size() +" "+ user_name.size()+" "+  user_grp.size()+" "+  user_address.size()+" "+ user_pwd.size());
			
if (user_login.size()==user_name.size() && user_name.size()==user_grp.size() && user_grp.size()==user_address.size() 
					&& user_address.size()==user_pwd.size())
			{

				System.out.println("######Start####");
				for (int i=0;i<user_login.size();i++)
				{

					System.out.println("########"+user_name.get(i)+"###########");


					String createUserDql="";

					//Generate User Creation script

					createUserDql="create,c,dm_user\nset,c,l,user_name\n"+user_name.get(i)+"\nset,c,l,user_login_name\n"
							+ ""+user_login.get(i)+"\nset,c,l,user_os_name\n"+user_login.get(i)+"\nset,c,l,user_address\n"+user_address.get(i)+"\n"
							+ "set,c,l,user_source\ninline password\nset,c,l,user_password\n"+user_pwd.get(i)+"\nset,c,l,user_privileges\n2\n"
							+ "set,c,l,owner_def_permit\n7\nset,c,l,world_def_permit\n3\nset,c,l,group_def_permit\n5\nset,c,l,user_state\n0\n"
							+ "set,c,l,client_capability\n4\nset,c,l,user_xprivileges\n32\nset,c,l,default_folder\n/Home\nsave,c,l";



					// Generate user's folder creation and its mapping with home cabinet script

		/*			
				createUserDql="CREATE dm_folder OBJECT SET object_name='"+user_name.get(i)+"', LINK '/Home'\ngo\n"
    	 		+ "UPDATE dm_folder OBJECT SET owner_name='"+user_name.get(i)+"' WHERE object_name='"+user_name.get(i)+"' "
    	 		+ "and folder('/Home')\ngo";


		*/	
					
		// Generate user's role mapping script


		/*	
    	 createUserDql="alter group "+user_grp.get(i)+" add '"+user_name.get(i)+"'\ngo";  
				 
		*/			System.out.println(createUserDql);

				}

				System.out.println("######End####");
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}