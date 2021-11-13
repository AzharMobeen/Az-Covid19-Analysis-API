package com.az.covid19.analysis.util;

import com.az.covid19.analysis.exception.CustomRuntimeException;
import com.az.covid19.analysis.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public void validateTokenExpiration(String token) {
        if(extractExpiration(token).before(new Date()))
            throw  new CustomRuntimeException("JWT is expired",
                    "JWT is valid only for 30 min, please generate new JWT",
                    HttpStatus.NOT_ACCEPTABLE);
    }

    public void validateToken(String token) {
        validateTokenExpiration(token);
        if(!userRepository.existsByJwt(token)) {
            throw new CustomRuntimeException("JWT is wrong", "JWT is no longer valid for this user",
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public String generateToken(UserDetails userDetails) {
        log.debug("generateToken method called {}", userDetails);
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                //           converting milli seconds to  0.5 Hr     (1sec, 1min, 30min)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30 ))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
}