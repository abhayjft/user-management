package usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import usermanagement.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByName(String name);
	//Role getAllRoleByUserID(long id);
}
