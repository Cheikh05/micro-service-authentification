package kane.tech.auth_micro_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserDetailsServiceImpl userDetailsService;

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
       // http.formLogin();

        // Désactiver la vérification du token csrf
        http.csrf().disable();
        // Connexion sans etat
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // On ajoute un filtre qui se chargera de gérer l'authentification
        http.addFilter(new JWTAuthentificattionFilter(authenticationManager()));
        http.authorizeRequests().antMatchers("/login/**","/register/**").permitAll();
        // La gestion des Roles et des Users néccéssite le role d'ADMIN
        http.authorizeRequests().antMatchers("/appUsers/**").hasAuthority("USER");
        http.authorizeRequests().antMatchers(  "/appRoles/**" ).hasAuthority( "ADMIN" );
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new JWTAuthorization(), UsernamePasswordAuthenticationFilter.class);






    }
}
