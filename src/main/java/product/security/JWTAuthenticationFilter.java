package product.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;
import product.services.TokenAuthentication;
import product.services.TokenAuthenticationImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean  {

    public TokenAuthentication tokenAuthentication;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        if(tokenAuthentication==null){
            ServletContext servletContext=request.getServletContext();
            WebApplicationContext webApplicationContext= WebApplicationContextUtils.getWebApplicationContext(servletContext);
            tokenAuthentication=webApplicationContext.getBean(TokenAuthentication.class);
        }
        Authentication authentication = tokenAuthentication
                .getAuthentication((HttpServletRequest)request);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}