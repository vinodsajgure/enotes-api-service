package com.demo.enotes_api.config.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.enotes_api.response_handler.GenericResponse;
import com.demo.enotes_api.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			String authheader = request.getHeader("Authorization");
			String token = null;
			String username = null;

			if (authheader != null && authheader.startsWith("Bearer ")) {

				token = authheader.substring(7);
				username = jwtService.extractUsername(token);
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				Boolean validateToken = jwtService.validateToken(token, userDetails);

				if (validateToken) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}

			}
		} catch (Exception e) {

			generateResponseError(response, e);

			return;
		}
		filterChain.doFilter(request, response);
	}

	private void generateResponseError(HttpServletResponse response, Exception e)
			throws JsonProcessingException, IOException {
		response.setContentType("application/json");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		Object error = GenericResponse.builder()
				.status("failed")
				.message(e.getMessage())
				.responseStatus(HttpStatus.UNAUTHORIZED)
				.build().createResponse().getBody();

		response.getWriter().write(new ObjectMapper().writeValueAsString(error));

	}

}
