package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedirectUrlCookieFilter extends OncePerRequestFilter {
	public static final String REDIRECT_URI_PARAM = "redirect_url";
	private static final int MAX_AGE = 180;
		
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		
		if (request.getRequestURI().startsWith("/auth/authorize")) {
			try {
				log.info("request url {} ", request.getRequestURI());
				String redirectUrl = request.getParameter(REDIRECT_URI_PARAM); //리퀘스트 파라미터에서 redirect_url를 가져온다.
				
				Cookie cookie = new Cookie(REDIRECT_URI_PARAM, redirectUrl);
				cookie.setPath("/");
				cookie.setHttpOnly(true);
				cookie.setMaxAge(MAX_AGE);
				response.addCookie(cookie);
				
			} catch (Exception ex) {
				logger.error("Could not set user authentication in security context", ex);
				log.info("Unauthorized Request");
			}
		}
		filterChain.doFilter(request, response);
		
	}
	
	
	

}
