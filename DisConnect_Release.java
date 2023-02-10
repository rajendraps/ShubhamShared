package test;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLoginInfo;
import com.novartis.swy.security.utils.SWYApplySecRFHelper;

public class DisConnect_Release 
{
	
	
	public static void main (String [] args)
	{
		DisConnect_Release obj=new DisConnect_Release();
		IDfSession sess=null;
		try 
		{
			String docbaseNam="SubWayX_PERF"; //SubWayX,SubWayR_DEV
			String usrName="hpa_dsfsdf";
			String passWD="xfdfdf";
			 sess=obj.getSession(docbaseNam,usrName,passWD);
			/* IDfSysObject sysobj=(IDfSysObject) sess.getObject(new DfId("090f44ae80129110")); 
			 System.out.println("Clinical object ["+sysobj.getString("r_object_type")+"]"); AAE581
			 System.out.println("Clinical object ["+sysobj.getString("swy_data_privacy")+"]");*/
			 
			 
			// DfQuery dql=new DfQuery("select (t1.attr1  + t2.attr2) as abc  from (select 4+5 as attr1 from dm_docbase_config ) t1, (select 6 as attr2 from dm_docbase_config ) t2");
			 //IDfCollection collection = dql.execute(sess, DfQuery.DF_EXEC_QUERY);
			 //IDfSysObject secRFObj=(IDfSysObject) sess.getObject(new DfId("090f44ae806f8f54"));
			
			 //select r_object_id, swy_filter_compound_topic,swy_filter_trial_num, swy_filter_product_code, swy_filter_region,swy_filter_submission_num, swy_filter_application_num,swy_filter_project_name,swy_filter_sub_country,swy_filter_authoring_entity from swy_sec_registration_form where r_object_id='090f45178029df1a';
			  //IDfSysObject secRFObj=(IDfSysObject) sess.getObject(new DfId("090f4517802d96d6"));
			 
			 IDfSysObject secRFObj=(IDfSysObject) sess.getObject(new DfId( "090f45278089fd3e")); //090f4517801c666a
			 System.out.println("Lock status "+secRFObj.isCheckedOut());
			 System.out.println("1. i_vStamp=["+secRFObj.getVStamp()+"]");
			 //secRFObj.checkout();
			 secRFObj.getObjectSession().beginTrans();
			 secRFObj.lock();
			 secRFObj.setObjectName("Test_Rend");
			 System.out.println("2. i_vStamp=["+secRFObj.getVStamp()+"]");
			 System.out.println("Lock status "+secRFObj.isCheckedOut());
			/*String dqlQualifier="(swy_secrf_application_mode='Automatic' or swy_secrf_application_mode='Default' or swy_secrf_application_mode is nullstring) and (swy_conflicting_secrf <> 'Non-Conflict' or swy_conflicting_secrf is nullstring )     "
			 		+ "and any swy_divisions='PH'   and artifact_name='Description and Composition'                                   and primary_group='Drug Product'   and subgroup='Composition" + 
			 		"'  and ( ('Automatic'='Manual' and swy_confidentiality='RSTD') or  'Automatic'='Automatic' ) and ( (domain='Quality'" + 
			 		"  and 'F' not in ('true','T')) or ( 'F' in ('true','T') and domain in (select distinct v.object_name from d2_dictionary d,d2_dictionary_value v where d.object_name = v.dictionary_name and d.object_name = 'Document Domains' and v.alias_value is not nullstring and v.i_position=d.i_position enable (ROW_BASED))))";
			*/
			 //String dqlQualifier="(swy_secrf_application_mode='Automatic' or swy_secrf_application_mode='Default' or swy_secrf_application_mode is nullstring) and (swy_conflicting_secrf <> 'Non-Conflict' or swy_conflicting_secrf is nullstring )                                           and primary_group='Central Trial Documents'   and subgroup='Protocol and Amendments'  and ( ('Automatic'='Manual' and swy_confidentiality='RSTD') or  'Automatic'='Automatic' ) and ( (domain='Clinical'  and 'F' not in ('true','T')) or ( 'F' in ('true','T') and domain in (select distinct v.object_name from d2_dictionary d,d2_dictionary_value v where d.object_name = v.dictionary_name and d.object_name = 'Document Domains' and v.alias_value is not nullstring and v.i_position=d.i_position enable (ROW_BASED))))";
			 /* String dqlQualifier="(swy_secrf_application_mode='Automatic' or swy_secrf_application_mode='Default' or swy_secrf_application_mode is nullstring) and (swy_conflicting_secrf <> 'Non-Conflict' or swy_conflicting_secrf is nullstring )"
			 		+ "and any swy_divisions='PH' and artifact_name='Application Form' and primary_group='Applicant Information' and subgroup='Application Form" + 
			 		"'  and ( ('Automatic'='Manual' and swy_confidentiality='RSTD') or  'Automatic'='Automatic' ) and ( (domain='Regulatory/Administrative'" + 
			 		"  and 'F' not in ('true','T')) or ( 'F' in ('true','T') and domain in (select distinct v.object_name from d2_dictionary d,d2_dictionary_value v where d.object_name = v.dictionary_name and d.object_name = 'Document Domains' and v.alias_value is not nullstring and v.i_position=d.i_position enable (ROW_BASED))))";
			  String docObjType="cd_reg_admin";
			 */
			 
			 System.out.println(sess.getDBMSName());
			 
			 
			  String docObjType="cd_quality";
			/* SWYApplySecRFHelper helpObj=new SWYApplySecRFHelper();
			 String predicate=helpObj.generateMorePredicates(dqlQualifier,secRFObj,docObjType);
			 System.out.println("[[predicate]] = {"+predicate+"}");
			 
			 String targetQuey=" where "+predicate + " and " + dqlQualifier;
			 targetQuery="select r_object_id from "+docObjType +" "+targetQuery;
			 System.out.println("[[predicate]] = {"+targetQuery+"}");*/
			 			 
			 
			 SWYApplySecRFHelper helpObj=new SWYApplySecRFHelper();
			// helpObj.applyChangesToDocAllVersions("090f45178028cae2",sess,false,false);
			 helpObj.isWorkflowAborted(secRFObj,sess,false);
			 System.out.println("3. i_vStamp=["+secRFObj.getVStamp()+"]");
			 helpObj.updateWFAttributesAfterAbort(secRFObj);
			 System.out.println("4. i_vStamp=["+secRFObj.getVStamp()+"]");
			 //helpObj.callLifecycleState("090f4517801be52b",sess,"(Abort WF)");
			 secRFObj.setTitle("wqdasddewew");
			 System.out.println("5. i_vStamp=["+secRFObj.getVStamp()+"]");
			 Thread.sleep(60000);
			 System.out.println("6. i_vStamp=["+secRFObj.getVStamp()+"]");
			 secRFObj.saveLock();
			 System.out.println("7. i_vStamp=["+secRFObj.getVStamp()+"]");
			 secRFObj.setString("resolution_label", "sadsff");			 
			 System.out.println("7.1==. i_vStamp=["+secRFObj.getVStamp()+"]");
			 
			 secRFObj.saveLock();
			 System.out.println("7.2==. i_vStamp=["+secRFObj.getVStamp()+"]");
			// secRFObj.cancelCheckout();
			 System.out.println("Lock status "+secRFObj.isCheckedOut());
			 System.out.println("8. i_vStamp=["+secRFObj.getVStamp()+"]");
			 secRFObj.getObjectSession().commitTrans();
			 System.out.println("Lock status "+secRFObj.isCheckedOut());
			 System.out.println("9. i_vStamp=["+secRFObj.getVStamp()+"]");
			 
			 
			/* IDfSysObject sysobj=(IDfSysObject) sess.newObject("cd_quality"); 
			 sysobj.setObjectName("Test_Rend");
			 sysobj.setContentType("msw12");
			 sysobj.setFile("C:\\Users\\shubhku1\\Desktop\\SNOW Tickets.docx");
			 sysobj.save();
			 
			 String objId=sysobj.getObjectId().getId();
			 System.out.println("OOObject Id ["+objId+"]");
			 
			 IDfSysObject sobj=(IDfSysObject) sess.getObject(new DfId(objId));
			 sobj.addRendition("C:\\KShubham\\PDFs\\LS16.6\\Documentum for LifeSciecnes Product Enhancements.pdf", "pdf");
			 sobj.save();
			 System.out.println("Rendition Done OOObject Id ["+objId+"]");
			 
			 IDfSysObject robj=(IDfSysObject) sess.getObject(new DfId(objId));
			 robj.addRendition("C:\\KShubham\\PDFs\\LS16.6\\Documentum for LifeSciecnes Product Enhancements.pdf", "pdf");
			// robj.save();
			 * */			 
			 
			 /*SWYApplySecRFHelper helpObj=new SWYApplySecRFHelper();
			 DfId tmpId=new DfId("090f4517802ae39a");
			 			 
			 IDfSysObject secRFObj=(IDfSysObject) sess.getObject(tmpId);
			 helpObj.applyingSecurity(tmpId.getId(),sess,true,true);*/						 
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		}
		finally
		{
			//System.out.println("Before Releasing Session ["+sess+"]");
			//obj.releaseSession(sess);
			try {
				System.out.println("Before Session Disconnecting");
				sess.disconnect();
				System.out.println("Session Disconnected");
			} catch (DfException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("After Releasing Session ["+sess+"]");
		}
	}
	
	IDfSessionManager smgr=null;
	public IDfSession getSession(String docbase, String user, String pwd) throws Exception
	{
		IDfClient client=new DfClient();
		String docbaseNam=docbase; //SubWayX,SubWayR_DEV
		String usrName=user;
		String passWD=pwd;
		
		//System.out.println("[[[" + client.encryptPassword("Test")+"]");
		
		
		
		smgr=client.newSessionManager();
		
		DfLoginInfo loginInfo=new DfLoginInfo();
		loginInfo.setUser(usrName);
		loginInfo.setPassword(passWD);
		
		/*loginInfo.setUser("ph_qdev_author_i1");
		loginInfo.setPassword("DEV1-Sep.phqdaui1");*/
		
		//loginInfo.setUser("ph_qdev_author_i1");
		//loginInfo.setPassword("DEV4+Aug.phqdaui1");
		smgr.setIdentity(docbaseNam,loginInfo);
		
		return smgr.getSession(docbaseNam);
	}
	
	public void releaseSession(IDfSession session)
	{
		smgr.release(session);
	}

}
