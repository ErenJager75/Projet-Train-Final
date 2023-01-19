package fr.ensiie.utils;

import fr.ensiie.config.JWTConfiguration;
import fr.ensiie.entity.ClientEntity;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    private final JWTConfiguration jwtConfiguration;

    public String generateToken(ClientEntity user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtConfiguration.getExpiration());

        return Jwts.builder()
                .setSubject(user.getMail())
                .claim("mail", user.getMail())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtConfiguration.getSecret())
                .compact();
    }

    public Claims getTokenInformation(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtConfiguration.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtConfiguration.getSecret()).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is incorrect");
        }
        return false;
    }

}
