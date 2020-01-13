/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package  de.hybris.platform.ybelsportinitialdata.dataimport.impl;

import de.hybris.platform.commerceservices.dataimport.AbstractDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.validation.services.ValidationService;

import java.io.InputStream;
import java.util.List;


/**
 * Extension of {@link AbstractDataImportService} that defines services related to core data.
 */
public class CoreDataImportService extends AbstractDataImportService
{
	public static final String IMPORT_CORE_DATA = "importCoreData";

	@Override
	public void execute(final AbstractSystemSetup systemSetup, final SystemSetupContext context, final List<ImportData> importData)
	{
		final boolean importCoreData = systemSetup.getBooleanSystemSetupParameter(context, IMPORT_CORE_DATA);

		if (importCoreData)
		{
			for (final ImportData data : importData)
			{
				importAllData(systemSetup, context, data, false);
			}

			final ValidationService validation = getBeanForName("validationService");
			validation.reloadValidationEngine();
		}
	}

	@Override
	protected void importCommonData(final String extensionName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/essential-data.impex", extensionName),
				true);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/countries.impex", extensionName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/regions.impex", extensionName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/regions_communes_localities.impex", extensionName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/delivery-modes.impex", extensionName),
				false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/themes.impex", extensionName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/user-groups.impex", extensionName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/refine_enum.impex",extensionName), false);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/mediaconversion_formats.impex",extensionName), true);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/RemoveOldStoredHttpSessionsCronjobSetup.impex",extensionName), true);
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/GenerateCSVExportToCourierCronJobSetup.impex",extensionName), true);
	}

	@Override
	protected void importProductCatalog(final String extensionName, final String productCatalogName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/productCatalogs/%sProductCatalog/catalog.impex",
				extensionName, productCatalogName), false);

		// Load Categories
		getSetupImpexService().importImpexFile(String.format(
				"/%s/import/coredata/productCatalogs/%sProductCatalog/categories.impex", extensionName, productCatalogName), false);
	}

	@Override
	protected void importContentCatalog(final String extensionName, final String contentCatalogName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/catalog.impex",
				extensionName, contentCatalogName), false);

		if (isResponsive())
		{
			final String responsiveContentFile = String.format(
					"/%s/import/coredata/contentCatalogs/%sContentCatalog/cms-responsive-content.impex", extensionName,
					contentCatalogName);
			if (getInputStream(responsiveContentFile) != null)
			{
				getSetupImpexService().importImpexFile(responsiveContentFile, false);
			}
			else
			{
				getSetupImpexService()
						.importImpexFile(String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/cms-content.impex",
								extensionName, contentCatalogName), false);
			}
		}
		else
		{
			getSetupImpexService()
					.importImpexFile(String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/cms-content.impex",
							extensionName, contentCatalogName), false);

			if (getConfigurationService().getConfiguration().getBoolean(IMPORT_MOBILE_DATA, false))
			{
				getSetupImpexService()
						.importImpexFile(String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/cms-mobile-content.impex",
								extensionName, contentCatalogName), false);
			}
		}

		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/email-content.impex",
						extensionName, contentCatalogName), false);
	}

	@Override
	protected void importStore(final String extensionName, final String storeName, final String productCatalogName)
	{
		final String responsiveStoreFile = String.format("/%s/import/coredata/stores/%s/store-responsive.impex", extensionName,
				storeName);
		final String responsiveSiteFile = String.format("/%s/import/coredata/stores/%s/site-responsive.impex", extensionName,
				storeName);

		if (isResponsive() && getInputStream(responsiveStoreFile) != null)
		{
			getSetupImpexService().importImpexFile(responsiveStoreFile, true);
		}
		else
		{
			getSetupImpexService()
					.importImpexFile(String.format("/%s/import/coredata/stores/%s/store.impex", extensionName, storeName), true);
		}

		if (isResponsive() && getInputStream(responsiveSiteFile) != null)
		{
			getSetupImpexService().importImpexFile(responsiveSiteFile, true);
		}
		else
		{
			getSetupImpexService()
					.importImpexFile(String.format("/%s/import/coredata/stores/%s/site.impex", extensionName, storeName), true);
		}

		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/coredata/stores/%s/warehouses.impex", extensionName, storeName), true);
	}

	@Override
	protected void importSolrIndex(final String extensionName, final String storeName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/stores/%s/solr.impex", extensionName, storeName),
				false);

		getSetupSolrIndexerService().createSolrIndexerCronJobs(String.format("%sIndex", storeName));

		getSetupImpexService()
				.importImpexFile(String.format("/%s/import/coredata/stores/%s/solrtrigger.impex", extensionName, storeName), false);
	}

	@Override
	protected void importJobs(final String extensionName, final String storeName)
	{
		getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/stores/%s/jobs.impex", extensionName, storeName),
				false);
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
