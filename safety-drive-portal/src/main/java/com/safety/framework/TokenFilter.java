package com.safety.framework;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.safety.util.TokenUtil;
import com.safety.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanwenbin on 2017/7/15.
 */
public class TokenFilter implements Filter {
    public static final String HTTP_ACCESS_TOKEN = "HTTP-ACCESS-TOKEN";

    private static final List<String> IGNORE_URL = new ArrayList<String>();


    final static Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    private static MACVerifier verifier = null;

    static {
        SecretKey secretKeySpec = new SecretKeySpec(TokenUtil.getSecretKey(), "AES");
        try {
            verifier = new MACVerifier(secretKeySpec);
        } catch (JOSEException e) {
            logger.error("new MACVerifier.error", e);
            System.exit(0);
        }
        IGNORE_URL.add("/login");
        IGNORE_URL.add("/register");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (HttpMethod.OPTIONS.equals(httpRequest.getMethod())) return;
        for (String ignore : IGNORE_URL) {
            if (httpRequest.getRequestURI().indexOf(ignore) > 0) {
                chain.doFilter(request, response);
                return;
            }
        }
        Response<String> error = securityCheck(httpRequest, httpResponse);
        if (null != error) {
            logger.error("securityCheck 302 error != null");
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write(JSON.toJSONString(error));
            httpResponse.getWriter().flush();
            return;
        }

        chain.doFilter(request, response);
    }

    private Response<String> securityCheck(HttpServletRequest request, HttpServletResponse
            response) {
        String token = request.getHeader(HTTP_ACCESS_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return Response.FAIL(403,"token invalid");
        }

        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            logger.error("SignedJWT.parse.error", e);
            return Response.FAIL(500,"");
        }
        try {
            if (!signedJWT.verify(verifier)) {
                return Response.FAIL(403,"token invalid");
            }
        } catch (JOSEException e) {
            logger.error("signedJWT.verify.error", e);
            return Response.FAIL(500,"");
        }

        try {
            if (Utils.getNow().getTime() > signedJWT.getJWTClaimsSet().getExpirationTime().getTime()) {
                logger.info("token is expire token{}", token);
                return Response.FAIL(403,"TOKEN_EXPIRE");
            }
            request.setAttribute("context", new Context(
                    Integer.valueOf(signedJWT.getJWTClaimsSet().getJWTID()),
                    signedJWT.getJWTClaimsSet().getIssuer()));
        } catch (ParseException e) {
            logger.error("signedJWT.getJWTClaimsSet().error", e);
            return Response.FAIL(500,"");
        }

        return null;
    }

    public void destroy() {

    }
}
