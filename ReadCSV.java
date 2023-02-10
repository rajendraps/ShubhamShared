package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.utils.QueryUtils;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**Code accepts 2 input file paths and check whether documents objects have only CN users currently configured. If other than CN users are also available 
 * then no issue since after removal of CN users , other users are present who can still access.
 * But if documents has only CN users then in that case code provide details of those objects, that we need to share with Business.
 * 
 * In code we have to provide the path where these files are kept, no need to mention the filename as code process all the files of that folder.
 * 
 * Input files 1 and 2 should be placed at 2 different location. These input files should be in csv only
 *
 * 
 * Input files 1: Objects details with CN users only,specific to the role attributes through SQL/ DQL , directly from tables 
 * 
 * 	R_OBJECT_ID,AUTHORS,DOMAIN,R_MODIFIER,R_MODIFY_DATE,I_VSTAMP,R_OBJECT_TYPE,SWY_CONFIDENTIALITY
 * 	0902b6988a34e178,Bai Lingyan,Clinical,Paulson Caylee,4-12-2022 4.21 PM,30,cd_clinical,BUO
 *	0902b6988a34e178,Meng Yang,Clinical,Paulson Caylee,4-12-2022 4.21 PM,30,cd_clinical,BUO
 *	0902b6988a34e178,Sun Feng-2,Clinical,Paulson Caylee,4-12-2022 4.21 PM,30,cd_clinical,BUO
 *	0902b6988a34e178,Wang Yingying,Clinical,Paulson Caylee,4-12-2022 4.21 PM,30,cd_clinical,BUO
 * 
 * Input files 2: Object details with complete list of all users where any user is from China (CN) specific to role attributes, through SQL/ DQL , directly from tables
 * 	
 * "R_OBJECT_ID","AUTHORS","DOMAIN","R_MODIFIER","R_MODIFY_DATE","I_VSTAMP","R_OBJECT_TYPE","SWY_CONFIDENTIALITY"
 *	"0902b6988a34e178","Meng Yang","Clinical","Paulson Caylee",12-APR-22,30,"cd_clinical","BUO"
 *	"0902b6988a34e178","Bai Lingyan","Clinical","Paulson Caylee",12-APR-22,30,"cd_clinical","BUO"
 *	"0902b6988a34e178","Fang Guo","Clinical","Paulson Caylee",12-APR-22,30,"cd_clinical","BUO"
 *	"0902b6988a34e178","Sun Feng-2","Clinical","Paulson Caylee",12-APR-22,30,"cd_clinical","BUO"
 *	"0902b6988a34e178","Margolskee Alison","Clinical","Paulson Caylee",12-APR-22,30,"cd_clinical","BUO"
 *	"0902b6988a34e178","Adam Sylvie","Clinical","Paulson Caylee",12-APR-22,30,"cd_clinical","BUO"
 *	"0902b6988a34e178","Chen Yang-4","Clinical","Paulson Caylee",12-APR-22,30,"cd_clinical","BUO"
 *	"0902b6988a34e178","Wang Yingying","Clinical","Paulson Caylee",12-APR-22,30,"cd_clinical","BUO"
 *	"0902b6988a34e178","Schilling Angela","Clinical","Paulson Caylee",12-APR-22,30,"cd_clinical","BUO"
 * 
 * 
 * Output: Log file will generate at "C:\Documentum\logs\dfc.log"
 * 
 *~0902b6988916ea8d:::~0902b6988916ea8d~clinical~swy_sec_registration_form~readers~1~1~CN Users count >= All Users~null
 *Input sheet 1 and 2 is having same no of performers against specific attribute and those are from CN only. But respective data is not accessible in docbase due any more.
 *~0902b6988c0be8b8:::~0902b6988c0be8b8~clinical~cd_clinical_tmf_doc~readers~1~1~CN Users count >= All Users~0902b6988c0be8b8~Clinical~cd_clinical_tmf_doc~KJX839~null~0902b6988c0be8b8~~0~CURRENT#1.0
 *~0902b698895265a8:::~0902b698895265a8~clinical~cd_clinical_tmf_doc~readers~1~1~CN Users count >= All Users~0902b698895265a8~Clinical~cd_clinical_tmf_doc~AIN457~null~0902b698895265a8~~0~CURRENT#1.0
 *~0902b69889166116:::~0902b69889166116~clinical~swy_sec_registration_form~readers~1~1~CN Users count >= All Users~null
 *~0902b698895265a9:::~0902b698895265a9~clinical~cd_clinical_tmf_doc~readers~1~1~CN Users count >= All Users~0902b698895265a9~Clinical~cd_clinical_tmf_doc~AIN457~null~0902b698895265a9~~0~CURRENT#1.0
 *~0902b698821338b7:::~0902b698821338b7~quality~cd_quality~format_reviewers~1~1~CN Users count >= All Users~0902b698821338b7~Quality~cd_quality~~null~0902b69880166a31~T20095a9856d0233~0~CURRENT#2.0
 *~0902b698801a0d46:::~0902b698801a0d46~quality~cd_quality~format_reviewers~1~1~CN Users count >= All Users~0902b698801a0d46~Quality~cd_quality~LCZ696~LCZ696-ABA~0902b698801a0d46~090095a982c9a091~0~1.0#CURRENT#Final
 *~0902b698801a66a5:::~0902b698801a66a5~quality~cd_quality~format_reviewers~1~1~CN Users count >= All Users~0902b698801a66a5~Quality~cd_quality~~null~0902b698801a66a5~090095a9851557e1~0~1.0#CURRENT#Final
 *~0902b69888cf7b4b:::~0902b69888cf7b4b~quality~cd_quality~authors~1~1~CN Users count >= All Users~0902b69888cf7b4b~Quality~cd_quality~EME735, EDQ011~EME735 No salt~0902b69888cf50d9~~0~0.4
 *~0902b698817e3348:::~0902b698817e3348~regulatory/administrative~cd_reg_admin~authors~1~1~CN Users count >= All Users~0902b698817e3348~Regulatory/Administrative~cd_reg_admin~MIW815~null~0902b698817e3348~090095ab84e314d2~0~2.0#CURRENT#Final
 *~0902b6988bf994a4:::~0902b6988bf994a4~clinical~cd_clinical_tmf_doc~authors~1~1~CN Users count >= All Users~0902b6988bf994a4~Clinical~cd_clinical_tmf_doc~DRB436~null~0902b6988bf994a4~~0~CURRENT#1.0
 *~0902b698817e5580:::~0902b698817e5580~regulatory/administrative~cd_reg_admin~authors~1~1~CN Users count >= All Users~0902b698817e5580~Regulatory/Administrative~cd_reg_admin~BYL719~null~0902b698817e5580~090095ab825e3f17~0~2.0#CURRENT#Final
 *~0902b698843ab4e3:::~0902b698843ab4e3~clinical~cd_clinical_tmf_doc~approvers~1~0~All Users Count Dosen't have Object ID~null
 *Input sheet 2 doesn't have object id.
 *~0902b6988bff147f:::~0902b6988bff147f~clinical~cd_clinical_tmf_doc~approvers~1~0~All Users Count Dosen't have Object ID~null
 *~0902b6988cbc4478:::~0902b6988cbc4478~clinical~cd_clinical_tmf_doc~approvers~1~0~All Users Count Dosen't have Object ID~null
 *~0902b6988a3bb111:::~0902b6988a3bb111~clinical~cd_clinical_tmf_doc~approvers~1~0~All Users Count Dosen't have Object ID~null
 *~0902b6988c73b4f0:::~0902b6988c73b4f0~clinical~cd_clinical~approvers~6~0~All Users Count Dosen't have Object ID~null
 *Input sheet 2 doesn't have object id.
 * 
 * @author SHUBHKU1
 *
 */



