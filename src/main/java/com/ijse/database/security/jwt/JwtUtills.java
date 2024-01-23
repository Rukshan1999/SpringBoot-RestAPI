package com.ijse.database.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtills {
    
    @Value("$(app.secrect)")
    private String secrect;

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secrect));
    }

    public String generateJwtToken(Authentication authentication){

        UserDetails userDetails =(UserDetails) authentication.getPrincipal();

        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime()+86400000))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String jwtToken){
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(jwtToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Token has been changed.Invalied");
        } catch(ExpiredJwtException e){
            System.out.println("Token is Expired!");
        } catch(UnsupportedJwtException e){
            System.out.println("Unsupported Token!");
        } catch(IllegalArgumentException e){
            System.out.println("Blank Token");
        }

        return false;
    }

    public String getUserNameFromToken(String jwtToken){
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(jwtToken).getBody().getSubject();
    }

}
