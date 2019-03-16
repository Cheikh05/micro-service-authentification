package kane.tech.auth_micro_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JWTAuthorization extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        response.addHeader( "Access-Control-Allow-Origin", "*" );
        response.addHeader( "Access-Control-Allow-Headers",
                "Origin,"
                        + "Accept,"
                        + "X-Requested-With,"
                        + "Content-Type,"
                        + "Access-Control-Request-Method,"
                        + "Access-Control-Request-Headers,"
                        + "Authorization" );

        response.addHeader( "Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin,Access-Control-Allow-Credentials,authorization,authorization" );

        response.addHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,PATCH");
        if ( request.getMethod().equals( "OPTIONS" ) ) {
            response.setStatus(
                    HttpServletResponse.SC_OK );
        }
        else if(request.getRequestURI().equals("/login")){
            filterChain.doFilter(request,response);
            return ;
        }else {

            // Récupération du token
            String jwtToken = request.getHeader(SecurityConstant.REQUEST_HEADER_PREFIX);
            System.out.println("Toekn JWT =" + jwtToken);

            if (jwtToken == null || !jwtToken.startsWith(SecurityConstant.HEADER_TOKEN)) {
                filterChain.doFilter(request, response);
                return;
            }

            // cette objet nous permet de vérifier le token
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityConstant.SECRET)).build();

            // Vérification du token à l'aide de l'objet verifier
            DecodedJWT decodedJWT = verifier.verify(jwtToken.substring(SecurityConstant.HEADER_TOKEN.length()));

            // Récupération du username
            String username = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);


            System.out.println("Username  = " + username);
            System.out.println("Roles = " + roles.toString());

            // Convertion des roles en GrantedAuthority
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            roles.forEach(
                    r -> {
                        authorities.add(new SimpleGrantedAuthority(r));
                    }
            );
            // Les roles de l'utilisateur est contenu dans l'objet authorities


            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username, null, authorities);

            // On authentifie l'utilisateur

            SecurityContextHolder.getContext().setAuthentication(user);

            // Et on passe à l'étape suivante

            filterChain.doFilter(request, response);
        }
    }
}
