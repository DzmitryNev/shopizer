package com.salesmanager.shop.converter.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.zone.ZoneService;
import com.salesmanager.core.constants.MeasureUnit;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.reference.zone.Zone;
import com.salesmanager.shop.converter.Converter;
import com.salesmanager.shop.model.content.ReadableImage;
import com.salesmanager.shop.model.references.ReadableAddress;
import com.salesmanager.shop.model.shop.ReadableMerchantStore;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.ImageFilePath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class ReadableMerchantConverter implements Converter<MerchantStore, ReadableMerchantStore> {

    @Autowired
    private CountryService countryService;
    @Autowired
    private ZoneService zoneService;
//    @Autowired
//    private ImageFilePath filePath;

    @Override
    public ReadableMerchantStore convert(MerchantStore source, MerchantStore store, Language language) {
        Validate.notNull(countryService, "Must use setter for countryService");
        Validate.notNull(zoneService, "Must use setter for zoneService");

        ReadableMerchantStore target = new ReadableMerchantStore();

        target.setId(source.getId());
        target.setCode(source.getCode());

        Optional.ofNullable(source.getDefaultLanguage())
                .map(Language::getCode)
                .ifPresent(code -> target.setDefaultLanguage(code));

        target.setCurrency(source.getCurrency().getCode());
        target.setPhone(source.getStorephone());

        ReadableAddress address = createReadableAddress(source, language);
        target.setAddress(address);

        target.setDimension(MeasureUnit.valueOf(source.getSeizeunitcode()));
        target.setWeight(MeasureUnit.valueOf(source.getWeightunitcode()));

        Optional<ReadableImage> image = createReadableImage(source.getStoreLogo());
        image.ifPresent(i -> target.setLogo(i));

        target.setCurrencyFormatNational(source.isCurrencyFormatNational());
        target.setEmail(source.getStoreEmailAddress());
        target.setName(source.getStorename());
        target.setId(source.getId());
        target.setInBusinessSince(DateUtil.formatDate(source.getInBusinessSince()));
        target.setUseCache(source.isUseCache());

        List<String> supportedLanguages = createSupportedLanguages(source.getLanguages());
        target.setSupportedLanguages(supportedLanguages);

        return target;
    }

    private List<String> createSupportedLanguages(List<Language> languageList) {
        return Optional.ofNullable(languageList)
                .filter(languages -> !CollectionUtils.isEmpty(languages))
                .map(languages ->
                        languages.stream()
                                .map(Language::getCode)
                                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private Optional<ReadableImage> createReadableImage(String storeLogo) {
        return Optional.ofNullable(storeLogo)
                .filter(item -> !StringUtils.isBlank(item))
                .map(item ->  {
                    ReadableImage image = new ReadableImage();
                    image.setName(item);
                    //TODO change
                    //            if (filePath != null) {
                    //                image.setPath(filePath.buildStoreLogoFilePath(source));
                    //            }
                    return image;
                });
    }

    private ReadableAddress createReadableAddress(MerchantStore source, Language language) {
        ReadableAddress address = new ReadableAddress();
        address.setAddress(source.getStoreaddress());
        address.setCity(source.getStorecity());
        if (source.getCountry() != null) {
            try {
                address.setCountry(source.getCountry().getIsoCode());
                Country c = countryService.getCountriesMap(language).get(source.getCountry().getIsoCode());
                if (c != null) {
                    address.setCountry(c.getIsoCode());
                }
            } catch (ServiceException e) {
                log.error("Cannot get Country", e);
            }
        }

        if (source.getZone() != null) {
            address.setStateProvince(source.getZone().getCode());
            try {
                Zone zone = zoneService.getZones(language).get(source.getZone().getCode());
                address.setStateProvince(zone.getCode());
            } catch (ServiceException e) {
                log.error("Cannot get Zone", e);
            }
        }

        if (!StringUtils.isBlank(source.getStorestateprovince())) {
            address.setStateProvince(source.getStorestateprovince());
        }

        address.setPostalCode(source.getStorepostalcode());

        return address;
    }
}
