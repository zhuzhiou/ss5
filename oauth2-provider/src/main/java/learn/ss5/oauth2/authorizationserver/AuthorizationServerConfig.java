package learn.ss5.oauth2.authorizationserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("a3885c28332f11e98285000c29c09b61")
                .secret("test")
                .authorizedGrantTypes("authorization_code")
                .autoApprove(true)
                .redirectUris("http://resourceserver", "http://oauth2client/login/oauth2/code/custom", "http://oauth2login/login/oauth2/code/custom")
                .authorities("test")
                .scopes("test");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.accessTokenConverter(tokenEnhancer());
    }

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom("ss5".getBytes());
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        JwtAccessTokenConverter tokenEnhancer = new JwtAccessTokenConverter();
        tokenEnhancer.setKeyPair(keyPair);
        return tokenEnhancer;
    }
}
