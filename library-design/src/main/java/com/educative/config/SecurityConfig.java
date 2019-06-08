package com.educative.config;

import com.educative.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
        jHipAuthenticationProvider.setPasswordEncoder(customPasswordEncoder());
       // jHipAuthenticationProvider.set
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

//    @Configuration
//    @Order(100)
//    public static class X509SecurityConfigurationAdapter extends AbstractX509SecurityConfigurationAdapter {
//
//        @Override
//        protected void configureUrlPattern(HttpSecurity http) throws Exception {
//
////            http.antMatcher("/internal/**")
////                    .authorizeRequests()
////                    .anyRequest().hasAnyRole("INTERNAL_SERVICE");
//        }
//
//    }

//    @Configuration
//    @Order(200)
//    public static class ApiSecurityConfigurationAdapter extends AbstractJWTSecurityConfigurationAdapter {
//
//        @Autowired
//        private JWTTokenService jwtTokenService;
//
//        @Override
//        protected void configureUrlPattern(HttpSecurity http) throws Exception {
////            http.csrf().disable()
////                    .antMatcher("/api/**")
////                    .authorizeRequests()
////                    .anyRequest().authenticated()
////                    .and()
////                    .httpBasic();
//        }
//
//        @Override
//        protected JWTTokenService jwtTokenService() {
//            return jwtTokenService;
//        }
//
//    }

    @Configuration
    public static class FormLoginSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{



        @Autowired
        private JHipAuthenticationProvider jHipAuthenticationProvider;

        @Autowired
        private DefaultAuthenticationSuccessHandler successHandler;

        @Autowired
        private DefaultAuthenticationFailureHandler failureHandler;

        @Autowired
        private AuthenticationEntryPoint authenticationEntryPoint;

        private int duration =15;

        private String issuer="jHip-auth";


        @Autowired
        private JWTTokenService jwtTokenService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(jHipAuthenticationProvider);
        }


//        @Override
//        public void configure(final AuthenticationManagerBuilder auth) throws Exception {
//                        auth.inMemoryAuthentication()
//                    .withUser("admin").password(encoder().encode("admin")).roles("ADMIN")
//                    .and()
//                    .withUser("user").password(encoder().encode("user")).roles("USER");
//        }



        @Override
        protected void configure(HttpSecurity http) throws Exception {


            http.csrf().disable().authorizeRequests().anyRequest().authenticated()
                    .and()
                    .formLogin().defaultSuccessUrl("/dashboard")
                    .loginPage("/login").permitAll().and().
                    httpBasic().authenticationEntryPoint(authenticationEntryPoint).and().
                    logout().permitAll();

            //http.addFilterAt(jsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//            http.csrf().disable()
//                    .authorizeRequests()
//                        .antMatchers("/login").permitAll()
//                        .anyRequest().authenticated()
//                    .and()
//                        .formLogin()
//                        .permitAll()
//                    .and()
//                        .httpBasic()
//                    .and()
//                        .logout().invalidateHttpSession(true);

        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/*.css");
            web.ignoring().antMatchers("/*.js");
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




    }

    @Bean
    public PasswordEncoder customPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(4));
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
            }
        };
    }











}
