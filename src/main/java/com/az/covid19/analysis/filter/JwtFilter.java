package com.az.covid19.analysis.filter;

import com.az.covid19.analysis.constant.AppConstants;
import com.az.covid19.analysis.exception.CustomRuntimeException;
import com.az.covid19.analysis.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("doFilterInternal request{}, response{}", request,response);

        try{
            if(request.getRequestURI().contains(AppConstants.URI_FOR_COVID_ANALYSIS)) {
                final String authorizationHeader = request.getHeader(AppConstants.AUTHORIZATION);
                validateAuthorizationHeader(authorizationHeader);
                String userName = null;
                String jwt = getJwtFromHeader(authorizationHeader);
                jwtUtil.validateToken(jwt);
                userName = jwtUtil.extractUsername(jwt);
                // SecurityContextHolder.getContext().getAuthentication() == null this means we haven't assigned authentication yet
                if (StringUtils.hasText(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e){
           resolver.resolveException(request, response, null, e);
        }
    }

    private String getJwtFromHeader(String authorizationHeader){
        return authorizationHeader.substring(7);
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        if(!StringUtils.hasText(authorizationHeader))
            throw new CustomRuntimeException("Authorization is missing",
                    "Authorization is required in header for this URI", HttpStatus.BAD_REQUEST);

        if(!authorizationHeader.contains("Bearer "))
            throw new CustomRuntimeException("Authorization value is invalid",
                    "Authorization value in header should be  followed by Bearer JWT", HttpStatus.BAD_REQUEST);
    }
}