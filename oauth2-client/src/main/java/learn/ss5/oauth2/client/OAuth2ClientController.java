package learn.ss5.oauth2.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OAuth2ClientController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/")
    public List<String> doGet() {
        List<String> list = new ArrayList<>();
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
        clientRegistrations.forEach(clientRegistration -> {
            list.add(clientRegistration.getClientName() + ": http://oauth2client/oauth2/authorization/" + clientRegistration.getRegistrationId());
        });
        return list;
    }

    @GetMapping("/github")
    public String github(@RegisteredOAuth2AuthorizedClient(registrationId = "github") OAuth2AuthorizedClient authorizedClient) {
        ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        return clientRegistration.getClientName() + ": " + accessToken.getTokenValue();
    }

    @GetMapping("/custom")
    public String userinfo(@RegisteredOAuth2AuthorizedClient(registrationId = "custom") OAuth2AuthorizedClient authorizedClient) {
        ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        return clientRegistration.getClientName() + ": " +  accessToken.getTokenValue();
    }
}
