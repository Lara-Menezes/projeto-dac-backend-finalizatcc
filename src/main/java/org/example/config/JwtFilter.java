package org.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.model.Usuario;
import org.example.services.JwtService;
import org.example.services.UsuarioDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)

            throws IOException, ServletException {



        String header =
                request.getHeader("Authorization");



        if(header != null && header.startsWith("Bearer ")) {


            String token =
                    header.substring(7);



            String email =
                    jwtService.getEmail(token);



            if(email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {



                Usuario usuario =
                        (Usuario) usuarioDetailsService
                                .loadUserByUsername(email);



                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                usuario,
                                null,
                                usuario.getAuthorities()
                        );



                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );



                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }

        }


        chain.doFilter(request,response);

    }

}

