package learn.ss5.oauth2.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class RegisteredOAuth2AuthorizedClientController {

    @Autowired
    private WebClient webClient;

    @GetMapping("/")
    public Mono<String> helloworld(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        Mono<String> mono = this.webClient
                .get()
                .uri("http://oauth2resource/helloworld")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class);
        return mono;
    }
}
