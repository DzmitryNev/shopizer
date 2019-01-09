package com.salesmanager.shop.store.api.v1.user;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.user.ReadableUser;
import com.salesmanager.shop.store.api.exception.RestApiException;
import com.salesmanager.shop.store.controller.store.facade.StoreFacade;
import com.salesmanager.shop.store.controller.user.facade.UserFacade;
import com.salesmanager.shop.utils.LanguageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.salesmanager.core.business.constants.Constants.DEFAULT_STORE;

/**
 * Api for managing admin users
 * @author carlsamson
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserApi {
	
	@Inject
	private StoreFacade storeFacade;
	
	@Inject
	private UserFacade userFacade;
	
	@Inject
	private LanguageUtils languageUtils;

	@GetMapping("/private/users/{name}")
	public ReadableUser get(@PathVariable(name = "name") String userName,
                            @RequestParam(name = "store", defaultValue = DEFAULT_STORE) String storeCode,
                            HttpServletRequest request) {

		try {
			MerchantStore merchantStore = storeFacade.getByCode(storeCode);
			Language language = languageUtils.getRESTLanguage(request, merchantStore);

			ReadableUser user = userFacade.findByUserName(userName, language);
			return user;
		} catch (Exception e) {
		    throw new RestApiException("Error while getting user " + e.getMessage());
		}
	}

}
