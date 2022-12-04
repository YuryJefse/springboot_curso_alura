package br.com.alura.forum.config.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secreat;

    public String gerarToken(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();

        return Jwts.builder()
                .setIssuer("Aula Forum Alura")
                .setSubject(usuario.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(new Date(hoje.getTime() + Long.valueOf(expiration)))
                .signWith(SignatureAlgorithm.HS256, secreat)
                .compact();
    }

}
