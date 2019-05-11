package product.services;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TokenAuthentication {
    Authentication getAuthentication(HttpServletRequest request);
    public List<String> getRoles(String token);
}
