package product.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("tokenAuthentication")
public class TokenAuthenticationImpl implements TokenAuthentication{
    @Value("${jwt.key}")
    private String SECRET;

    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";
    public List<String> getRoles(String token){
        if (token != null) {
            // parse the token.
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));
            String email = claimsJws.getBody().getSubject();
            return (List<String>) claimsJws.getBody().get("scopes");

        }
        return null;
    }
    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            Jws<Claims> claimsJws= Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));
            String email=claimsJws.getBody().getSubject();
            List<String> scopes = (List<String>) claimsJws.getBody().get("scopes");
            List<GrantedAuthority> authorities;
            if(scopes!=null) {
                authorities = scopes.stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toList());
            } else {
                authorities=new ArrayList<>();
            }
            return email != null ?
                    new UsernamePasswordAuthenticationToken(email, null, authorities) :
                    null;
        }
        return null;
    }
}
