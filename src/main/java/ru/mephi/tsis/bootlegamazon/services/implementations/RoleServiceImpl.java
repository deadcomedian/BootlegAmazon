package ru.mephi.tsis.bootlegamazon.services.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.RoleEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.RoleRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.RoleNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Role;
import ru.mephi.tsis.bootlegamazon.services.RoleService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getById(Integer id) throws RoleNotFoundException {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(()-> new RoleNotFoundException("Could not find Role with id:" + id));
        return new Role(roleEntity.getId(), roleEntity.getName());
    }

    @Override
    public List<Role> getAll(Comparator<RoleEntity> comparator){
        List<RoleEntity> roleEntities = Lists.newArrayList(roleRepository.findAll());
        return processRoles(roleEntities, comparator);
    }

    @Override
    public  Role getByName(String roleName, Comparator<RoleEntity> comparator) throws RoleNotFoundException {
        RoleEntity roleEntity = roleRepository.findByName(roleName)
                .orElseThrow(()-> new RoleNotFoundException("Could not find Role with name:" + roleName));
        return new Role(roleEntity.getId(), roleEntity.getName());
    }

    private List<Role> processRoles(List<RoleEntity> roleEntities, Comparator<RoleEntity> comparator) {
        if (comparator != null) {
            roleEntities.sort(comparator);
        }
        ArrayList<Role> roles = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            roles.add(new Role(roleEntity.getId(), roleEntity.getName()));
        }
        return roles;
    }
}
