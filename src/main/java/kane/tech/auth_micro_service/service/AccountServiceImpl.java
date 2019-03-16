package kane.tech.auth_micro_service.service;

import kane.tech.auth_micro_service.dao.RoleRepository;
import kane.tech.auth_micro_service.dao.UserRepository;
import kane.tech.auth_micro_service.entities.AppRole;
import kane.tech.auth_micro_service.entities.AppUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional

public class AccountServiceImpl implements AccountService {
    public UserRepository userRepository;
    public RoleRepository roleRepository;
    public BCryptPasswordEncoder bCryptPasswordEncoder;


    // Pour l'injection de dépendence

    public AccountServiceImpl(UserRepository userRepository,RoleRepository roleRepository , BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public AppUser saveUser(String username, String password, String confirm) {
        AppUser user = userRepository.findByUsername(username);
        if(user != null) throw new RuntimeException("Cet compte existe déja");
        if(!password.equals(confirm)) throw new RuntimeException("Les deux mot de pesse ne sont pas identique");

        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUser.setActive(true);

        userRepository.save(appUser);

        addRoleToUser(username,"USER");
        return appUser;
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return roleRepository.save(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
      AppRole role = roleRepository.findByRoleName(roleName);
      AppUser user = userRepository.findByUsername(username);
      user.getRoles().add(role);
    }

    @Override
    public List<AppUser> getAllUSer() {
        return userRepository.findAll();
    }
}
