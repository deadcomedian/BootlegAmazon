package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

}
