package com.educative.config;

import com.educative.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {



    @Bean
    public LoginAttemptService loginAttemptService() {
        return new LoginAttemptServiceImpl();
    }


    @Bean
    public UserAccountService userAccountService() {
        return new UserAccountServiceImpl();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        DefaultJHipUserDetailService jHipUserDetailService = new DefaultJHipUserDetailService();
        jHipUserDetailService.setUserAccountService(userAccountService());
        jHipUserDetailService.setLoginAttemptService(loginAttemptService());
        return jHipUserDetailService;
    }

    @Bean
    public JHipAuthenticationProvider jHipAuthenticationProvider() {
        JHipAuthenticationProvider jHipAuthenticationProvider = new JHipAuthenticationProvider();
        jHipAuthenticationProvider.setUserDetailsService(userDetailsService());
        return jHipAuthenticationProvider;
    }


    @Bean
    public DefaultAuthenticationSuccessHandler authenticationSuccessHandler(UserAccountService userAccountService) {
        DefaultAuthenticationSuccessHandler handler = new DefaultAuthenticationSuccessHandler();
        handler.setLoginAttemptService(loginAttemptService());
        return handler;
    }

    @Bean
    public DefaultAuthenticationFailureHandler authenticationFailureHandler(UserAccountService userAccountService) {
        DefaultAuthenticationFailureHandler handler = new DefaultAuthenticationFailureHandler();
        return handler;
    }

    @Bean
    public JWTTokenService jwtTokenService() {
        JWTKeyClient client = new JWTKeyClient();
        return new JWTTokenServiceImpl(client);
    }


    public static class FormLoginSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{



        @Autowired
        private JHipAuthenticationProvider jHipAuthenticationProvider;

        @Autowired
        private DefaultAuthenticationSuccessHandler successHandler;

        @Autowired
        private DefaultAuthenticationFailureHandler failureHandler;

        private int duration =15;

        private String issuer="jHip-auth";


        @Autowired
        private JWTTokenService jwtTokenService;





        @Override
        protected void configure(HttpSecurity http) throws Exception {
//            http.csrf().disable().authorizeRequests()
//                    .antMatchers("/v1/user").hasAnyRole("USER")
//                    .antMatchers("/v1/book").hasAnyRole("ADMIN")
//                    .anyRequest().authenticated()
//                    .and()
//                    .formLogin().loginPage("/login").permitAll()
//                    .and().logout().permitAll();


            http.addFilterAt(jsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.csrf().disable()
                    .authorizeRequests()
                        .antMatchers("/reset_token/**").permitAll()
                        .antMatchers("/init_login/**").permitAll()
                        .and()
                    .authorizeRequests()
                        .antMatchers("/auth").permitAll()
                    .and()
                        .formLogin().loginPage("/auth")
                    .and()
                        .logout().invalidateHttpSession(true);
//                        .addLogoutHandler()
//                    .logoutSuccessHandler()

        }

        @Bean
        public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() throws Exception {
            JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter =  new JsonUsernamePasswordAuthenticationFilter();
            jsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
            jsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler());
            jsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);
            return jsonUsernamePasswordAuthenticationFilter;
        }

        @Bean
        public AuthenticationSuccessHandler jsonAuthenticationSuccessHandler() {
            return new AuthenticationSuccessHandler() {

                private SavedRequestAwareAuthenticationSuccessHandler nonJsonSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
                private JWTAuthenticationSuccessHandler jsonSuccessHandler = new JWTAuthenticationSuccessHandler(jwtTokenService, issuer, duration);

                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                    Authentication authentication) throws IOException, ServletException {

                    successHandler.onAuthenticationSuccess(request, response, authentication);

                    String contentType = request.getContentType();
                    if(contentType != null && contentType.toLowerCase().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
                        jsonSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                    } else {
                        nonJsonSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                    }
                }
            };
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(jHipAuthenticationProvider);
        }

    }











}
