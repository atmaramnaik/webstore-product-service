package product.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import product.services.external.Registration;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("tokenAuthentication")
public class TokenAuthenticationImpl implements TokenAuthentication{
    @Autowired
    private Registration registration;

    public Authentication getAuthentication(HttpServletRequest request) {
            List<String> scopes = registration.getRoles(request.getHeader("Authorization"));
            List<GrantedAuthority> authorities;
            if(scopes!=null) {
                authorities = scopes.stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toList());
            } else {
                authorities=new ArrayList<>();
            }
            return new UsernamePasswordAuthenticationToken("authorized", null, authorities) ;
    }
}
