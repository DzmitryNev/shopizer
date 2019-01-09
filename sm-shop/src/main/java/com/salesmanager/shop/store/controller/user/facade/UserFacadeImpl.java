package com.salesmanager.shop.store.controller.user.facade;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.salemanager.shop.exception.ResourceNotFoundException;
import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.user.PermissionService;
import com.salesmanager.core.business.services.user.UserService;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.user.Group;
import com.salesmanager.core.model.user.Permission;
import com.salesmanager.core.model.user.User;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.model.security.ReadableGroup;
import com.salesmanager.shop.model.security.ReadablePermission;
import com.salesmanager.shop.model.user.ReadableUser;
import com.salesmanager.shop.populator.user.Converter;
import org.springframework.stereotype.Service;

@Service("userFacade")
public class UserFacadeImpl implements UserFacade {

    @Inject
    private UserService userService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private LanguageService languageService;

    @Inject
    private Converter<User, ReadableUser> readableUserConverter;

    @Override
    public ReadableUser findByUserName(String userName, Language lang) throws Exception {

        User user = Optional.ofNullable(userService.getByUserName(userName))
                .orElseThrow(() -> new ResourceNotFoundException("No User found for name : " + userName));

        ReadableUser readableUser = createReadableUser(lang, user);

        List<Integer> groupIds = getGroupIds(user);

        // Add permissions on top of the groups
        List<ReadablePermission> permissions = findPermissionsByGroups(groupIds);
        readableUser.setPermissions(permissions);

        return readableUser;
    }

    private List<Integer> getGroupIds(User user) {
        return user.getGroups()
                    .stream()
                    .map(Group::getId)
                    .collect(Collectors.toList());
    }

    private ReadableUser createReadableUser(Language lang, User user) throws ConversionException {
        return readableUserConverter.convert(user, user.getMerchantStore(), lang);
    }

    @Override
    public List<ReadablePermission> findPermissionsByGroups(List<Integer> userIds) throws Exception {
        return permissionService.getPermissions(userIds)
                .stream()
                .map(permission -> convertToReadablePermission(permission))
                .collect(Collectors.toList());
    }

    private ReadablePermission convertToReadablePermission(Permission permission) {
        ReadablePermission readablePermission = new ReadablePermission();
        readablePermission.setId(Long.valueOf(permission.getId()));
        readablePermission.setName(permission.getPermissionName());
        return readablePermission;
    }

    // TODO should changed to user roles
    @Override
    public boolean authorizedStore(String userName, String merchantStoreCode) throws Exception {

        ReadableUser readableUser = findByUserName(userName, languageService.defaultLanguage());

        //unless superadmin
        boolean isSuperAdmin = readableUser.getGroups()
                .stream()
                .map(ReadableGroup::getName)
                .anyMatch(Constants.GROUP_SUPERADMIN::equals);

        if (isSuperAdmin) {
            return true;
        } else {
            User user = userService.findByStore(readableUser.getId(), merchantStoreCode);
            return isUserAuthorized(user);
        }
    }

    private boolean isUserAuthorized(User user) {
        return user != null;
    }
}
