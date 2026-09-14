package com.devon.building.config;


import com.devon.building.filters.JwtTokenFilter;
import com.devon.building.security.CustomSuccessHandler;
import com.devon.building.service.impl.CustomOAuth2UserService;
import com.devon.building.service.impl.CustomOidcUserService;
import com.devon.building.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOidcUserService oidcUserService, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/users/list").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("MANAGER")
                        .requestMatchers((HttpMethod.DELETE), "/api/buildings/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/admin/register").permitAll()
                        .requestMatchers("/admin/assets/**", "/admin/assets/js/**", "/admin/assets/css/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("STAFF", "MANAGER")
                        .requestMatchers(HttpMethod.PUT,"/web/customers").hasAnyRole("MANAGER","STAFF")
                        .requestMatchers(HttpMethod.POST,"/web/customers/assign").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE,"/web/customers/**").hasRole("MANAGER")
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"))
                .formLogin(form -> form
                                .loginPage("/admin/login")
                                .loginProcessingUrl("/j_spring_security_check")
                                .successHandler(myAuthenticationSuccessHandler())
//                        .defaultSuccessUrl("/admin/accountInfo", true)
                                .failureUrl("/admin/login?incorrectAccount")
                                .usernameParameter("userName")
                                .passwordParameter("password")
                                .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/admin/login")
                        .successHandler(myAuthenticationSuccessHandler())

                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                                .oidcUserService(oidcUserService))
                        .failureUrl("/admin/login?incorrectAccount")
                        .permitAll()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }
}
