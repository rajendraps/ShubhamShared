package test;


/**
 * Input:
 * 
 * 	Pamula Vinay	ph_cd_clinical_doc_approvers	Novartis Pharma
 *	Pamula Vinay	ph_cd_clinical_doc_authors_int	Novartis Pharma
 *	Pamula Vinay	ph_cd_clinical_doc_readers		Novartis Pharma
 *
 *	Sachdeva Ext Sheena-1	cd_clinical	
 *	Sachdeva Ext Sheena-1	cd_clinical_doc_authors	
 *	Sachdeva Ext Sheena-1	cd_clinical_doc_readers	
 *	Sachdeva Ext Sheena-1	cd_safety	
 *	Sachdeva Ext Sheena-1	swy_tmf_authors	
 *	Sachdeva Ext Sheena-1	sz_cd_clinical_doc_readers	Sandoz

 *
 * Output:
 * Pamula Vinay	WARN#ph_cd_clinical_doc_approvers#ph_cd_clinical_doc_authors_int#ph_cd_clinical_doc_readers#ph_cd_clinical_doc_readers_int#ph_cd_clinical_tmf_doc_readers#ph_cd_safety_doc_readers
 *
 * Sachdeva Ext Sheena-1	OK#sz_cd_clinical_doc_readers
 * 
 * List out the users who is member of SZ , indicated with OK
 *List out the users who is not member of SZ , indicated with WARN 
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.documentum.fc.common.DfLogger;
import com.emc.common.dctm.queries.DqlRepeatingValuesList;

public class ReadXLSX {

	public static void main(String[] args) 
	{
		ReadXLSX obj=new ReadXLSX();
		obj.test();
		//obj.tmp();
	}

	public void tmp()
	{
		String tmpStr="cn=cd_native_pdf_exporters,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=cd_native_pdf_exporters,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=cd_native_pdf_exporters,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=DYN_ALL_NOVARTIS,ou=groups,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_bulk_doc_exporters,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_bulk_doc_exporters,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_bulk_doc_exporters,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_bus_admins_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_bus_admins_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_bus_admins_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_approvers_int,ou=SubWay_ALL_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_readers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_readers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_doc_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_secrf_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_secrf_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_secrf_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_template_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_template_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_template_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_template_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_template_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_template_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_template_reviewers_int,ou=SubWay_ALL_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_template_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_ad_promo_template_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_bulk_doc_exporters,ou=SubWay_ALL_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_bulk_doc_exporters,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_bulk_doc_exporters,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_bus_admins_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_bus_admins_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_bus_admins_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_readers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_readers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_doc_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_secrf_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_secrf_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_secrf_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_template_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_template_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_template_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_template_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_template_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_template_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_template_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_template_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_corres_template_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_bulk_doc_exporters,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_bulk_doc_exporters,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_bulk_doc_exporters,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_bus_admins_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_bus_admins_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_bus_admins_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_readers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_readers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_doc_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_secrf_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_secrf_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_secrf_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_template_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_template_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_template_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_template_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_template_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_template_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_template_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_template_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_labeling_template_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_product_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_product_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_quality_doc_admins_regcmc_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_quality_doc_admins_regcmc_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_quality_doc_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_quality_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_quality_doc_authors_regcmc_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_quality_doc_authors_regcmc_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_quality_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_quality_doc_reviewers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_quality_doc_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_bulk_doc_exporters,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_bulk_doc_exporters,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_bulk_doc_exporters,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_bus_admins_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_bus_admins_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_bus_admins_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_readers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_readers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_doc_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_publisher_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_publisher_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_publisher_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_secrf_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_secrf_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_secrf_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_template_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_template_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_template_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_template_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_template_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_template_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_template_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_template_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_regulatory_template_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_submission_archivists_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_submission_archivists_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cd_submission_archivists_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cta_hub_approvers,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cta_hub_approvers,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cta_hub_authors,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cta_hub_authors,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cta_hub_readers,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cta_hub_readers,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cta_hub_reviewers,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_cta_hub_reviewers,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_flexreport_users_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_flexreport_users_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=ph_flexreport_users_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_bulk_doc_exporters,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_bulk_doc_exporters,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_bulk_doc_exporters,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_bulk_doc_exporters,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_bus_admins_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_bus_admins_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_bus_admins_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_bus_admins_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_authors_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_readers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_readers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_readers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_reviewers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_doc_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_secrf_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_secrf_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_secrf_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_secrf_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_authors_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_reviewers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_ad_promo_template_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_clinical_doc_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_clinical_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_clinical_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_clinical_doc_authors_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_clinical_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_clinical_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_clinical_doc_authors_tmf_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_clinical_doc_authors_tmf_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_clinical_doc_authors_tmf_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_bulk_doc_exporters,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_bulk_doc_exporters,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_bulk_doc_exporters,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_bulk_doc_exporters,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_bus_admins_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_bus_admins_int,ou=SubWay_ALL_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_bus_admins_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_bus_admins_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_authors_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_readers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_readers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_readers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_reviewers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_doc_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_secrf_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_secrf_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_secrf_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_secrf_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_template_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_template_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_template_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_template_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_template_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_template_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_template_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_corres_template_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_bulk_doc_exporters,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_bulk_doc_exporters,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_bulk_doc_exporters,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_bulk_doc_exporters,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_bus_admins_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_bus_admins_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_bus_admins_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_bus_admins_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_admins_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_admins_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_admins_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_authors_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_readers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_readers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_readers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_reviewers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_doc_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_secrf_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_secrf_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_secrf_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_secrf_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_authors_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_reviewers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_labeling_template_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_product_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_product_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_product_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_product_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_admins_regcmc_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_admins_regcmc_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_authors_qualdev_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_authors_qualdev_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_authors_qualdev_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_authors_regcmc_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_authors_regcmc_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_authors_regcmc_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_reviewers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_doc_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_project_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_quality_project_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_bulk_doc_exporters,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_bulk_doc_exporters,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_bulk_doc_exporters,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_bulk_doc_exporters,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_bus_admins_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_bus_admins_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_bus_admins_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_bus_admins_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_authors_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_readers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_readers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_readers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_readers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_reviewers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_doc_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_publisher_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_publisher_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_publisher_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_publisher_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_secrf_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_secrf_managers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_secrf_managers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_secrf_managers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_approvers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_authors_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_authors_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_reviewers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_reviewers_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_reviewers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_regulatory_template_reviewers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_safety_doc_approvers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_safety_doc_approvers_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_safety_doc_approvers_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_safety_doc_authors_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_safety_doc_authors_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_safety_doc_authors_int,ou=subway_all_tst,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_safety_project_managers_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_submission_archivists_int,ou=subway_all_dev,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_submission_archivists_int,ou=Subway_all_dmx,ou=applications,ou=intranet,dc=novartis,dc=com|cn=sz_cd_submission_archivists_int,ou=subway_all_pft,ou=applications,ou=intranet,dc=novartis,dc=com";
		String role[]=tmpStr.split("\\|");;
		for(int j=0;j<role.length;j++)
		{
			String str=role[j];

			DfLogger.debug(this, str, null, null);
		}

	}

	public void test() {
		//DqlRepeatingValuesL

		try {
			File file = new File("C:\\Users\\SHUBHKU1\\Downloads\\Subway user memberships.xlsx");
			//creating a new file instance  
			FileInputStream fis = new FileInputStream(file);
			//obtaining bytes from the file  
			//creating Workbook instance that refers to .xlsx file  
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheet("Sheet3");

			//creating a Sheet object to retrieve object  
			Iterator < Row > itr = sheet.iterator();
			//iterating over excel file

			ArrayList<String> user_names=new  ArrayList<String>();  //0th
			ArrayList<String> group_names=new  ArrayList<String>();   //1st

			while (itr.hasNext()) {
				Row row = itr.next();
				Iterator < Cell > cellIterator = row.cellIterator();
				//iterating over each column  


				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					if (cell.getColumnIndex()==0)
					{
						user_names.add( cell.getStringCellValue());
					}
					else  if (cell.getColumnIndex()==1)
					{
						group_names.add(cell.getStringCellValue());
					}
					// break;

				}        
			}

			int counter=0;
			if(user_names.size()==group_names.size())
			{
				HashMap<String, ArrayList<String> > users_data=new HashMap<String, ArrayList<String> >();

				System.out.println("user_names.size() [" +user_names.size()+"] group_names ["+group_names.size() +"]" );
				for(int i=0;i<user_names.size();i++)
				{
					System.out.println(counter);
					String user_name=user_names.get(i);
					//System.out.println(member_of_str);

					String roleStr=group_names.get(i);
					String roleTmpStr=group_names.get(i).substring(0,3);
					ArrayList<String> roleArr=new ArrayList<String>();

					if(!roleTmpStr.toLowerCase().trim().contains("cd_") && !roleTmpStr.toLowerCase().trim().contains("swy")
							&& !roleTmpStr.toLowerCase().trim().contains("ws_") && !roleTmpStr.toLowerCase().trim().contains("grp"))
					{

						if(users_data.containsKey(user_name))
						{
							roleArr=users_data.get(user_name);

							if(!roleArr.contains(roleStr))
							{
								roleArr.add(roleStr);
							}
						}
						else
						{
							roleArr.add(roleStr);
							users_data.put(user_name,roleArr);

						}

					}
					counter=counter+1;
				}
				writeSheet(users_data,file);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void writeSheet(HashMap<String, ArrayList<String> > data,File xlsfile) throws Exception
	{
		FileInputStream fis = new FileInputStream(xlsfile);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		//XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet spreadsheet = wb.createSheet("Result");

		Set<String> set= data.keySet();
		Iterator<String> iterator=set.iterator();

		XSSFRow row;
		int rowid = 0;

		  XSSFCellStyle cellStylem = wb.createCellStyle();//4
	     // cellStylem.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	      //cellStylem.setFillPattern(FillPatternType.SQUARES);
	        
		while(iterator.hasNext())
		{
			String key=iterator.next();
			ArrayList<String> grpList=data.get(key);
			int grpSize=grpList.size();

			String warnStr="";
			String grpsNamesStr="";

			for(int j=0;j<grpList.size();j++)
			{
				String tmpGrpStr=grpList.get(j);

				String tmpGrpStrPrefix=tmpGrpStr.substring(0,3);

				if(!warnStr.equalsIgnoreCase("warn"))
				{
					if(tmpGrpStrPrefix.toLowerCase().startsWith("sz_"))
					{
						warnStr="OK";
					}
					else
					{
						warnStr="WARN";
					}
				}

				if(j==0)
				{
					grpsNamesStr=tmpGrpStr;
				}
				else
				{
					grpsNamesStr=grpsNamesStr+"#"+tmpGrpStr;
				}

			}
			grpsNamesStr=warnStr +"#"+grpsNamesStr;

			row=spreadsheet.createRow(rowid++);
			
			Cell cell = row.createCell(0);
			cell.setCellValue(key);
			
			
			cell = row.createCell(1);
			cell.setCellValue(grpsNamesStr);
		
			
			if(!warnStr.equalsIgnoreCase("warn"))
			{
				//System.out.println("Hiii");
				row.setRowStyle(cellStylem);
						
			}
		}
		fis.close();

		FileOutputStream out = new FileOutputStream(xlsfile);
		wb.write(out);
		wb.close();
		out.close();
	}
}