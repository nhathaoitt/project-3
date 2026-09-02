package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.repository.UserRepository;
import com.devon.building.utils.OAuth2PictureFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOidcUserService extends OidcUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2PictureFetcher oAuth2PictureFetcher;
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        log.info(oidcUser.getClaims().toString());
        String email = oidcUser.getEmail();
        if (!StringUtils.isNoneBlank(email)) {
            throw new OAuth2AuthenticationException("Email is invalid");
        }
        User user = userRepository.findByUserName(email);
        if(user == null){
            user = createGoogleUser(oidcUser);
        }
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getUserRole());
        return new DefaultOidcUser(Collections.singleton(grantedAuthority), oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
    }
    public User createGoogleUser(OidcUser oidcUser){
        User user = new User();
        user.setUserName(oidcUser.getEmail());
        user.setActive(true);
        user.setUserRole(SystemConstant.USER_ROLE);
        user.setFullName(oidcUser.getFullName());
        user.setGoogleAccountId(oidcUser.getAttribute("sub"));
        user.setEncrytedPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        byte[] profileImage = oAuth2PictureFetcher.fetchGoogleProfilePicture(oidcUser.getAttribute("picture"));
        user.setImage(profileImage);
        return userRepository.save(user);
    }
}
