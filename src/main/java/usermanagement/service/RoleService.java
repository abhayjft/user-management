package usermanagement.service;

import java.util.Collection;
import java.util.List;

import usermanagement.model.Role;

public interface RoleService {
    List <usermanagement.model.Role> getAllRole();
    void saveRole(Role role);
    Role getRoleById(long id);
    void deleteRoleById(long id);
	Role findByName(String name);
	Collection<Role> findAll();
}
