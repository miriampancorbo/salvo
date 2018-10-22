package com.minhubweb.salvo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/rest/**").hasAnyAuthority( "ADMIN")
            .antMatchers("/web/game.html","/web/scripts/game.js").hasAnyAuthority("USER")
            .antMatchers("/api/game_view/**").hasAnyAuthority("USER")
            .antMatchers("/web/**", "/api/**").permitAll();
        http
            .formLogin()
            .usernameParameter("username")
            .passwordParameter("password")
            .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        //Bug h2 spring boot security
        http.headers().frameOptions().disable();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

/*
@Autowired
private PlayerRepository playerRepository;

@RequestMapping("/books")
public List<Book> getAll(Authentication authentication) {
    return playerRepository.findByUserName(authentication.getUserName());
};
*/



/*import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
*/
