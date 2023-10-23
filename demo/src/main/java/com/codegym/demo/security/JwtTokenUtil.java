package com.codegym.demo.security;

import com.codegym.demo.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationInMs;

	public String generateToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		return Jwts.builder()
				.setSubject(user.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS256, jwtSecret)
				.compact();
	}

	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();

		return claims.getSubject();
	}

	public String getJwtFromBearerToken(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody();

		return claims.getSubject();
	}

	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}
	
//	public static final long JWT_TOKEN_VALIDITY = 20*60;
//
//	public static final long JWT_REFRESH_TOKEN_VALIDITY = 7*24*60*60;
//
//	@Value("${jwt.secret}")
//	private String secret;
//
//	public String getUsernameFromToken(String token) {
//		return getClaimFromToken(token, Claims::getSubject);
//	}
//
//	public Date getIssuedAtDateFromToken(String token) {
//		return getClaimFromToken(token, Claims::getIssuedAt);
//	}
//
//	public Date getExpirationDateFromToken(String token) {
//		return getClaimFromToken(token, Claims::getExpiration);
//	}
//
//	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//		final Claims claims = getAllClaimsFromToken(token);
//		return claimsResolver.apply(claims);
//	}
//
//	private Claims getAllClaimsFromToken(String token) {
//		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//	}
//
//	public Boolean isTokenExpired(String token) {
//		final Date expiration = getExpirationDateFromToken(token);
//		return expiration.before(new Date());
//	}
//
//	private Boolean ignoreTokenExpiration(String token) {
//		// here you specify tokens, for that the expiration is ignored
//		return false;
//	}
//
//	public String generateToken(User user) {
//		Map<String, Object> claims = new HashMap<>();
//		return doGenerateToken(claims, user.getUsername());
//	}
//
//	private String doGenerateToken(Map<String, Object> claims, String subject) {
//
//		return Jwts.builder()
//				.setClaims(claims)
//				.setSubject(subject)
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
//				.signWith(SignatureAlgorithm.HS256, secret)
//				.compact();
//	}
//
//	private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
//
//		return Jwts.builder()
//				.setClaims(claims)
//				.setSubject(subject)
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY*1000))
//				.signWith(SignatureAlgorithm.HS256, secret)
//				.compact();
//	}
//
//	public Boolean canTokenBeRefreshed(String token) {
//		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
//	}
//
//	public Boolean validateToken(String token, User user) {
//		final String username = getUsernameFromToken(token);
//		return (username.equals(user.getUsername()) && !isTokenExpired(token));
//	}
}
