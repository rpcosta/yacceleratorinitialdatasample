/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ybelsportinitialdata.dataimport.impl;

import de.hybris.platform.commerceservices.dataimport.AbstractDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.core.initialization.SystemSetupContext;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.hybris.platform.ybelsportinitialdata.constants.InitialDataConstants.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Extension of {@link AbstractDataImportService} that defines services related to core data.
 */
public class UpdateDataImportService extends AbstractDataImportService
{
	private static Logger LOG = LoggerFactory.getLogger(UpdateDataImportService.class);

	@Override
	public void execute(final AbstractSystemSetup systemSetup, final SystemSetupContext context, final List<ImportData> importData)
	{

		final List<String> listSprintDirectories = createSprintDirectoryList(SAP_CX_UPDATE_BASE_FOLDER);

		if(CollectionUtils.isNotEmpty(listSprintDirectories) ) {
			for(final String currentDirectory : listSprintDirectories){
				final String parameterName = String.format("sapcx%s",currentDirectory);
				final boolean parameterEnabled = Boolean.valueOf(context.getParameter(parameterName));
				LOG.info("Parameter {} set as {}", parameterName, (parameterEnabled)? "TRUE. Preparing to import!": "FALSE. Skipping!");
				if (parameterEnabled) {
					final String folderName = SAP_CX_UPDATE_BASE_FOLDER +"/"+ currentDirectory;
					LOG.info("Importing {}...", folderName);
					imporImpexFolder(context, folderName);
					LOG.info("{} importing done!", folderName);
				}
			}
		}
	}

	/**
	 * List update directories
	 *
	 * @param folderName
	 * @return
	 */
	private List<String> createSprintDirectoryList(final String folderName){
		final File[] files = new File(this.getClass().getResource(folderName).getFile()).listFiles();
		List<String> sprintDirectoryList = null;

		if (files != null && files.length > 0)
		{
			sprintDirectoryList = new ArrayList<>(files.length);

			Arrays.sort(files);

			for (final File file : files){
				if (file.isDirectory())
				{
					sprintDirectoryList.add(file.getName());
				}
			}
		}

		return sprintDirectoryList;
	}

	/**
	 * Load impex by folder recursively
	 * @param context
	 * @param folderName
	 */
	private void imporImpexFolder(final SystemSetupContext context, final String folderName) {
		final File[] files = new File(this.getClass().getResource(folderName).getFile()).listFiles();
		if (files != null && files.length > 0) {
			Arrays.sort(files);
			for (final File file : files) {
				final String fileName = folderName + "/" + file.getName();
				if (file.isDirectory()) {
					LOG.info("...Folder " + file.getName());
					imporImpexFolder(context, fileName);
				} else {
					if (file.getName().endsWith(IMPEX_SUFFIX)) {
						LOG.info("......File " + file.getName());
						getSetupImpexService().importImpexFile(fileName, true);
					}
				}
			}
		}
	}

	@Override
	protected void importCommonData(String extensionName) {
		//Nothing to be done!
	}

	@Override
	protected void importProductCatalog(String extensionName, String productCatalogName) {
		//Nothing to be done!
	}

	@Override
	protected void importContentCatalog(String extensionName, String contentCatalogName) {
		//Nothing to be done!
	}

	@Override
	protected void importStore(String extensionName, String storeName, String productCatalogName) {
		//Nothing to be done!
	}

	@Override
	protected void importSolrIndex(String extensionName, String storeName) {
		//Nothing to be done!
	}

	@Override
	protected void importJobs(String extensionName, String storeName) {
		//Nothing to be done!
	}

	protected InputStream getInputStream(final String fileName)
	{
		return getClass().getResourceAsStream(fileName);
	}

	protected boolean isResponsive()
	{
		return ResponsiveUtils.isResponsive();
	}
}
