package product.services.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service("registration")
public class RegistrationImpl implements Registration {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${registration.service}")
    private String REGISTRATION_SERVICE_URL;

    static String ROLES_ENDPOINT="/auth/roles";

    @Override
    public List<String> getRoles(String token) {
        HttpHeaders headers=new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization",token);
        HttpEntity<String> entity=new HttpEntity<>("parameters",headers);
        return restTemplate.exchange(REGISTRATION_SERVICE_URL+ROLES_ENDPOINT, HttpMethod.GET,entity,List.class).getBody();
    }
}
