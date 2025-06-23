package it.epicode.epic_energy_services_team4_bw.security;


import it.epicode.epic_energy_services_team4_bw.exception.UnauthorizedException;
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

@Component
public class JwtFilter extends OncePerRequestFilter {
    // vai su errore, implement method

    @Autowired
    private JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //questo metodo dovrà verificare se la richiesta ha il token
        //se non ha il token -> eccezione
        //se ha il token -> viene verficato che il token sia valido,se non è valido -> eccezione
        //se se è valido, accedere la richiesta ai filtri successivi

        String authorization = request.getHeader("Authorization");
        if(authorization== null || !authorization.startsWith("Bearer ")){
            throw new UnauthorizedException("Token non presente");
        } else {
            String token = authorization.substring(7); // mi prendo la parte dopo "bearer "
            jwtTool.validateToken(token);

            try{

                // recuperiamo utente
                User user = jwtTool.getUserFromToken(token);
                //controllare il ruolo
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); // getAuthorities mi rende il ruolo?

                // mantenere nel suo contesto i dati sulla sicurezza di questo utente?
                //abilitare o meno l'accesso a un serevizio a seconda dell'utente
                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (NotFoundException e) {
                throw new UnauthorizedException("Utente collegato al token non trovato");
            }




            filterChain.doFilter(request, response);
        }
    }

    //questo serve per non avere il controllo del token su alcuni endpoint (tipo la pagina di login)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
