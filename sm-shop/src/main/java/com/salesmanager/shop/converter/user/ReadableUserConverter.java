package com.salesmanager.shop.converter.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.user.Group;
import com.salesmanager.shop.converter.Converter;
import com.salesmanager.shop.model.security.ReadableGroup;
import com.salesmanager.shop.model.user.ReadableUser;
import com.salesmanager.core.model.user.User;
import com.salesmanager.shop.utils.DateUtil;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

@Component
public class ReadableUserConverter implements Converter<User, ReadableUser> {

    @Override
    public ReadableUser convert(User source, MerchantStore store, Language language) {
        Validate.notNull(source, "User cannot be null");

        ReadableUser target = new ReadableUser();

        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setEmailAddress(source.getAdminEmail());
        target.setUserName(source.getAdminName());

        if (source.getLastAccess() != null) {
            target.setLastAccess(DateUtil.formatLongDate(source.getLastAccess()));
        }

        String defaultLanguage = getDefaultLanguage(source);
        target.setDefaultLanguage(defaultLanguage);

        target.setMerchant(source.getMerchantStore().getCode());
        target.setId(source.getId());

        List<ReadableGroup> groupTOs = convertGroups(source);
        target.getGroups().addAll(groupTOs);

        return target;
    }

    private List<ReadableGroup> convertGroups(User source) {
        return source.getGroups()
                .stream()
                .map(group -> createReadableGroup(group))
                .collect(Collectors.toList());
    }

    private String getDefaultLanguage(User source) {
        return Optional.ofNullable(source.getDefaultLanguage())
                .map(Language::getCode)
                .orElse(Constants.DEFAULT_LANGUAGE);
    }

    private ReadableGroup createReadableGroup(Group group) {
        ReadableGroup readableGroup = new ReadableGroup();
        readableGroup.setId(new Long(group.getId()));
        readableGroup.setName(group.getGroupName());
        return readableGroup;
    }
}
