package com.example.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  // @Value("${app.jwtSecret}")
  // private String jwtSecret;

  // @Value("${app.jwtExpirationInMs}")
  // private int jwtExpirationInMs;

  private static final String jwtSecret =
    "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

  // private int jwtExpirationInMs = 60000000;

  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, username);
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String usnername = extractUsername(token);
    return (
      usnername.equals(userDetails.getUsername()) && !isTokenExpired(token)
    );
  }

  public Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private String createToken(Map<String, Object> claims, String username) {
    String token = Jwts
      .builder()
      .setClaims(claims)
      .setSubject(username)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
      .signWith(getSignKey(), SignatureAlgorithm.HS256)
      .compact();

    return token;
  }

  private Key getSignKey() {
    try {
      byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
      return Keys.hmacShaKeyFor(keyBytes);
    } catch (Exception e) {
      System.out.println("getSignKeys");
      System.out.println(e);
      return null;
    }
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    Claims claims = Jwts
      .parserBuilder()
      .setSigningKey(getSignKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
    return claims;
  }
}
