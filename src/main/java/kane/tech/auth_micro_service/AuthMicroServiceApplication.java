package kane.tech.auth_micro_service;

import kane.tech.auth_micro_service.entities.AppRole;
import kane.tech.auth_micro_service.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class AuthMicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthMicroServiceApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder getBCRYPT(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
           accountService.saveRole(new AppRole(null,"USER"));
           accountService.saveRole(new AppRole(null,"ADMIN"));

            Stream.of("user1","cheikh","user2","admin").forEach(
                    u->{
                        accountService.saveUser(u,"1234","1234");
                    }
            );

            accountService.addRoleToUser("admin","ADMIN");
          accountService.getAllUSer().forEach(
                  u->{
                      System.out.println(u);
                  }
          );
        };
    }

}
