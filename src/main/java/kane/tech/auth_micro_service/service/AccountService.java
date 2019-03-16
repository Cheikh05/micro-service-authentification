package kane.tech.auth_micro_service.service;

import kane.tech.auth_micro_service.entities.AppRole;
import kane.tech.auth_micro_service.entities.AppUser;

import java.util.List;

public interface AccountService {

    public AppUser saveUser(String username,String password,String confirm);
    public AppRole saveRole(AppRole role);
    public AppUser loadUserByUsername(String username);
    public void addRoleToUser(String username,String roleName);
    public List<AppUser> getAllUSer();
}
