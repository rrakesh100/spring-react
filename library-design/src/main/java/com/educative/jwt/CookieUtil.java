package com.educative.jwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    private static final String HEADER_X_FORWARDED_PROTO = "X-Forwarded-Proto";
    private static final String PROTO_HTTPS = "https";

    public static Cookie createCookie(HttpServletRequest request, String name, String value, int duration) {
        boolean isSecure = PROTO_HTTPS.equalsIgnoreCase(request.getHeader(HEADER_X_FORWARDED_PROTO))
                || PROTO_HTTPS.equalsIgnoreCase(request.getScheme());

        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(duration*60);
        cookie.setPath("/");
        cookie.setSecure(isSecure);
        return cookie;
    }

    public static void removeCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}

