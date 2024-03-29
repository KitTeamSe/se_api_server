package com.se.apiserver.v1.common.infra.security.provider;


import com.se.apiserver.v1.account.application.service.AccountContextService;
import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

@RequiredArgsConstructor
@Component
public class JwtTokenResolver {

  private final AccountContextService accountContextService;

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
    UserDetails userDetails = accountContextService.loadUserByUsername(getUserId(token));
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
      if(claims.getBody().getExpiration().before(new Date()))
        throw new BusinessException(GlobalErrorCode.EXPIRED_JWT_TOKEN);
      return true;
    }
    catch (ExpiredJwtException e){
      throw new BusinessException(GlobalErrorCode.EXPIRED_JWT_TOKEN);
    }
    catch (Exception e) {
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
    UserDetails userDetails = accountContextService.loadDefaultGroupAuthorities(defaultGroup);
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }
}
