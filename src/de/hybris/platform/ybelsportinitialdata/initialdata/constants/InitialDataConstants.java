/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ybelsportinitialdata.initialdata.constants;

/**
 * Global class for all Quicksilverb2cstoreinitialdata constants.
 */
public final class InitialDataConstants
{
	public static final String EXTENSIONNAME = "ybelsportinitialdata";
	public static final String STORE_UID = "__STORE_UID__";
	public static final String CATALOG_PREFIX = "__CATALOG_PREFIX__";

	public static final String IMPORT_CORE_DATA = "importCoreData";
	public static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	public static final String SYNCHRONIZE_CONTENT_CATALOG = "synchronizeContentCatalog";
	public static final String SYNCHRONIZE_PRODUCT_CATALOG = "synchronizeProductCatalog";

	public static final String IMPORT_SAMPLE_DATA = "importSampleData";
	public static final String SAMPLE_DATA_LOAD = "sampledata.load";
	public static final String CUSTOMER_SUPPORT_BACKOFFICE_EXTENSION_NAME = "customersupportbackoffice";
	public static final String ORDER_MANAGEMENT_BACKOFFICE_EXTENSION_NAME = "ordermanagementbackoffice";
	public static final String ASSISTED_SERVICE_EXTENSION_NAME = "assistedservicestorefront";

	public static final String IMPORT_UPDATE_DATA = "importUpdateData";
	public static final String SAP_CX_IMPORT_ROOT = "/" + EXTENSIONNAME + "/import";
	public static final String SAP_CX_UPDATE_BASE_FOLDER = SAP_CX_IMPORT_ROOT + "/updatedata/cx";
	public static final String IMPEX_SUFFIX = ".impex";

	private InitialDataConstants()
	{
		//empty
	}
}
