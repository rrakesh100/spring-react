package com.educative.auth;

import com.educative.dao.Role;
import com.educative.dao.User;
import com.educative.jwt.CookieUtil;
import com.educative.jwt.JWTUtil;
import com.educative.jwt.LoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JWTAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Getter @Setter
    private JWTTokenService jwtTokenService;
    @Getter @Setter
    private String issuer;
    @Getter @Setter
    private int duration;

    public JWTAuthenticationSuccessHandler(JWTTokenService jwtTokenService, String issuer, int duration) {
        this.jwtTokenService = jwtTokenService;
        this.issuer = issuer;
        this.duration = duration;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        User user = null;
        if(authentication.getPrincipal() instanceof JHipAuthUserDetails) {
            JHipAuthUserDetails jHipAuthUserDetails = (JHipAuthUserDetails) authentication.getPrincipal();
            user=jHipAuthUserDetails.getUserAccount();
        }else {
            throw new ServletException("Expecting JHipAuthUserDetails");
        }


        String roles[] = new String[0];
        List<String> rolesList = new ArrayList();
        user.getGroups().forEach(group -> {
            Set<Role> roleSet = group.getRoles();
            List<String> l = roleSet.stream().map(new Function<Role, String>() {

                @Override
                public String apply(Role role) {
                    return role.getName();
                }
            }).collect(Collectors.toList());
            rolesList.addAll(l);
        });

        Date currentTime = new Date();
        Date expTime = DateUtils.addMinutes(currentTime, duration);
        JWTSpec spec = JWTSpec.builder().issuer(issuer)
                .subject(user.getId().toString())
                .tenantId("")
                .rolez(rolesList.toArray(roles))
                .issuedAt(currentTime)
                .expiresAt(expTime).build();

        String token = jwtTokenService.createToken(spec);

        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setToken(token);
        responseDto.setCsrfToken(spec.getCsrfToken());
        responseDto.setExpiresAt(expTime);
        responseDto.setIssuedAt(currentTime);
        responseDto.setUserId(user.getId());
        responseDto.setTenantId(0L);
        responseDto.setRoles(roles);

        Cookie cookie = CookieUtil.createCookie(httpServletRequest, "jwt", token, duration);
        httpServletResponse.addCookie(cookie);

        PrintWriter out = httpServletResponse.getWriter();
        ObjectMapper objectMapper= new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(responseDto);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();

    }

}
