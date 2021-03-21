package com.se.apiserver.security.provider;


import com.se.apiserver.domain.usecase.account.AccountReadUseCase;
import com.se.apiserver.security.service.AccountDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenResolver {

  private final AccountDetailService accountDetailService;

  @Value("${spring.jwt.header}")
  private String AUTH_HEADER;

  @Value("${spring.jwt.default-group}")
  private String defaultGroup;

  @Value("${spring.jwt.secret}")
  private String securityKey;

  private final Long tokenExpirePeriod = 1000L * 60 * 60;

  @PostConstruct
  protected void init() {
    securityKey = Base64.getEncoder().encodeToString(securityKey.getBytes());
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = accountDetailService.loadUserByUsername(getUserId(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUserId(String token) {
    return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest httpRequest) {
    return httpRequest.getHeader(AUTH_HEADER);
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  // TODO 인증서버 구축시 삭제
  public String createToken(String userId) {
    Claims claims = Jwts.claims().setSubject(userId);
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + tokenExpirePeriod))
        .signWith(SignatureAlgorithm.HS256, securityKey)
        .compact();
  }

  public Authentication getDefaultAuthentication() {
    UserDetails userDetails = accountDetailService.loadDefaultGroupAuthorities(defaultGroup);
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }
}
