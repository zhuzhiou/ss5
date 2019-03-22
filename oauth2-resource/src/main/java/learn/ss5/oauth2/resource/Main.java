package learn.ss5.oauth2.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;

@SpringBootApplication
@RestController
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/test")
    public String doGet() {
        return String.valueOf(System.currentTimeMillis());
    }

    @GetMapping("/userinfo")
    public UserDetails userDetails(@AuthenticationPrincipal Jwt jwt) {
        System.out.println(jwt.getSubject());
        /*
         * principal.getName()取的是 claim中的subject
         * UserDetails userDetails = userService.findByName(principal.getName);
         */
        UserDetails userDetails = new User("zhuzhiou", "", Arrays.asList(new SimpleGrantedAuthority("USER")));
        return userDetails;
    }
}
