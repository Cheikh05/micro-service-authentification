package kane.tech.auth_micro_service.web;

import kane.tech.auth_micro_service.entities.AppUser;
import kane.tech.auth_micro_service.service.AccountService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private AccountService accountService;


    @PostMapping("/register")
    public AppUser register(@RequestBody UserForm userForm){
      return accountService.saveUser(userForm.getUsername(),userForm.getPassword(),userForm.getConfirmation());
    }
}

@Data
class UserForm{
    private String username;
    private String password;
    private String confirmation;
}
