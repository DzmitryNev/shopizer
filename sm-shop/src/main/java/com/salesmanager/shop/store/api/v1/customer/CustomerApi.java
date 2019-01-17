package com.salesmanager.shop.store.api.v1.customer;

import static com.salesmanager.core.business.constants.Constants.DEFAULT_STORE;

import com.salesmanager.shop.store.api.exception.ResourceNotFoundException;
import com.salesmanager.shop.store.api.exception.UnauthorizedException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.CustomerCriteria;
import com.salesmanager.core.model.customer.CustomerList;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.customer.PersistableCustomer;
import com.salesmanager.shop.model.customer.ReadableCustomer;
import com.salesmanager.shop.populator.customer.ReadableCustomerList;
import com.salesmanager.shop.populator.customer.ReadableCustomerPopulator;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.controller.store.facade.StoreFacade;
import com.salesmanager.shop.utils.LanguageUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CustomerApi {

  @Inject
  private CustomerFacade customerFacade;

  @Inject
  private StoreFacade storeFacade;

  @Inject
  private LanguageUtils languageUtils;

  /** Create new customer for a given MerchantStore */
  @PostMapping("/private/customers")
  @ApiOperation(
      httpMethod = "POST",
      value = "Creates a customer",
      notes = "Requires administration access",
      produces = "application/json",
      response = PersistableCustomer.class)
  public PersistableCustomer create(
      @Valid PersistableCustomer customer,
      @RequestParam(name = "store", defaultValue = DEFAULT_STORE) String storeCode) {

    MerchantStore merchantStore = storeFacade.get(storeCode);
    return customerFacade.create(customer, merchantStore);
  }

  @PutMapping("/private/customers/{username}")
  @ApiOperation(
      httpMethod = "PUT",
      value = "Updates a customer",
      notes = "Requires administration access",
      produces = "application/json",
      response = PersistableCustomer.class)
  public PersistableCustomer update(
      @PathVariable String userName,
      @Valid PersistableCustomer customer,
      @RequestParam(name = "store", defaultValue = DEFAULT_STORE) String storeCode,
      HttpServletResponse response) {

    MerchantStore merchantStore = storeFacade.get(storeCode);
    // TODO customer.setUserName
    // TODO more validation
    return customerFacade.update(customer, merchantStore);
  }

  @DeleteMapping("/private/customers/{userName}")
  @ApiOperation(
      httpMethod = "DELETE",
      value = "Deletes a customer",
      notes = "Requires administration access",
      response = Void.class)
  public void delete(@PathVariable String userName) {
    customerFacade.deleteByNick(userName);
  }

  /**
   * Get all customers
   *
   * @param start
   * @param count
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @GetMapping("/private/customers")
  public ReadableCustomerList getFilteredCustomers(
      @RequestParam(value = "start", required = false) Integer start,
      @RequestParam(value = "count", required = false) Integer count,
      @RequestParam(name = "store", defaultValue = DEFAULT_STORE) String storeCode,
      HttpServletRequest request) {

    MerchantStore merchantStore = storeFacade.get(storeCode);
    Language language = languageUtils.getRESTLanguage(request, merchantStore);
    return customerFacade.listByStore(merchantStore, language, start, count);
  }

  @GetMapping("/private/customers/{userName}")
  public ReadableCustomer get(
      @PathVariable String userName,
      @RequestParam(name = "store", defaultValue = DEFAULT_STORE) String storeCode,
      HttpServletRequest request) {

    MerchantStore merchantStore = storeFacade.get(storeCode);
    Language language = languageUtils.getRESTLanguage(request, merchantStore);
    return customerFacade.getByUserName(userName, merchantStore, language);
  }

  @GetMapping("/auth/customers/profile")
  public ReadableCustomer get(
      HttpServletRequest request,
      @RequestParam(name = "store", defaultValue = DEFAULT_STORE) String storeCode) {

    MerchantStore merchantStore = storeFacade.get(storeCode);
    Language language = languageUtils.getRESTLanguage(request, merchantStore);

    Principal principal = request.getUserPrincipal();
    String userName = principal.getName();

    return customerFacade.getCustomerByUserName(userName, merchantStore, language);
  }
}
