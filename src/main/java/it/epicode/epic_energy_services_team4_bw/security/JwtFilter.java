package it.epicode.epic_energy_services_team4_bw.security;


import it.epicode.epic_energy_services_team4_bw.exception.NotFoundException;
import it.epicode.epic_energy_services_team4_bw.exception.UnauthorizedException;
import it.epicode.epic_energy_services_team4_bw.model.Utente;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {
    // vai su errore, implement method

    @Autowired
    private JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if(authorization== null || !authorization.startsWith("Bearer ")){
            throw new UnauthorizedException("Token non presente");
        } else {
            String token = authorization.substring(7); // mi prendo la parte dopo "bearer "
            jwtTool.validateToken(token);

            try{


                Utente utente = jwtTool.getUtenteFromToken(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(utente, null, utente.getAuthorities());


                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (NotFoundException e) {
                throw new UnauthorizedException("Utente collegato al token non trovato");
            }




            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludedEndpoints = new String[] {"/auth/**", "/html/**"};

        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }
}
