package com.pachiraframework.scheduler.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pachiraframework.scheduler.util.JWTUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

/**
 * @author wangxuzheng
 *
 */
public class JWTFilter extends GenericFilterBean {
	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String TOKEN_PREFIX = "Bearer ";
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String authHeader = req.getHeader(AUTH_HEADER_KEY);
		if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
			 write(resp, "非法的请求，请求头中没有包含["+AUTH_HEADER_KEY+"]或者["+AUTH_HEADER_KEY+"]的值没有以["+TOKEN_PREFIX+"]开头");
			 return;
		}
		// The part after "Bearer "
		final String token = authHeader.substring(TOKEN_PREFIX.length()); 
		try {
            final Claims claims = Jwts.parser().setSigningKey(JWTUtil.SECRET_KEY)
                .parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        }
        catch (final SignatureException e) {
            write(resp, "非法的token参数");
            return;
        }

		chain.doFilter(req, response);
	}

	private void write(HttpServletResponse response,String error) throws IOException {
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");  
	    Map<String, Object> map = Maps.newHashMap();
	    map.put("error", error);
	    map.put("success", false);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(map);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.getWriter().write(json);
	}
}