public class ReadCSV 
{

	public static final char delimiter = ',';

	public static void main (String[] args)	
	{
		try
		{
			ReadCSV obj=new ReadCSV();
			//obj.openReadCSV("C:\\Users\\SHUBHKU1\\OneDrive - Novartis Pharma AG\\Documents\\KShubham\\SHUBHAM\\Lotus Datafix\\PROD_SWYquery\\NAUsersForSensitiveProd.csv");

			ArrayList<String> cnUsersFileList=obj.readCSVFileDir("C:\\Users\\SHUBHKU1\\OneDrive - Novartis Pharma AG\\Documents\\KShubham\\SHUBHAM\\Lotus Datafix\\PROD_SWYquery\\20220924_CN user Access");

			/*for(int i=0;i<cnUsersFileList.size();i++)
		{
			System.out.println(cnUsersFileList.get(i));
		}*/

			ArrayList<String> cnUsersAllUsersFileList=obj.readCSVFileDir("C:\\Users\\SHUBHKU1\\OneDrive - Novartis Pharma AG\\Documents\\KShubham\\SHUBHAM\\Lotus Datafix\\PROD_SWYquery\\20221024_CN user Access_All_users");

			HashMap<String, HashMap<String, Integer> > objIdDataofAllCNUsers=obj.openReadCSV(cnUsersFileList);
			System.out.println("**************************");
			//System.gc();
			HashMap<String, HashMap<String, Integer> > allUsersDataofAllCNUsers=obj.openReadCSV(cnUsersAllUsersFileList);

			ArrayList<String[]> processedResList=obj.processData(objIdDataofAllCNUsers,allUsersDataofAllCNUsers);
			System.out.println("processedResList Count ["+processedResList.size()+"] ");
			System.out.println("******Retrieving Data From Docbase**********");
			//DfLogger.debug(obj,"******Retrieving Data From Docbase**********", null, null);
			HashMap<String, String> dataProdinfo=obj. retrievDataFromRepo(processedResList);
			DfLogger.debug(obj,"******Printing Data From Docbase, No. of Data from Docbase["+dataProdinfo.size()+"]**********", null, null);
			System.out.println("******Printing Data From Docbase, No. of Data from Docbase["+dataProdinfo.size()+"]********** ");
			for (int i=0;i<processedResList.size();i++)
			{
				String objId=processedResList.get(i)[0];
				String docbasedata=dataProdinfo.get(objId);
				DfLogger.debug(obj, "~"+objId+":::~"+processedResList.get(i)[1] +"~"+docbasedata, null, null);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public HashMap<String, String> retrievDataFromRepo(ArrayList<String[]> processedResList) throws DfException
	{
		HashMap<String, String> dataMap=new HashMap<String,String>();
		int batchSize=70;
		int versionCount=processedResList.size();
		//int versionCount=80;
		int no_of_batch=versionCount/batchSize;
		int doc_count_outside_batch=versionCount%batchSize;
		System.out.println("Total Versions available ["+versionCount+"] No. Of batch["+no_of_batch+"] Documents count outside batch ["+doc_count_outside_batch+"] BatchSize["+batchSize+"]");
		//DfLogger.debug(this, "Total Versions available ["+versionCount+"] No. Of batch["+no_of_batch+"] Documents count outside batch ["+doc_count_outside_batch+"] BatchSize["+batchSize+"]", null, null);
		int counter=0;
		String versionIdsAsPredicate=null;
		int total_batch=doc_count_outside_batch>0?no_of_batch+1:no_of_batch;


		for (int batchCounter=0;batchCounter<total_batch;batchCounter++)
		{
			versionIdsAsPredicate=null;
			if(batchCounter==no_of_batch)
				batchSize=doc_count_outside_batch;
			for(int j=0;j<batchSize;j++)
			{
				if(versionIdsAsPredicate==null || versionIdsAsPredicate.trim().equalsIgnoreCase(""))
				{
					versionIdsAsPredicate="'"+processedResList.get(counter)[0]+"'";
				}
				else
				{
					versionIdsAsPredicate=versionIdsAsPredicate+ ",'"+processedResList.get(counter)[0]+"'";
				}
				counter=counter+1;
			}

			//System.out.println("Batch ["+(batchCounter+1)+"] Processed records ["+counter+"] Version Object Ids ["+versionIdsAsPredicate+"]");
			//DfLogger.debug(this, "Processing Object Id ["+docObjId+"] Batch ["+(batchCounter+1)+"] Processed records ["+counter+"] Versions Object IDs as predicate ["+versionIdsAsPredicate+"]", null, null);

			String dqlToExecute=null;

			dqlToExecute="select r_object_id,domain,r_object_type,i_chronicle_id,mig_r_object_id,product_code,drug_substance_name,is_placeholder,r_version_label from cd_common_ref_model(all) where "
					+ "r_object_id in ("+versionIdsAsPredicate+")";

			DfLogger.debug(this, batchCounter +":: "+dqlToExecute, null, null);
			if(dqlToExecute!=null && !dqlToExecute.trim().equalsIgnoreCase(""))
			{
				IDfSession session=null;
				DisConnect_Release seesionObject=new DisConnect_Release();
				try
				{
					session =seesionObject.getSession("SubWayX", "hpa_aaa1", "xxxx@yyy$zzz^aaa");

					IDfQuery query=new DfQuery();
					query.setDQL(dqlToExecute);
					IDfCollection coll=null;
					try
					{
						coll=query.execute(session, DfQuery.DF_EXEC_QUERY);
						while(coll.next())
						{
							String r_object_id=coll.getString("r_object_id");
							String domain=coll.getString("domain");
							String r_object_type=coll.getString("r_object_type");
							String product_code=coll.getString("product_code");
							String drug_substance_name=coll.getString("drug_substance_name");
							String i_chronicle_id=coll.getString("i_chronicle_id");
							String mig_r_object_id=coll.getString("mig_r_object_id");
							String is_placeholder=coll.getString("is_placeholder");
							String r_version_label=coll.getAllRepeatingStrings("r_version_label","#");

							String row=r_object_id + "~"+ domain + "~"+ r_object_type + "~"+ product_code + "~"+ drug_substance_name + "~"+ i_chronicle_id + "~"+
									mig_r_object_id + "~" + is_placeholder + "~"+ r_version_label ;
							//DfLogger.debug(this, "Docbase Data ~"+row, null, null);

							dataMap.put(r_object_id, row);
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
				finally
				{
					seesionObject.releaseSession(session);
				}

			}
		}
		return dataMap;
	}

	public ArrayList<String[]> processData(HashMap<String, HashMap<String, Integer> > cnUserMap,HashMap<String, HashMap<String, Integer> > allUsersMap)
	{
		//HashMap<String, HashMap<String, Integer> > resultMAp=new HashMap<String, HashMap<String, Integer> >();
		ArrayList<String[]> processResult= new ArrayList<String[]>();
		if(cnUserMap.size()==allUsersMap.size())
		{
			System.out.println("Hahahahahaha");

			Set<Entry<String, HashMap<String, Integer>>> outentry=cnUserMap.entrySet();
			Iterator<Entry<String, HashMap<String, Integer>>> outIterator=outentry.iterator();
			while(outIterator.hasNext())
			{
				Entry<String, HashMap<String, Integer>>  inentry=outIterator.next();
				String performerstype=inentry.getKey();
				HashMap<String, Integer> cnDataMap=inentry.getValue();

				HashMap<String, Integer> objIdsAllUsersCountMap=allUsersMap.get(performerstype);


				Set<Entry<String,  Integer>> incnEntry=cnDataMap.entrySet();
				Iterator<Entry<String, Integer>> inCnIterator=incnEntry.iterator();
				while(inCnIterator.hasNext())
				{
					Entry<String, Integer> cnData=inCnIterator.next();
					String chkObjId=cnData.getKey();
					String objIdTocheck=chkObjId.split("~")[0];
					int chkObjIdValue=cnData.getValue();

					if(objIdsAllUsersCountMap.containsKey(chkObjId))
					{
						int tmpgetValue=objIdsAllUsersCountMap.get(chkObjId);
						if(tmpgetValue>chkObjIdValue)
						{
							//do nothing , AllUsers are > only CN users

						}
						else
						{
							// capture data
							String tmpStr=chkObjId + "~" + performerstype + "~"+ chkObjIdValue +"~"+ tmpgetValue + "~" + "CN Users count >= All Users";
							processResult.add(new String [] {objIdTocheck,tmpStr});
						}
					}
					else
					{
						String tmpStr=chkObjId + "~" + performerstype + "~"+ chkObjIdValue +"~"+ "0"+ "~"+ "All Users Count Dosen't have Object ID";
						processResult.add(new String [] {objIdTocheck,tmpStr});
					}

				}
			}

		}
		return processResult;
	}


	public ArrayList<String> readCSVFileDir(String csvDir)
	{
		ArrayList<String> fileList=new ArrayList<String>();

		try
		{
			File dir = new File(csvDir);

			if(dir.isDirectory())
			{
				File[] files=dir.listFiles();

				for(int i=0;i<files.length;i++)
				{
					if(files[i].getName().endsWith(".csv"))

						fileList.add(files[i].getAbsolutePath());
				}
			}
		}
		catch (Exception ex)
		{

		}
		return fileList;
	}

	public HashMap<String, HashMap<String, Integer> > openReadCSV(ArrayList<String> fileList)
	{
		HashMap<String, HashMap<String, Integer> > performersCount=new HashMap<String, HashMap<String, Integer> > ();
		try {

			for(int j=0;j<fileList.size();j++)
			{

				String csvFilePath=fileList.get(j);
				FileReader filereader = new FileReader(csvFilePath);

				// create csvParser object with
				// custom separator semi-colon
				CSVParser parser = new CSVParserBuilder().withSeparator(delimiter).build();

				// create csvReader object with parameter
				// filereader and parser
				CSVReader csvReader = new CSVReaderBuilder(filereader)
						.withCSVParser(parser)
						.build();

				// Read all data at once
				List<String[]> allData = csvReader.readAll();
				ArrayList<String> csvUserNameList = new ArrayList<String> ();

				String colName="";
				HashMap<String, Integer> objIdsCount=new HashMap<String, Integer> ();

				if(allData!=null && allData.size()>0)
				{
					for(int i=0;i<allData.size();i++)
					{
						if(i==0)
						{
							colName=allData.get(i)[1].toLowerCase();
							//System.out.println(colName);
						}


						if (i>0)
						{
							/*for (String cell : allData.get(i)) {
								csvUserNameList.add(cell);
								//System.out.print(cell + "\t");
							}
							System.out.println();*/

							String objId=allData.get(i)[0].toLowerCase();
							String objdomain=allData.get(i)[2].toLowerCase();
							String objtype=allData.get(i)[6].toLowerCase();
							objId=objId +"~"+objdomain +"~"+objtype;
							//String performers=allData.get(i)[1].toLowerCase();
							//System.out.println(objId + " \t " +performers);


							if(objIdsCount.containsKey(objId))
							{
								int value=objIdsCount.get(objId);
								value=value+1;
								objIdsCount.put(objId, value);
							}
							else
							{
								objIdsCount.put(objId, 1);
							}

						}
					}
				}
				else
				{
					//System.out.println(csvFilePath);
				}

				if(!colName.isEmpty())
				{
					System.out.println("adding data for ["+colName+"]");
					if(performersCount.containsKey(colName))
					{
						HashMap<String, Integer> tmpMap =performersCount.get(colName);

						Set<Entry<String, Integer>> tmpSet=objIdsCount.entrySet();

						Iterator<Entry<String, Integer>> iterator=tmpSet.iterator();

						while(iterator.hasNext())
						{
							Entry<String, Integer> entry=iterator.next();
							String objectid=entry.getKey();
							int extvalue=entry.getValue();

							if(tmpMap.containsKey(objectid))
							{
								int tmpvalue=objIdsCount.get(objectid);
								tmpvalue=tmpvalue+extvalue;
								tmpMap.put(objectid, tmpvalue);
							}
							else
							{
								tmpMap.put(objectid, extvalue);
							}

						}
						performersCount.put(colName, tmpMap);
					}
					else
					{
						performersCount.put(colName, objIdsCount);
					}
				}

				Set<Entry<String, HashMap<String, Integer>>> outentry=performersCount.entrySet();
				Iterator<Entry<String, HashMap<String, Integer>>> outIterator=outentry.iterator();
				while(outIterator.hasNext())
				{
					Entry<String, HashMap<String, Integer>>  inentry=outIterator.next();
					String performerstype=inentry.getKey();
					HashMap<String, Integer> dataMap=inentry.getValue();
					System.out.println(performerstype +"\t" + dataMap.size());
				}

			}
		}
		//	String user_list=StringUtils.join(csvUserNameList, '#');

		//System.out.println("["+user_list+"]");
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return performersCount;
	}
}
