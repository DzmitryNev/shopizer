package com.salesmanager.shop.store.controller.store.facade;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import com.salemanager.shop.exception.ResourceNotFoundException;
import org.apache.commons.lang.Validate;
import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.currency.CurrencyService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.reference.zone.ZoneService;
import com.salesmanager.core.constants.MeasureUnit;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.shop.PersistableMerchantStore;
import com.salesmanager.shop.model.shop.ReadableMerchantStore;
import com.salesmanager.shop.populator.store.PersistableMerchantStorePopulator;
import com.salesmanager.shop.populator.store.ReadableMerchantStorePopulator;
import com.salesmanager.shop.utils.ImageFilePath;

@Service("storeFacade")
public class StoreFacadeImpl implements StoreFacade {
	
	@Inject
	private MerchantStoreService merchantStoreService;
	
	@Inject
	private LanguageService languageService;
	
	@Inject
	private CountryService countryService;
	
	@Inject
	private ZoneService zoneService;
	
	@Inject
	private CurrencyService currencyService;
	
	@Inject
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@Override
	public MerchantStore getByCode(HttpServletRequest request) throws Exception {
		String code = request.getParameter("store");
		if(StringUtils.isEmpty(code)) {
			code = com.salesmanager.core.business.constants.Constants.DEFAULT_STORE;
		} 
		return get(code);
	}

	@Override
	public MerchantStore get(String code) throws Exception {
		return Optional.ofNullable(merchantStoreService.getByCode(code))
				.orElseThrow(() -> new ResourceNotFoundException("MerchanStore not found for merchant store [" + code + "]"));
	}

	@Override
	public ReadableMerchantStore getByCode(String code, Language language) throws Exception {
		
		MerchantStore store = get(code);

        ReadableMerchantStore readable = new ReadableMerchantStore();

		ReadableMerchantStorePopulator populator = new ReadableMerchantStorePopulator();
		populator.setCountryService(countryService);
		populator.setZoneService(zoneService);
		populator.setFilePath(imageUtils);

		/**
		 * Language is not important for this conversion
		 * using default language
		 */
		readable = populator.populate(store, readable, store, language);

		return readable;
	}

	@Override
	public void create(PersistableMerchantStore store) throws Exception {
		
		Validate.notNull(store,"PersistableMerchantStore must not be null");
		Validate.notNull(store.getCode(),"PersistableMerchantStore.code must not be null");
		
		
		MerchantStore mStore = new MerchantStore();
	
		PersistableMerchantStorePopulator populator = new PersistableMerchantStorePopulator();
		populator.setCountryService(countryService);
		populator.setZoneService(zoneService);
		populator.setLanguageService(languageService);
		populator.setCurrencyService(currencyService);
		

		//set default values
		mStore.setWeightunitcode(MeasureUnit.KG.name());
		mStore.setSeizeunitcode(MeasureUnit.IN.name());
		
		mStore = populator.populate(store, mStore, languageService.defaultLanguage());
		
		merchantStoreService.create(mStore);
		
	}

	@Override
	public void update(PersistableMerchantStore store) throws Exception {
		MerchantStore mStore = merchantStoreService.getByCode(store.getCode());
		
		if(mStore == null) {
			throw new Exception("Store with code " + store.getCode() + " does not exists");
		}
		
		PersistableMerchantStorePopulator populator = new PersistableMerchantStorePopulator();
		populator.setCountryService(countryService);
		populator.setZoneService(zoneService);
		populator.setLanguageService(languageService);
		populator.setCurrencyService(currencyService);
		
		store.setId(mStore.getId());

		mStore = populator.populate(store, mStore, languageService.defaultLanguage());
		
		merchantStoreService.update(mStore);
		
	}

}
