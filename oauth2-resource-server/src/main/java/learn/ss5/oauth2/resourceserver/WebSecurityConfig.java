package learn.ss5.oauth2.resourceserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .httpBasic()
                .disable()
            .csrf()
                .disable()
            .authorizeRequests()
                .anyRequest()
                    .authenticated()
                    .and()
            .oauth2ResourceServer()
                .jwt();
        //@formatter:on
    }
}
