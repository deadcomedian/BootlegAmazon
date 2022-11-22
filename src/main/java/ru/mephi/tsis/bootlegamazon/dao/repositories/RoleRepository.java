package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

    @Query("select r from RoleEntity r where r.name = ?1")
    Optional<RoleEntity> findByName(String name);

}
