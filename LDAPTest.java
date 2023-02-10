package test;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LDAPTest{

	String ldap = "cds.eu.novartis.net";
	int port = 3636;
	String ldapUrl = "ldaps://" + ldap + ":" + port;

	// service user
	String serviceUserDN = "ou=SubWay_all_prd,ou=applications,ou=intranet,dc=novartis,dc=com";
	String serviceUserPassword = "zxxxxxx";
	/*xfdffff4645 --tst
	sdfds54654 pft
	sdfdsf56546 --dev
	dfgfdg45456 -prd*/

	public static void main(String... args) {

		LDAPTest obj=new LDAPTest();
		obj.searchGroup();
		obj.searchUser();
	}

	public void searchUser()
	{
		DirContext serviceCtx =null;
		try
		{
			String identifyingAttribute = "uid";
			String base = "ou=people,ou=intranet,dc=novartis,dc=com";
			String[] attributeFilter = { identifyingAttribute ,"novaCountryCode","novaCountryName","cn","uid","novaUniqueDisplayName","employeeType", "isMemberOf"};


			SearchControls sc = new SearchControls();
			sc.setDerefLinkFlag(true);

			sc.setReturningAttributes(attributeFilter);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String searchFilter = "(|";
			searchFilter=searchFilter	+ "(" + identifyingAttribute + "=" + "SUKANRA1" + ")";
			searchFilter =searchFilter+ "(" + identifyingAttribute + "=" + "shubhku1" + ")";
			searchFilter =searchFilter+ "(" + identifyingAttribute + "=" + "quinnja2" + ")";
			searchFilter =searchFilter+ "(" + identifyingAttribute + "=" + "wechsas1" + ")";
			searchFilter =searchFilter+ "(" + identifyingAttribute + "=" + "simpsna1" + ")";
			searchFilter =searchFilter+")";
			
			serviceCtx =setLDAPContexts();
			NamingEnumeration<SearchResult> results = serviceCtx.search(base, searchFilter, sc);
			int usrCntr=1;
			while (results.hasMore()) {

				// get the users DN (distinguishedName) from the result
				SearchResult result = results.next();
				String distinguishedName = result.getNameInNamespace();

				Attributes attr= result.getAttributes();

				NamingEnumeration<String> idNum=attr.getIDs();

				/*while(idNum.hasMoreElements())
				{
					String idstr=idNum.nextElement();
					System.out.println("idstr=["+idstr+"]");
				}*/

				/*users attributes*/
				String novaCountryNameStr=(String) attr.get("novaCountryName").get();
				String novaCountryCodeStr=(String) attr.get("novaCountryCode").get();
				String uidStr=(String) attr.get("uid").get(); 
				String nameStr=(String) attr.get("cn").get(); 
				String uniqueDisplayNameStr=(String) attr.get("novaUniqueDisplayName").get(); 
				String novaemployeeType=(String) attr.get("employeeType").get(); 
				String novaisMemberOf=(String) attr.get("isMemberOf").get(); 

				System.out.println(usrCntr+". "+"BaseDn=["+distinguishedName+"] novaEmployeeType=["+novaemployeeType+"] UID=["+uidStr+"] Name=["+nameStr+"] uniqueDisplayName ["+uniqueDisplayNameStr+"] CountryCode=["+novaCountryCodeStr+"] Country=["+novaCountryNameStr+"] isMemberOf["+novaisMemberOf+"]");
				usrCntr=usrCntr+1;
				
				// System.out.println( distinguishedName+" ^^^^ "+result.toString() + " ***** "+attr.toString() +" AAA "+novaCountryStr);

				// attempt another authentication, now with the user
				/*Properties authEnv = new Properties();
	            authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	            authEnv.put(Context.PROVIDER_URL, ldapUrl);
	            authEnv.put(Context.SECURITY_PRINCIPAL, distinguishedName);
	            authEnv.put(Context.SECURITY_CREDENTIALS, password); // password of the user
	            new InitialDirContext(authEnv);

	            System.out.println("Authentication successful");*/
				//return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serviceCtx != null) {
				try {
					serviceCtx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void searchGroup()
	{
		DirContext serviceCtx =null;
		try
		{
			String identifyingAttribute = "cn";
			String base = "ou=applications,ou=intranet,dc=novartis,dc=com"; 
			String[] attributeFilter = { "cn",  "novaEntitlementDescription"};

			SearchControls sc = new SearchControls();
			sc.setDerefLinkFlag(true);

			sc.setReturningAttributes(attributeFilter);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String searchFilter = "(|";
			searchFilter=searchFilter	+ "(" + identifyingAttribute + "=" + "*cn*" + ")"; // China only groups
			searchFilter =searchFilter+")";
			serviceCtx =setLDAPContexts();
			NamingEnumeration<SearchResult> results = serviceCtx.search(base, searchFilter, sc);
			int grpCntr=1;
			while (results.hasMore()) {

				// get the users DN (distinguishedName) from the result
				SearchResult result = results.next();
				String distinguishedName = result.getNameInNamespace();

				Attributes attr= result.getAttributes();

				NamingEnumeration<String> idNum=attr.getIDs();

				String nameStr=(String) attr.get(identifyingAttribute).get(); 
				String novaEntitlementDescription=(String) attr.get("novaEntitlementDescription").get(); 

				System.out.println(grpCntr+". "+"BaseDn=["+distinguishedName+"]cn=["+nameStr+"]novaEntitlementDescription=["+novaEntitlementDescription+"] ");
				grpCntr=grpCntr+1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serviceCtx != null) {
				try {
					serviceCtx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public DirContext setLDAPContexts() throws Exception
	{
		DirContext serviceCtx = null;

		// use the service user to authenticate
		Properties serviceEnv = new Properties();
		serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
		serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		serviceEnv.put(Context.SECURITY_PRINCIPAL, serviceUserDN);
		serviceEnv.put(Context.SECURITY_CREDENTIALS, serviceUserPassword);
		serviceCtx = new InitialDirContext(serviceEnv);
		return serviceCtx;
	}
}
