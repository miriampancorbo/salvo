package com.minhubweb.salvo.configs;
import com.minhubweb.salvo.models.Player;
import com.minhubweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired  //Spring hará nuevos objetos de tipo playerRepository. Busca una class ya creada que se llame así y se lo paso. Inyección de dependencia
    PlayerRepository playerRepository; ///inyección de dependencia. Spring crea un new pr o ve si hay creados

    @Bean //Guarda en memoria ese método para usarlo cuando se necesite
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Player player = playerRepository.findByUserName(inputName);
            if (player != null) {
                if(player.getUserName().equals("miri")){
                    return new User(player.getUserName(),player.getPassword(),
                            AuthorityUtils.createAuthorityList("ADMIN","USER"));
                }
                else {
                    return new User(player.getUserName(), player.getPassword(),
                            AuthorityUtils.createAuthorityList("USER"));
                }
            }
            else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}


