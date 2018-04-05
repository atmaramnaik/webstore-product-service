package product.services;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface TokenAuthentication {
    Authentication getAuthentication(HttpServletRequest request);
}
