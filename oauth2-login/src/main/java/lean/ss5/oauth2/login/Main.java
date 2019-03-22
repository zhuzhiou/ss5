package lean.ss5.oauth2.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/")
    public Map<String, String> index(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                     @AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("principalName", authorizedClient.getPrincipalName());
        hashMap.put("accessToken", authorizedClient.getAccessToken().getTokenValue());
        hashMap.put("name", oauth2User.getName());
        oauth2User.getAuthorities().forEach(authority ->
            hashMap.put("authority[]", authority.getAuthority())
        );

        return hashMap;
    }
}
