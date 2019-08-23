package samples.security.oauth2.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.OAuth2User;

@SpringBootApplication
public class Main {

    @Bean
    public DefaultAuthorizationCodeTokenResponseClient authorizationCodeTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService oauth2UserService = new DefaultOAuth2UserService();
        return oauth2UserService;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("custom")
                .clientId("d11fdc32ec8d11e8be7628d244f87815")
                .clientSecret("PBMJ9QBuqHMthUy8DQLfxwSEkEzAdfgq")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("http://localhost:8084/oauth/authorize")
                .tokenUri("http://localhost:8084/oauth/token")
                .userInfoUri("http://localhost:8084/api/userinfo")
                .redirectUriTemplate("http://localhost:8080/oauth")
                .userNameAttributeName("username")
                .scope("bds")
                .build();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
