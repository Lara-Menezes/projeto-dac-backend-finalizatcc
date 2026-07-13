package org.example.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String SECRET =
            "minha-chave-super-secreta-com-mais-de-256-bits";


    private final long EXPIRATION = 3600000;


    public String gerarToken(Usuario usuario) {

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("tipo", usuario.getTipo().name())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis() + EXPIRATION
                        )
                )
                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes()
                        )
                )
                .compact();
    }



    public String getEmail(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes()
                        )
                )
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}


