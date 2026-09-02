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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2PictureFetcher oAuth2PictureFetcher;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("Attributes: {}", attributes.toString());
        String userName = attributes.get("login").toString();
        if (!StringUtils.isNoneBlank(userName)) {
            throw new OAuth2AuthenticationException("Username is invalid");
        }
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            user = createGithubUser(attributes);
        }
        GrantedAuthority grandAuthority = new SimpleGrantedAuthority(user.getUserRole());
        return new DefaultOAuth2User(Collections.singleton(grandAuthority), attributes, "login");
    }

    public User createGithubUser(Map<String, Object> attributes) {
        String userName = attributes.get("login").toString();
        String githubAccountId = attributes.get("id").toString();
        String fullName = attributes.get("name").toString();
        User user = new User();
        user.setUserName(userName);
        user.setActive(true);
        user.setUserRole(SystemConstant.USER_ROLE);
        user.setFullName(fullName);
        user.setGithubAccountId(githubAccountId);
        user.setEncrytedPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        byte[] profileImage = oAuth2PictureFetcher.fetchGoogleProfilePicture(attributes.get("avatar_url").toString());
        user.setImage(profileImage);
        return userRepository.save(user);
    }
}
