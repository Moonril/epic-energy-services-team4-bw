package it.epicode.epic_energy_services_team4_bw.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.epicode.security_practice.exceptions.NotFoundException;
import it.epicode.security_practice.model.User;
import it.epicode.security_practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {

    // classe gestita direttamente da string per la gestione del token

    @Value("${jwt.duration}")
    private long durata;

    @Value("${jwt.secret}")
    private String chiaveSegreta;

    // ci serve il service dell'user per estrarre l'utente in base la token
    @Autowired
    private UserService userService;

    public String createToken(User user) {
        //claim tutti i componenti del token ???

        //creare token e dargli una durata
        return Jwts.builder().issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+durata))
                .subject(user.getId() + "").signWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).compact(); // questo crea il token, nel token c'è la data di creazione e l'id

    }

    //metodo per la validità del token
    public void validateToken(String token){
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes()))
                .build().parse(token);
    }

    // metodo per estrarre l'utente collegato al token, aggiungi service utente sopra

    public User getUserFromToken(String token) throws NotFoundException {
        //recuperare l'id dal token + estrarre l'id dal token. subjetc contiene l'id dell'utente come stringa, quindi cast in integer
        int id = Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).build().parseSignedClaims(token).getPayload().getSubject());
        // estrai utente con questo id
        return userService.getUser(id);

    }
}
