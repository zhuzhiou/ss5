package lean.ss5.oauth2.login;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //formatter:off
        http
            .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
            .oauth2Login();
    }
}
