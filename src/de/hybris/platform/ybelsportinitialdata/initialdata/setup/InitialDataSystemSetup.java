/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ybelsportinitialdata.initialdata.setup;

import de.hybris.platform.ybelsportinitialdata.constants.InitialDataConstants;
import de.hybris.platform.ybelsportinitialdata.initialdata.dataimport.impl.CoreDataImportService;
import de.hybris.platform.ybelsportinitialdata.initialdata.dataimport.impl.SampleDataImportService;
import de.hybris.platform.ybelsportinitialdata.initialdata.dataimport.impl.UpdateDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.ybelsportinitialdata.initialdata.constants.InitialDataConstants.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@SystemSetup(extension = InitialDataConstants.EXTENSIONNAME)
public class InitialDataSystemSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(InitialDataSystemSetup.class);

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;
	private UpdateDataImportService updateDataImportService;

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));
		params.add(createBooleanSystemSetupParameter(SYNCHRONIZE_CONTENT_CATALOG, "Synchronize Content Catalog", true));
		params.add(createBooleanSystemSetupParameter(SYNCHRONIZE_PRODUCT_CATALOG, "Synchronize Product Catalog", true));
		// Add more Parameters here as you require

		final List<String> listUpdateDirectories = createSprintDirectoryList(SAP_CX_UPDATE_BASE_FOLDER);

		if(CollectionUtils.isNotEmpty(listUpdateDirectories) ) {
			for(final String currentDirectory : listUpdateDirectories){
				params.add(createBooleanSystemSetupParameter("sapcx" + currentDirectory, currentDirectory + " data", false));
			}
		}

		return params;
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during
	 * initialization and system update. Be sure that this method can be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();

		final ImportData storeImportData = new ImportData();
		storeImportData.setProductCatalogName(CATALOG_PREFIX);
		storeImportData.setContentCatalogNames(Arrays.asList(CATALOG_PREFIX));
		storeImportData.setStoreNames(Arrays.asList(STORE_UID));
		importData.add(storeImportData);

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));

		getSampleDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

		getUpdateDataImportService().execute(this, context, importData);

	}

	public List<String> createSprintDirectoryList(final String folderName){
		URL folderResorce = this.getClass().getResource(folderName);
		if (folderResorce != null) {
			File updateDataFolder = new File(folderResorce.getFile());
			final File[] files = updateDataFolder.listFiles();
			List<String> sprintDirectoryList = null;

			if (files != null && files.length > 0) {
				sprintDirectoryList = new ArrayList<>(files.length);

				Arrays.sort(files);

				for (final File file : files) {
					if (file.isDirectory()) {
						sprintDirectoryList.add(file.getName());
					}
				}
			}

			return sprintDirectoryList;
		} else {
			LOG.error("Folder {} not found! No update data will be loaded.", folderName);
			return Collections.emptyList();
		}
	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}

	public UpdateDataImportService getUpdateDataImportService() {
		return updateDataImportService;
	}

	@Required
	public void setUpdateDataImportService(final UpdateDataImportService updateDataImportService) {
		this.updateDataImportService = updateDataImportService;
	}
}
