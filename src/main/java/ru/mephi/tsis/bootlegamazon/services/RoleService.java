package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.dao.entities.RoleEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.RoleNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Role;

import java.util.Comparator;
import java.util.List;

public interface RoleService {
    Role getById(Integer id) throws RoleNotFoundException;
    List<Role> getAll(Comparator<RoleEntity> comparator);
    Role getByName(String roleName, Comparator<RoleEntity> comparator) throws RoleNotFoundException;
}
