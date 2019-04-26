package com.educative.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Processes an authentication form submission. Called {@code AuthenticationProcessingFilter} prior to Spring Security 3.0.
 * <p>
 * Login forms must present two parameters to this filter: a username and password. The
 * default parameter names to use are contained in the static fields
 * {@link #SPRING_SECURITY_FORM_USERNAME_KEY} and
 * {@link #SPRING_SECURITY_FORM_PASSWORD_KEY}. The parameter names can also be changed by
 * setting the {@code usernameParameter} and {@code passwordParameter} properties.
 * <p>
 * This filter by default responds to the URL {@code /login}.
 *
 */
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private  static  final Logger _logger = LoggerFactory.getLogger(JsonUsernamePasswordAuthenticationFilter.class);

    //purpose of overiding this method is to handle scenarios like
    //in our case username can be same across tenants
    //e.g rrakesh100@gmail.com/pepsi  or rrakesh100@gmail.com/coke. So create a unique string as username

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String contentType= request.getContentType();
        if(contentType != null && contentType.toLowerCase().startsWith(MediaType.APPLICATION_JSON_VALUE)) {

            if (!request.getMethod().equals("POST")) {
                throw new AuthenticationServiceException(
                        "Authentication method not supported: " + request.getMethod());
            }

            LoginRequest loginRequest = getRequest(request);
//              check later
//            if(loginRequest.getUsername() == null || loginRequest.getPassword() == null || loginRequest.getTenantname() == null )
//                return null;

            String username = loginRequest.getUsername().trim();
            String password = loginRequest.getPassword().trim();
            String tenantname = loginRequest.getTenantname().trim();
            if (username == null) {
                username = "";
            }else {
                username = username + "|" + tenantname;
            }

            if (password == null) {
                password = "";
            }

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, authRequest);
            return getAuthenticationManager().authenticate(authRequest);

        }else {
            _logger.info("calling super class to handle the request of content type ={} ", contentType);
            return super.attemptAuthentication(request, response);
        }

    }


    private LoginRequest getRequest(HttpServletRequest request) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = request.getReader();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(bufferedReader, LoginRequest.class);
        } catch (IOException e) {
           _logger.error("Could not read credentials from input");
        }finally {
            if( bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    _logger.error("Could not close");
                }
            }
        }
        return null;

    }

}
