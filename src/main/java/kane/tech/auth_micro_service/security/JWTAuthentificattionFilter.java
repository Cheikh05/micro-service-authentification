package kane.tech.auth_micro_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import kane.tech.auth_micro_service.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JWTAuthentificattionFilter extends UsernamePasswordAuthenticationFilter {


    public AuthenticationManager authenticationManager;

   public JWTAuthentificattionFilter(AuthenticationManager authenticationManager){
       super();
       this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

/*
       try{
           AppUser appUser = new ObjectMapper().readValue(request.getInputStream(),AppUser.class);
           return new UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword());

       }catch (Exception e){
          e.printStackTrace();
           throw new RuntimeException(e);
       }
*/

        AppUser user = null;
        try {
            user = new ObjectMapper().readValue( request.getInputStream(), AppUser.class );
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }

        return authenticationManager
                .authenticate( new UsernamePasswordAuthenticationToken( user.getUsername(), user.getPassword() ) );



    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User)authResult.getPrincipal();
       // AppUser user = (AppUser) authResult.getPrincipal();

        List<String> roles = new ArrayList<>();

        authResult.getAuthorities().forEach(a->{
            roles.add(a.getAuthority());
        });

        String jwt = JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ SecurityConstant.NOMBRE_EXPIRATION))
                .withArrayClaim("roles",roles.toArray(new String[roles.size()]))
                .sign(Algorithm.HMAC256(SecurityConstant.SECRET));

        response.addHeader(SecurityConstant.REQUEST_HEADER_PREFIX,SecurityConstant.HEADER_TOKEN+jwt);
    }
}
