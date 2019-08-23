package samples.security.oauth2.client;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ShowcaseController {

    @Autowired
    private DefaultAuthorizationCodeTokenResponseClient authorizationCodeTokenResponseClient;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService;

    private RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS).build();

    private Map<String, OAuth2AuthorizationRequest> authorizationRequestRegistry = new HashMap<>();

    /**
     * 生成类似 <code>http://localhost:8084/oauth/authorize?response_type=code&client_id=d11fdc32ec8d11e8be7628d244f87815&state=aert&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2F</code> 的路径
     */
    @GetMapping(value = "/oauth")
    public RedirectView authorizeRedirect() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("custom");
        OAuth2AuthorizationRequest authorizationRequest =
                OAuth2AuthorizationRequest
                        .authorizationCode()
                        /*
                        .authorizationRequestUri("http://localhost:8080/")//可以不使用默认的规则生成
                        */
                        .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                        .state(generator.generate(6))
                        // 这里设置的是回调地址
                        .redirectUri(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUriString())
                        .clientId(clientRegistration.getClientId())
                        .build();
        authorizationRequestRegistry.put(authorizationRequest.getState(), authorizationRequest);
        return new RedirectView(authorizationRequest.getAuthorizationRequestUri());
    }

    /**
     * 接收授权后的请求，请求地址类似：http://localhost:8080/?code=iUeKd&state=Hei5x
     */
    @GetMapping(value = "/oauth", params = "code")
    @ResponseBody
    public OAuth2User authorizeCallback(@RequestParam String code, @RequestParam String state) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("custom");
        OAuth2AuthorizationRequest authorizationRequest = authorizationRequestRegistry.remove(state);

        OAuth2AuthorizationResponse authorizationResponse =
                OAuth2AuthorizationResponse.success(code)
                        // 这里用来对比 OAuth2AuthorizationRequest 的 redirectUri
                        .redirectUri(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUriString())
                        .state(state)
                        .build();
        OAuth2AuthorizationExchange authorizationExchange = new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse);
        OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest
                = new OAuth2AuthorizationCodeGrantRequest(clientRegistration, authorizationExchange);
        OAuth2AccessTokenResponse accessTokenResponse = authorizationCodeTokenResponseClient.getTokenResponse(authorizationCodeGrantRequest);

        OAuth2UserRequest oauth2UserRequest = new OAuth2UserRequest(clientRegistration, accessTokenResponse.getAccessToken());
        OAuth2User oauthUser = oauth2UserService.loadUser(oauth2UserRequest);

        return oauthUser;
    }
}
