package kane.tech.auth_micro_service.dao;

import kane.tech.auth_micro_service.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RoleRepository extends JpaRepository<AppRole,Long> {

    public AppRole findByRoleName(String roleName);
}
