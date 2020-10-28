package usermanagement.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usermanagement.model.Role;
import usermanagement.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public List<Role> getAllRole() {
		return roleRepository.findAll();
	}

	@Override
	public void saveRole(Role role) {
		this.roleRepository.save(role);
	}

	@Override
	public Role getRoleById(long id) {
		Optional <Role> optional = roleRepository.findById(id);
		Role role = null;
		if(optional.isPresent()){
			role=optional.get();
		}else {
			throw new RuntimeException("Now role found by Id :" + id);
		}
		return role;
	}

	@Override
	public void deleteRoleById(long id) {
		this.roleRepository.deleteById(id);
		
	}

	@Override
	public Role findByName(String name) {
		return this.findByName(name);
		
	}
	
	public Collection<Role> findAll(){
		return this.roleRepository.findAll();
		
	}
	
}

