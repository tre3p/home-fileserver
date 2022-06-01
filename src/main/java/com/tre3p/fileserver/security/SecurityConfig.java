package com.tre3p.fileserver.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Value("${security.username}")
    private String username;

    @Value("${security.password}")
    private String password;

    @Override
    protected final void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
          .requestCache().requestCache(new CustomRequestCache())
          .and().authorizeRequests()
          .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
          .anyRequest().authenticated()
          .and().formLogin()
          .loginPage(LOGIN_URL).permitAll()
          .loginProcessingUrl(LOGIN_PROCESSING_URL)
          .failureUrl(LOGIN_FAILURE_URL)
          .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }


    /**
     * Used to authenticate user with his credentials.
     * @return UserDetailsService with user credentials and default role.
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
      UserDetails user = User.withUsername(username)
              .password("{noop}" + password)
              .roles("USER")
              .build();

      return new InMemoryUserDetailsManager(user);
    }

    @Override
    public final void configure(WebSecurity web) {
      web.ignoring().antMatchers(
          "/VAADIN/**",
          "/favicon.ico",
          "/robots.txt",
          "/manifest.webmanifest",
          "/sw.js",
          "/offline.html",
          "/icons/**",
          "/images/**",
          "/styles/**");
    }
}
